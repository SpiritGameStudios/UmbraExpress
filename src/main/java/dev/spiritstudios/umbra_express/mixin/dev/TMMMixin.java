package dev.spiritstudios.umbra_express.mixin.dev;

import dev.doctor4t.wathe.Wathe;
import dev.spiritstudios.umbra_express.UmbraExpress;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Wathe.class, remap = false)
public class TMMMixin {

	@Inject(method = "isSupporter", at = @At("HEAD"), cancellable = true, remap = true)
	private static void developmentCommands(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		if (UmbraExpress.DEVELOPMENT) cir.setReturnValue(true);
	}
}
