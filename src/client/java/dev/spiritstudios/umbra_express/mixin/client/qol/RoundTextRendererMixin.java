package dev.spiritstudios.umbra_express.mixin.client.qol;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.client.gui.RoundTextRenderer;
import dev.spiritstudios.umbra_express.UmbraExpress;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RoundTextRenderer.class, remap = false)
public class RoundTextRendererMixin {

	@Shadow
	private static int welcomeTime;
	@Unique
	private static boolean umbra_express$forceShowWelcome = false;

	@WrapMethod(method = "renderHud")
	private static void showWelcomeWhenPressingPlayerList(TextRenderer renderer, ClientPlayerEntity player, DrawContext context, Operation<Void> original) {
		if (!umbra_express$forceShowWelcome) {
			original.call(renderer, player, context);
			return;
		}

		int realWelcomeTime = welcomeTime;
		welcomeTime = 2;
		try {
			original.call(renderer, player, context);
		} catch (Throwable throwable) {
			UmbraExpress.LOGGER.error("An error occurred when wrapping RoundTextRenderer.renderHud", throwable);
		}
		if (welcomeTime == 2) {
			welcomeTime = realWelcomeTime;
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private static void assumeFalse(CallbackInfo ci) {
		umbra_express$forceShowWelcome = false;
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;isPressed()Z"))
	private static boolean updatePressingPlayerList(boolean original, @Local ClientPlayerEntity player) {
		if (!original) {
			return false;
		}
		if (TMMClient.gameComponent != null) {
			umbra_express$forceShowWelcome = TMMClient.gameComponent.isRunning() && (player == null || !player.isSpectator());
		}
		return true;
	}
}
