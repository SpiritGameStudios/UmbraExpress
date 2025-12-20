package dev.spiritstudios.umbra_express.mixin.qol;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.doctor4t.wathe.block.MountableBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MountableBlock.class)
public class MountableBlockMixin {

	@ModifyExpressionValue(method = "onUse", at = @At(value = "CONSTANT", args = "doubleValue=1.5"))
	private double increaseInteractionDistance(double original) {
		return original * 2;
	}
}
