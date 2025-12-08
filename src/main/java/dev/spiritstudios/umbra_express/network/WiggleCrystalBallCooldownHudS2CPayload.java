package dev.spiritstudios.umbra_express.network;

import dev.spiritstudios.umbra_express.UmbraExpress;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record WiggleCrystalBallCooldownHudS2CPayload() implements CustomPayload {

    public static final Id<WiggleCrystalBallCooldownHudS2CPayload> PAYLOAD_ID = UmbraExpress.payloadId("wiggle_crystal_ball_cooldown_hud_s2c");
    public static final PacketCodec<RegistryByteBuf, WiggleCrystalBallCooldownHudS2CPayload> PACKET_CODEC = PacketCodec.unit(new WiggleCrystalBallCooldownHudS2CPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return PAYLOAD_ID;
    }

}
