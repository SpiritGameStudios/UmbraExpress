package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.ratatouille.util.registrar.SoundEventRegistrar;
import dev.spiritstudios.umbra_express.UmbraExpress;
import net.minecraft.sound.SoundEvent;

public interface UmbraExpressSoundEvents {

    SoundEventRegistrar REGISTRAR = new SoundEventRegistrar(UmbraExpress.MOD_ID);

    SoundEvent CRYSTAL_BALL_REVEAL = REGISTRAR.create("block.crystal_ball.reveal");

    static void init() {
        REGISTRAR.registerEntries();
    }

}
