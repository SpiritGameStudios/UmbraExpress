package dev.spiritstudios.umbra_express.mixin.client.qol;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.wathe.client.WatheClient;
import dev.spiritstudios.umbra_express.init.UmbraExpressConfig;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WatheClient.class)
public class TMMClientMixin {

    @WrapOperation(method = "getInstinctHighlight", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;hsvToRgb(FFF)I"))
    private static int modifyKillerHighlight(float hue, float saturation, float value, Operation<Integer> original) {
        return original.call(hue, saturation, UmbraExpressConfig.accurateInstinctHighlights ? 0.5F : value);
    }

    @Inject(method = "getInstinctHighlight", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/cca/PlayerMoodComponent;getMood()F", shift = At.Shift.BY, by = 2), cancellable = true)
    private static void modifyInnocentHighlight(Entity target, CallbackInfoReturnable<Integer> cir, @Local(name = "mood") float mood) {
        if (UmbraExpressConfig.accurateInstinctHighlights)
            cir.setReturnValue(MathHelper.hsvToRgb(mood / 3.0F, 1.0F, 1.0F));
    }

}
