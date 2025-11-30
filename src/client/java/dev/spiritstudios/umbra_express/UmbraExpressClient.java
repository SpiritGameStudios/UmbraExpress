package dev.spiritstudios.umbra_express;

import dev.spiritstudios.umbra_express.init.UmbraExpressBlockEntities;
import dev.spiritstudios.umbra_express.render.BroadcastButtonBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class UmbraExpressClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
		BlockEntityRendererFactories.register(UmbraExpressBlockEntities.BROADCAST_BUTTON, BroadcastButtonBlockEntityRenderer::new);
	}
}
