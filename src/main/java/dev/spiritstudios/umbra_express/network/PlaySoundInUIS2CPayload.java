package dev.spiritstudios.umbra_express.network;

import dev.spiritstudios.umbra_express.UmbraExpress;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundEvent;

public record PlaySoundInUIS2CPayload(SoundEvent soundEvent, float pitch, float volume) implements CustomPayload {

    public static final Id<PlaySoundInUIS2CPayload> PAYLOAD_ID = UmbraExpress.payloadId("play_sound_in_ui_s2c");
    public static final PacketCodec<RegistryByteBuf, PlaySoundInUIS2CPayload> PACKET_CODEC = PacketCodec.tuple(
        SoundEvent.PACKET_CODEC, PlaySoundInUIS2CPayload::soundEvent,
        PacketCodecs.FLOAT, PlaySoundInUIS2CPayload::pitch,
        PacketCodecs.FLOAT, PlaySoundInUIS2CPayload::volume,
        PlaySoundInUIS2CPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

}
