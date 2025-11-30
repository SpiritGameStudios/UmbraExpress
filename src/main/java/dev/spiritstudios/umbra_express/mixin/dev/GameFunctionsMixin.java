package dev.spiritstudios.umbra_express.mixin.dev;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.spiritstudios.umbra_express.UmbraExpress;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GameFunctions.class, remap = false)
public class GameFunctionsMixin {

    @ModifyExpressionValue(method = "startGame", at = @At(value = "FIELD", target = "Ldev/doctor4t/trainmurdermystery/api/GameMode;minPlayerCount:I", opcode = Opcodes.GETFIELD), remap = true)
    private static int setToZeroIfDevelopment(int original) {
        if (UmbraExpress.DEVELOPMENT) {
            return 0;
        }
        return original;
    }
}
