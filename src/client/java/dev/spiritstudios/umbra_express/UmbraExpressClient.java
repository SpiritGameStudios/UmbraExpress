package dev.spiritstudios.umbra_express;

import dev.spiritstudios.umbra_express.init.UmbraExpressBlockEntities;
import dev.spiritstudios.umbra_express.init.UmbraExpressBlocks;
import dev.spiritstudios.umbra_express.init.UmbraExpressItems;
import dev.spiritstudios.umbra_express.init.UmbraExpressParticles;
import dev.spiritstudios.umbra_express.mixin.client.TMMItemTooltipsAccessor;
import dev.spiritstudios.umbra_express.network.PlaySoundInUIS2CPayload;
import dev.spiritstudios.umbra_express.network.WiggleCrystalBallCooldownHudS2CPayload;
import dev.spiritstudios.umbra_express.particle.CrystalBallSparkleParticle;
import dev.spiritstudios.umbra_express.render.BroadcastButtonBlockEntityRenderer;
import dev.spiritstudios.umbra_express.render.CrystalBallBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class UmbraExpressClient implements ClientModInitializer {

    private static final int MAX_COOLDOWN_HUD_WIGGLE_TIME = 8;
    private static final int HUD_WIGGLE_COUNT = 3;
    private static int crystalBallCooldownHudWiggleTime = 0;

    @SuppressWarnings("UnreachableCode")
    @Override
    public void onInitializeClient() {
		BlockEntityRendererFactories.register(UmbraExpressBlockEntities.BROADCAST_BUTTON, BroadcastButtonBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(UmbraExpressBlockEntities.CRYSTAL_BALL, CrystalBallBlockEntityRenderer::new);

        ParticleFactoryRegistry.getInstance().register(UmbraExpressParticles.CRYSTAL_BALL_SPARKLE, CrystalBallSparkleParticle.Factory::new);
        BlockRenderLayerMap.INSTANCE.putBlock(UmbraExpressBlocks.CRYSTAL_BALL, RenderLayer.getTranslucent());

        ClientPlayNetworking.registerGlobalReceiver(PlaySoundInUIS2CPayload.PAYLOAD_ID, (payload, context) -> {
            context.client().getSoundManager().play(PositionedSoundInstance.master(payload.soundEvent(), payload.pitch(), payload.volume()));
        });

        ClientPlayNetworking.registerGlobalReceiver(WiggleCrystalBallCooldownHudS2CPayload.PAYLOAD_ID, (payload, context) ->
            crystalBallCooldownHudWiggleTime = MAX_COOLDOWN_HUD_WIGGLE_TIME
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (crystalBallCooldownHudWiggleTime > 0)
                crystalBallCooldownHudWiggleTime--;
        });

        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
            TMMItemTooltipsAccessor.umbra_express$invokeAddTooltipForItem(UmbraExpressItems.MASTER_KEY, itemStack, list);
            TMMItemTooltipsAccessor.umbra_express$invokeAddTooltipForItem(UmbraExpressItems.ANTIDOTE, itemStack, list);
        });
    }

    public static void transformCooldownHudMatrices(DrawContext context, float tickProgress) {
        float wiggleTime = crystalBallCooldownHudWiggleTime - tickProgress;

        if (wiggleTime <= 0)
            return;

        float wiggle = MathHelper.sin(MathHelper.PI * wiggleTime * ((float) HUD_WIGGLE_COUNT / MAX_COOLDOWN_HUD_WIGGLE_TIME)) * 0.1F;
        float animProgress = (wiggleTime - MAX_COOLDOWN_HUD_WIGGLE_TIME) / MAX_COOLDOWN_HUD_WIGGLE_TIME;
        float dampenCoefficient = 1 - animProgress;

        float scale = 1 - MathHelper.sin(MathHelper.PI * dampenCoefficient) * 0.3F * dampenCoefficient;

        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotation(wiggle));
        context.getMatrices().scale(scale, scale, scale);
    }

}
