package dev.spiritstudios.umbra_express.mixin.client.roles.shop;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.gui.StoreRenderer;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = StoreRenderer.class, remap = false)
public class StoreRendererMixin {

    @WrapOperation(method = "renderHud", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/cca/GameWorldComponent;canUseKillerFeatures(Lnet/minecraft/entity/player/PlayerEntity;)Z"), remap = false)
    private static boolean canUseStoreGui(GameWorldComponent instance, PlayerEntity player, Operation<Boolean> original) {
        return UmbraExpressRoles.MONEY_MAKERS.containsKey(instance.getRole(player));
    }

}
