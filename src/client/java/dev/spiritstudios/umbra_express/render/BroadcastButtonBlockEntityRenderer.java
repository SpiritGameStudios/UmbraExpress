package dev.spiritstudios.umbra_express.render;

import dev.spiritstudios.umbra_express.block.entity.BroadcastButtonBlockEntity;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@SuppressWarnings("ClassCanBeRecord")
public class BroadcastButtonBlockEntityRenderer implements BlockEntityRenderer<BroadcastButtonBlockEntity> {
    protected final TextRenderer textRenderer;

    public BroadcastButtonBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.textRenderer = ctx.getTextRenderer();
	}

    @Override
    public void render(BroadcastButtonBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Camera camera = MinecraftClient.getInstance().getEntityRenderDispatcher().camera;

		matrices.push();

		matrices.translate(0.5, 0, 0.5);
        final float signScale = 0.04F;
		matrices.scale(-signScale, -signScale, -signScale);
		matrices.translate(0, 2, 0);

		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));

        ClientWorld world = MinecraftClient.getInstance().world;
        final float lineHeight = 10;

        if (world != null) {
            BroadcastWorldComponent broadcast = BroadcastWorldComponent.KEY.get(world);
            int color = broadcast.getRenderColor();

			float seconds = (float) broadcast.getTicksForRendering() / SharedConstants.TICKS_PER_SECOND;
			float mins = seconds / 60;

			String leftSec = String.valueOf(MathHelper.floor(seconds / 10) % 10);
			String rightSec = String.valueOf(MathHelper.floor(seconds) % 10);

			String leftMin = String.valueOf(MathHelper.floor(mins / 10) % 6);
			String rightMin = String.valueOf(MathHelper.floor(mins) % 10);

			String text = leftMin + rightMin + ":" + leftSec + rightSec;

			float x = (float)(-this.textRenderer.getWidth(text) / 2);
			float y = -(4 * lineHeight / 2);

            this.textRenderer.draw(
				text,
				x, y,
				color,
				false,
				matrices.peek().getPositionMatrix(),
				vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET,
				0x00000000,
				light
			);
        }

		matrices.pop();
	}
}
