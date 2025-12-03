package dev.spiritstudios.umbra_express;

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

import java.util.Objects;

@SuppressWarnings("CastToIncompatibleInterface")
public class UmbraExpressEvents {

    public static void registerGameLifecycle() {
        TMMGameLifecycleEvents.INITIALIZING.register((serverWorld, game, readyPlayerList) -> {
            ConductorVoicechatPlugin.reset();
            CooldownWorldComponent.resetAll(serverWorld);

            HitListWorldComponent hitlist = (HitListWorldComponent) game;
            hitlist.umbra_express$reset();
            hitlist.umbra_express$rerollTarget();
        });

        TMMGameLifecycleEvents.FINALIZING.register((serverWorld, game) -> {
            ConductorVoicechatPlugin.reset();
            CooldownWorldComponent.resetAll(serverWorld);

            ((HitListWorldComponent) game).umbra_express$reset();
        });

        TMMGameLifecycleEvents.FINALIZED.register((serverWorld, game) -> HauntingVoicechatPlugin.reset());
    }

    public static void registerPlayer() {
        TMMPlayerEvents.INITIALIZING.register((serverWorld, serverPlayer, role, playing, game) -> {
            if (!playing) {
                TrainVoicePlugin.addPlayer(serverPlayer.getUuid()); // haunting
                return;
            }

            if (role.equals(UmbraExpressRoles.LOCKSMITH))
                serverPlayer.giveItemStack(UmbraExpressItems.MASTER_KEY.getDefaultStack());
            else if (role.equals(UmbraExpressRoles.CONDUCTOR))
                ConductorVoicechatPlugin.addReceiver(serverPlayer);
        });

        TMMPlayerEvents.TICK.register((serverWorld, serverPlayer, role, playing, game) -> {
            if (playing)
                return;

            HitListWorldComponent hitlist = (HitListWorldComponent) game;

            if (Objects.equals(serverPlayer.getUuid(), hitlist.umbra_express$getTarget()))
                hitlist.umbra_express$rerollTarget();
        });

        TMMPlayerEvents.DIED.register((world, player, killer, deathReason, game) -> {
            if (game.getRole(player).equals(UmbraExpressRoles.CONDUCTOR))
                BroadcastWorldComponent.KEY.get(world).setBroadcasting(false);

            if (killer == null || !game.getRole(killer).equals(UmbraExpressRoles.ASSASSIN) || !game.isRunning())
                return;

            HitListWorldComponent hitlist = (HitListWorldComponent) game;
            hitlist.umbra_express$rerollTarget();

            if (Objects.equals(player.getUuid(), hitlist.umbra_express$getTarget()))
                hitlist.umbra_express$addKilledTarget();
        });
    }

}
