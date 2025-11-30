package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.ratatouille.util.registrar.ParticleTypeRegistrar;
import dev.spiritstudios.umbra_express.UmbraExpress;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface UmbraExpressParticles {

    ParticleTypeRegistrar REGISTRAR = new ParticleTypeRegistrar(UmbraExpress.MOD_ID);

    SimpleParticleType CRYSTAL_BALL_SPARKLE = (SimpleParticleType) REGISTRAR.create("crystal_ball_sparkle", FabricParticleTypes.simple());

    static void init() {
        REGISTRAR.registerEntries();
    }

}
