package dev.spiritstudios.umbra_express.voicechat;

import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.audiochannel.StaticAudioChannel;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import de.maxhenkel.voicechat.api.packets.MicrophonePacket;
import dev.doctor4t.wathe.compat.TrainVoicePlugin;
import dev.spiritstudios.umbra_express.UmbraExpress;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static dev.doctor4t.wathe.compat.TrainVoicePlugin.SERVER_API;

public class HauntingVoicechatPlugin implements VoicechatPlugin {
	public static final Identifier ID = UmbraExpress.id("haunting");
	public static final UUID HAUNT_UUID = UUID.randomUUID();
	@Nullable
	public static StaticAudioChannel hauntChannel;
	public static final Set<ServerPlayerEntity> killers = new HashSet<>();

	public static void initHauntForKiller(ServerPlayerEntity player) {
		if (TrainVoicePlugin.isVoiceChatMissing()) {
			return;
		}

		VoicechatConnection playerConnection = SERVER_API.getConnectionOf(player.getUuid());
		if (playerConnection == null) {
			return;
		}

		if (hauntChannel == null) {
			hauntChannel = SERVER_API.createStaticAudioChannel(HAUNT_UUID);
		}

		if (hauntChannel == null) {
			return;
		}

		killers.add(player);
		hauntChannel.addTarget(playerConnection);
	}

	@Override
	public void registerEvents(EventRegistration registration) {
		registration.registerEvent(MicrophonePacketEvent.class, HauntingVoicechatPlugin::play);
	}

	public static void play(MicrophonePacketEvent event) {
		if (TrainVoicePlugin.isVoiceChatMissing()) {
			return;
		}

		VoicechatConnection playerConnection = event.getSenderConnection();
		if (playerConnection == null || !(playerConnection.getPlayer().getPlayer() instanceof ServerPlayerEntity serverPlayer)) {
			return;
		}

		if (killers.contains(serverPlayer)) {
			return;
		}

		Group group = playerConnection.getGroup();
		if (group == null || TrainVoicePlugin.GROUP == null || !Objects.equals(group, TrainVoicePlugin.GROUP)) {
			return;
		}

		MicrophonePacket microphonePacket = event.getPacket();
		//noinspection NonStrictComparisonCanBeEquality
		if (microphonePacket.getOpusEncodedData().length <= 0 || microphonePacket.isWhispering()) {
			return;
		}

		if (hauntChannel != null) hauntChannel.send(microphonePacket);
	}

	public static void reset() {
		if (hauntChannel != null) hauntChannel.clearTargets();
		killers.clear();
	}

	@Override
	public String getPluginId() {
		return ID.toString();
	}
}
