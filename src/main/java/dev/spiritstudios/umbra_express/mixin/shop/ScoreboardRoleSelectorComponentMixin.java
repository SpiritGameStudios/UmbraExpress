package dev.spiritstudios.umbra_express.mixin.shop;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.doctor4t.trainmurdermystery.cca.PlayerShopComponent;
import dev.doctor4t.trainmurdermystery.cca.ScoreboardRoleSelectorComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ScoreboardRoleSelectorComponent.class, remap = false)
public class ScoreboardRoleSelectorComponentMixin {

    @WrapWithCondition(method = "assignKillers", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/cca/PlayerShopComponent;setBalance(I)V"), remap = false)
    private boolean bypassBalanceSet(PlayerShopComponent instance, int amount) {
        return false;
    }

}
