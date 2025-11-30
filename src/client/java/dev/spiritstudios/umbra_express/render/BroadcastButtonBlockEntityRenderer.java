package dev.spiritstudios.umbra_express.render;

import dev.spiritstudios.umbra_express.block.entity.BroadcastButtonBlockEntity;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

@SuppressWarnings("ClassCanBeRecord")
public class BroadcastButtonBlockEntityRenderer implements BlockEntityRenderer<BroadcastButtonBlockEntity> {

    protected final TextRenderer textRenderer;

    public BroadcastButtonBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.textRenderer = ctx.getTextRenderer();
	}

    @Override
    public void render(BroadcastButtonBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Camera camera = MinecraftClient.getInstance().getEntityRenderDispatcher().camera;
        Vec3d cameraPos = camera.getPos();

		// text rendering is weird, I could probably use the original matrixstack but whatever
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.push();
        Vec3d pos = entity.getPos().toCenterPos().subtract(cameraPos);
        matrixStack.translate(pos.x, pos.y, pos.z);

        matrixStack.push();
        final float signScale = 0.04F;
        matrixStack.scale(-signScale, -signScale, -signScale);
        matrixStack.translate(0, 2, 0);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));

        ClientWorld clientWorld = MinecraftClient.getInstance().world;
        final float lineHeight = 10;

        if (clientWorld != null) {
            BroadcastWorldComponent broadcast = BroadcastWorldComponent.KEY.get(clientWorld);
            int color = broadcast.getRenderColor();
            OrderedText orderedText = Text.literal(String.valueOf(broadcast.getTicksForRendering())).asOrderedText();
            float x = (float)(-this.textRenderer.getWidth(orderedText) / 2);
            float j = 4 * lineHeight / 2;
            this.textRenderer.draw(orderedText, x, -j, color, false, matrixStack.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, 15728880);
        }

        matrixStack.pop();

        matrixStack.pop();
    }
}
