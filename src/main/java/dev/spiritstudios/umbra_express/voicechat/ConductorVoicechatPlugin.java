package dev.spiritstudios.umbra_express.voicechat;

import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.audiochannel.StaticAudioChannel;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.compat.TrainVoicePlugin;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static dev.doctor4t.trainmurdermystery.compat.TrainVoicePlugin.SERVER_API;

/**
 * @see TrainVoicePlugin
 */
public class ConductorVoicechatPlugin implements VoicechatPlugin {
    public static final Identifier ID = UmbraExpress.id("conductor");
    public static final UUID BROADCAST_UUID = UUID.randomUUID();
    @Nullable
    public static StaticAudioChannel announcementChannel;
    public static Set<ServerPlayerEntity> receivers = new HashSet<>();
    public static UUID announcerUUID;

    public static void addReceiver(ServerPlayerEntity player) {
        if (TrainVoicePlugin.isVoiceChatMissing()) {
            return;
        }

        VoicechatConnection playerConnection = SERVER_API.getConnectionOf(player.getUuid());
        if (playerConnection == null) {
            return;
        }

        if (announcementChannel == null) {
            announcementChannel = SERVER_API.createStaticAudioChannel(BROADCAST_UUID);
        }

        if (announcementChannel == null) {
            return;
        }

        receivers.add(player);
        announcementChannel.addTarget(playerConnection);
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, ConductorVoicechatPlugin::play);
    }

    public static void play(MicrophonePacketEvent event) {
        if (TrainVoicePlugin.isVoiceChatMissing()) {
            return;
        }

        VoicechatConnection playerConnection = event.getSenderConnection();
        if (playerConnection == null || !(playerConnection.getPlayer().getPlayer() instanceof ServerPlayerEntity serverPlayer)) {
            return;
        }

        if (receivers.contains(serverPlayer)) {
            return;
        }

        World world = serverPlayer.getEntityWorld();
        GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(world);
        if (gameWorldComponent.isRunning()) {
            if (!BroadcastWorldComponent.KEY.get(world).isBroadcasting() || !gameWorldComponent.isRole(serverPlayer, UmbraExpressRoles.CONDUCTOR)) {
                return;
            }
        } else if (!serverPlayer.getUuid().equals(announcerUUID)) {
            return;
        }

        MicrophonePacket microphonePacket = event.getPacket();
		//noinspection NonStrictComparisonCanBeEquality
		if (microphonePacket.getOpusEncodedData().length <= 0 || microphonePacket.isWhispering()) {
            return;
        }

        if (announcementChannel != null) announcementChannel.send(microphonePacket);
    }

    public static void vanillaBroadcast(ServerPlayerEntity broadcaster) {
        ServerWorld serverWorld = broadcaster.getServerWorld();
        BroadcastWorldComponent.KEY.get(serverWorld).setAnnouncerUuid(broadcaster.getUuid());
        for (ServerPlayerEntity serverPlayer : serverWorld.getPlayers()) {
            if (serverPlayer.equals(broadcaster)) {
                continue;
            }

            addReceiver(serverPlayer);
        }
    }

    public static void reset() {
        if (announcementChannel != null) announcementChannel.clearTargets();
        receivers.clear();
    }

    @Override
    public String getPluginId() {
        return ID.toString();
    }
}
