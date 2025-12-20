package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import dev.doctor4t.wathe.compat.TrainVoicePlugin;
import dev.doctor4t.wathe.index.WatheItems;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import dev.spiritstudios.umbra_express.cca.CooldownWorldComponent;
import dev.spiritstudios.umbra_express.duck.HitListWorldComponent;
import dev.spiritstudios.umbra_express.event.DefaultShopEntryEvents;
import dev.spiritstudios.umbra_express.event.TMMGameLifecycleEvents;
import dev.spiritstudios.umbra_express.event.TMMPlayerEvents;
import dev.spiritstudios.umbra_express.role.MoneyManager;
import dev.spiritstudios.umbra_express.voicechat.ConductorVoicechatPlugin;
import dev.spiritstudios.umbra_express.voicechat.HauntingVoicechatPlugin;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

@ApiStatus.NonExtendable
public interface UmbraExpressEvents {

    static void registerGameLifecycle() {
        TMMGameLifecycleEvents.INITIALIZED.register((serverWorld, game) -> {
            HitListWorldComponent hitlist = HitListWorldComponent.cast(game);
            resetWorld(serverWorld, hitlist);
            hitlist.umbra_express$rerollTarget();
        });

        // is it necessary to split these??
        TMMGameLifecycleEvents.FINALIZING.register((serverWorld, game) -> resetWorld(serverWorld, HitListWorldComponent.cast(game)));
        TMMGameLifecycleEvents.FINALIZED.register((serverWorld, game) -> HauntingVoicechatPlugin.reset());
    }

    static void registerPlayer() {
        TMMPlayerEvents.INITIALIZING.register((serverWorld, serverPlayer, role, playing, game) -> {
			if (!safeRoleEquals(role, UmbraExpressRoles.CONDUCTOR))
				ConductorVoicechatPlugin.addReceiver(serverPlayer);

            if (!playing) {
                TrainVoicePlugin.addPlayer(serverPlayer.getUuid()); // haunting
                return;
            }

            if (safeRoleEquals(role, UmbraExpressRoles.LOCKSMITH))
                giveItem(serverPlayer, UmbraExpressItems.MASTER_KEY);

            if (MoneyManager.ROLE_MAP.containsKey(role))
                PlayerShopComponent.KEY.get(serverPlayer).setBalance(MoneyManager.ROLE_MAP.get(role).startingAmount());
        });

        TMMPlayerEvents.TICK.register((serverWorld, serverPlayer, role, playing, game) -> {
            if (!playing) {
                HitListWorldComponent hitlist = HitListWorldComponent.cast(game);

                if (Objects.equals(serverPlayer.getUuid(), hitlist.umbra_express$getTarget()))
                    hitlist.umbra_express$rerollTarget();
            }
        });

        TMMPlayerEvents.DIED.register((world, player, playerRole, killer, killerRole, deathReason, game) -> {
            if (safeRoleEquals(playerRole, UmbraExpressRoles.CONDUCTOR))
                BroadcastWorldComponent.KEY.get(world).setBroadcasting(false);

            if (killer != null && safeRoleEquals(killerRole, UmbraExpressRoles.ASSASSIN) && game.isRunning()) {
                HitListWorldComponent hitlist = HitListWorldComponent.cast(game);

				if (Objects.equals(player.getUuid(), hitlist.umbra_express$getTarget()))
					hitlist.umbra_express$addKilledTarget();

                hitlist.umbra_express$rerollTarget();
            }
        });
    }

	static void registerShop() {
		DefaultShopEntryEvents.MODIFY_PRICE.register((shopEntry, currentPrice) -> {
			ItemStack stack = shopEntry.stack();
			if (stack.isOf(WatheItems.POISON_VIAL) || stack.isOf(WatheItems.BODY_BAG)) {
				return (int) (currentPrice * 0.6);
			}
			if (stack.isOf(WatheItems.SCORPION)) {
				return (int) (currentPrice * 0.8);
			}
			return currentPrice;
		});
	}

    static void resetWorld(ServerWorld serverWorld, HitListWorldComponent hitList) {
        ConductorVoicechatPlugin.reset();
        CooldownWorldComponent.resetAll(serverWorld);
        hitList.umbra_express$reset();
    }

    static boolean safeRoleEquals(Role a, Role b) {
        return Objects.equals(a, b);
    }

    static void giveItem(ServerPlayerEntity serverPlayer, Item item) {
        serverPlayer.giveItemStack(item.getDefaultStack());
    }
}
