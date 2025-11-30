package dev.spiritstudios.umbra_express.mixin.dev;

import dev.doctor4t.trainmurdermystery.game.MurderGameMode;
import dev.spiritstudios.umbra_express.UmbraExpress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MurderGameMode.class, remap = false)
public class MurderGameModeMixin {

    @Inject(method = "tickServerGameLoop", at = @At("HEAD"), cancellable = true, remap = true)
    private void endlessDev(CallbackInfo ci) {
        if (UmbraExpress.DEVELOPMENT) {
            ci.cancel();
        }
    }
}
