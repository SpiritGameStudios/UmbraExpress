package dev.spiritstudios.umbra_express.render;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.spiritstudios.umbra_express.block.CrystalBallBlock;
import dev.spiritstudios.umbra_express.block.entity.CrystalBallBlockEntity;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import static dev.spiritstudios.umbra_express.block.entity.CrystalBallBlockEntity.MAX_APPARITION_RENDER_TICKS;

/**
 * @author axialeaa
 */
public class CrystalBallBlockEntityRenderer implements BlockEntityRenderer<CrystalBallBlockEntity> {

    private static final float HEAD_PIXEL_WIDTH = 5.0F;
    private static final int BALL_WIDTH = 10;
    private static final int BASE_HEIGHT = 3;

    private static final float HEAD_SCALE = 1.0F / 8.0F * HEAD_PIXEL_WIDTH;
    private static final float HEAD_AND_BALL_SCALE_DIFF = (BALL_WIDTH - HEAD_PIXEL_WIDTH) / 2.0F;
    private static final float Y_OFFSET = (BASE_HEIGHT + HEAD_AND_BALL_SCALE_DIFF) / 16.0F;

    private static final float ALPHA_PULSE_AMPLITUDE = 0.5F;
    private static final int TINT_COLOR = 0xF281E5;

    private final EntityModelLoader entityModelLoader;
    private final PlayerSkinProvider playerSkinProvider;

    public CrystalBallBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.entityModelLoader = ctx.getLayerRenderDispatcher();
        this.playerSkinProvider = MinecraftClient.getInstance().getSkinProvider();
    }

    @Override
    public void render(CrystalBallBlockEntity blockEntity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!blockEntity.isRevealingApparition() || blockEntity.apparition == null)
            return;

        GameWorldComponent game = TMMClient.gameComponent;

        if (game != null && game.isRunning() && !game.getRole(MinecraftClient.getInstance().player).equals(UmbraExpressRoles.MYSTIC))
            return;

        matrices.push();
        transformMatrices(blockEntity, tickProgress, matrices);

        RenderLayer renderLayer = RenderLayer.getEntityTranslucent(this.playerSkinProvider.getSkinTextures(blockEntity.apparition.getGameProfile()).texture());
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);

        SkullEntityModel model = new SkullEntityModel(this.entityModelLoader.getModelPart(EntityModelLayers.PLAYER_HEAD));
        renderModel(blockEntity, tickProgress, matrices, vertexConsumer, light, model);

        matrices.pop();
    }

    private static void transformMatrices(CrystalBallBlockEntity blockEntity, float tickProgress, MatrixStack matrices) {
        Direction facing = blockEntity.getCachedState().get(CrystalBallBlock.FACING);
        matrices.translate(0.5F, Y_OFFSET + getAnimationWobble(blockEntity, tickProgress, 0.15F, 0.006F), 0.5F);

        matrices.multiply(facing.getRotationQuaternion());
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90 + getAnimationWobble(blockEntity, tickProgress, 0.08F, 2.0F)));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(getAnimationWobble(blockEntity, tickProgress, 0.1F, 1.0F)));

        matrices.scale(HEAD_SCALE, HEAD_SCALE, HEAD_SCALE);
    }

    private static float getAnimationWobble(CrystalBallBlockEntity blockEntity, float tickProgress, float wavelength, float amplitude) {
        return MathHelper.sin((blockEntity.apparitionTicks - tickProgress) * wavelength) * amplitude;
    }

    private static void renderModel(CrystalBallBlockEntity blockEntity, float tickProgress, MatrixStack matrices, VertexConsumer vertexConsumer, int light, SkullEntityModel model) {
        float animProgress = (blockEntity.apparitionTicks - tickProgress - MAX_APPARITION_RENDER_TICKS) / MAX_APPARITION_RENDER_TICKS;
        float dampenCoefficient = 1 - animProgress;

        float alpha = MathHelper.abs(MathHelper.sin(dampenCoefficient * MathHelper.PI)) * ALPHA_PULSE_AMPLITUDE * dampenCoefficient;
        int color = ColorHelper.Argb.withAlpha((int) (alpha * 255), TINT_COLOR);

        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, color);
    }

}
