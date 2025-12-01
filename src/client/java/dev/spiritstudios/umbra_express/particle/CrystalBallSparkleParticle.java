package dev.spiritstudios.umbra_express.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;

/**
 * @author axialeaa
 */
public class CrystalBallSparkleParticle extends SpriteBillboardParticle {

    public CrystalBallSparkleParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;

        this.scale(MathHelper.nextBetween(this.random, 0.1F, 0.25F));
        this.setSprite(spriteProvider);

        this.maxAge = 20;
    }

    /**
     * PLEASE DON'T CHANGE THIS TO TRANSLUCENT!!
     * The particles are designed to appear inside the ball.
     * Translucent particles only render behind translucent quads in fabulous graphics which is force-disabled.
     */
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected int getBrightness(float tint) {
        return LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new CrystalBallSparkleParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
        }

    }

}
