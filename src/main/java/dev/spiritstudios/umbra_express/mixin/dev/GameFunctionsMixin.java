package dev.spiritstudios.umbra_express.mixin.dev;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.spiritstudios.umbra_express.init.UmbraExpressConfig;
import net.minecraft.server.world.ServerWorld;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GameFunctions.class, remap = false)
public class GameFunctionsMixin {

    @ModifyExpressionValue(method = "startGame", at = @At(value = "FIELD", target = "Ldev/doctor4t/trainmurdermystery/api/GameMode;minPlayerCount:I", opcode = Opcodes.GETFIELD), remap = true)
    private static int setToZeroIfDevelopment(int original, @Local(argsOnly = true, name = "arg0")ServerWorld serverWorld) {
        if (UmbraExpressConfig.development(serverWorld)) {
            return 0;
        }
        return UmbraExpressConfig.getMinPlayerCount(original, serverWorld);
    }
}
