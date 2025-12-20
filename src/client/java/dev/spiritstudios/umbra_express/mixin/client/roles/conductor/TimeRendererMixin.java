package dev.spiritstudios.umbra_express.mixin.client.roles.conductor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.client.gui.TimeRenderer;
import dev.doctor4t.wathe.client.gui.TimeRenderer.TimeNumberRenderer;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(value = TimeRenderer.class, remap = false)
public class TimeRendererMixin {

	@Unique
	private static final TimeNumberRenderer umbra_express$VIEW = new TimeNumberRenderer();

	@WrapOperation(method = "renderHud", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/client/gui/TimeRenderer$TimeNumberRenderer;render(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/DrawContext;IIIF)V"), remap = true)
	private static void renderBroadcastTimerIfConductor(TimeRenderer.TimeNumberRenderer instance, TextRenderer renderer, DrawContext context, int x, int y, int color, float delta, Operation<Void> original, @Local(argsOnly = true, name = "arg1") ClientPlayerEntity player, @Local(name = "gameWorldComponent") GameWorldComponent game) {
		original.call(instance, renderer, context, x, y, color, delta);

		if (!game.isRole(player, UmbraExpressRoles.CONDUCTOR)) {
			umbra_express$VIEW.setTarget(0);
			return;
		}

		BroadcastWorldComponent broadcastWorldComponent = BroadcastWorldComponent.KEY.get(player.getWorld());
		umbra_express$VIEW.setTarget(broadcastWorldComponent.getTicksForRendering());
		original.call(umbra_express$VIEW, renderer, context, x, renderer.fontHeight + 8, broadcastWorldComponent.getRenderColor(), delta);
	}

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/client/gui/TimeRenderer$TimeNumberRenderer;update()V"))
	private static void tickConductorView(TimeNumberRenderer instance, Operation<Void> original) {
		original.call(instance);
		original.call(umbra_express$VIEW);
	}
}
