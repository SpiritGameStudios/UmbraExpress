package dev.spiritstudios.umbra_express;

import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.compat.TrainVoicePlugin;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import dev.spiritstudios.umbra_express.cca.CooldownWorldComponent;
import dev.spiritstudios.umbra_express.duck.HitListWorldComponent;
import dev.spiritstudios.umbra_express.event.TMMGameLifecycleEvents;
import dev.spiritstudios.umbra_express.event.TMMPlayerEvents;
import dev.spiritstudios.umbra_express.init.UmbraExpressItems;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import dev.spiritstudios.umbra_express.voicechat.ConductorVoicechatPlugin;
import dev.spiritstudios.umbra_express.voicechat.HauntingVoicechatPlugin;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Objects;

public class UmbraExpressEvents {

    public static void registerGameLifecycle() {
        TMMGameLifecycleEvents.INITIALIZING.register((serverWorld, game, readyPlayerList) -> {
            HitListWorldComponent hitlist = HitListWorldComponent.cast(game);
            resetWorld(serverWorld, hitlist);
            hitlist.umbra_express$rerollTarget();
        });

        // is it necessary to split these??
        TMMGameLifecycleEvents.FINALIZING.register((serverWorld, game) -> resetWorld(serverWorld, HitListWorldComponent.cast(game)));
        TMMGameLifecycleEvents.FINALIZED.register((serverWorld, game) -> HauntingVoicechatPlugin.reset());
    }

    public static void registerPlayer() {
        TMMPlayerEvents.INITIALIZING.register((serverWorld, serverPlayer, role, playing, game) -> {
            if (!playing) {
                TrainVoicePlugin.addPlayer(serverPlayer.getUuid()); // haunting
                return;
            }

            if (safeRoleEquals(role, UmbraExpressRoles.LOCKSMITH))
                giveItem(serverPlayer, UmbraExpressItems.MASTER_KEY);

            if (!safeRoleEquals(role, UmbraExpressRoles.CONDUCTOR))
                ConductorVoicechatPlugin.addReceiver(serverPlayer);
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
                hitlist.umbra_express$rerollTarget();

                if (Objects.equals(player.getUuid(), hitlist.umbra_express$getTarget()))
                    hitlist.umbra_express$addKilledTarget();
            }
        });
    }

    private static void resetWorld(ServerWorld serverWorld, HitListWorldComponent hitList) {
        ConductorVoicechatPlugin.reset();
        CooldownWorldComponent.resetAll(serverWorld);
        hitList.umbra_express$reset();
    }

    private static boolean safeRoleEquals(Role a, Role b) {
        return Objects.equals(a, b);
    }

    private static void giveItem(ServerPlayerEntity serverPlayer, Item item) {
        serverPlayer.giveItemStack(item.getDefaultStack());
    }

}
