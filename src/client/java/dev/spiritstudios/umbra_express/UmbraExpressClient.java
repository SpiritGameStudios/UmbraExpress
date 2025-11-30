package dev.spiritstudios.umbra_express;

import dev.spiritstudios.umbra_express.init.UmbraExpressBlockEntities;
import dev.spiritstudios.umbra_express.init.UmbraExpressBlocks;
import dev.spiritstudios.umbra_express.init.UmbraExpressParticles;
import dev.spiritstudios.umbra_express.render.BroadcastButtonBlockEntityRenderer;
import dev.spiritstudios.umbra_express.render.CrystalBallBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class UmbraExpressClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
		BlockEntityRendererFactories.register(UmbraExpressBlockEntities.BROADCAST_BUTTON, BroadcastButtonBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(UmbraExpressBlockEntities.CRYSTAL_BALL, CrystalBallBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(UmbraExpressBlocks.CRYSTAL_BALL, RenderLayer.getTranslucent());
	}
}
