package dev.spiritstudios.umbra_express.mixin.optimization;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.doctor4t.trainmurdermystery.cca.PlayerShopComponent;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GameFunctions.class, remap = false)
public class GameFunctionsMixin {

    @WrapWithCondition(method = "baseInitialize", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/cca/PlayerShopComponent;reset()V", ordinal = 1), remap = false)
    private static boolean disableRedundantShopSync(PlayerShopComponent instance) {
        return false;
    }

}
