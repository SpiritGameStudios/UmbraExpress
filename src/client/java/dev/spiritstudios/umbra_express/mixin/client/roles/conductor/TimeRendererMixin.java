package dev.spiritstudios.umbra_express.mixin.client.roles.conductor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.gui.TimeRenderer;
import dev.doctor4t.trainmurdermystery.client.gui.TimeRenderer.TimeNumberRenderer;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import dev.spiritstudios.umbra_express.duck.ConductorWorldComponent;
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

	@WrapOperation(method = "renderHud", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/api/Role;canSeeTime()Z", remap = false), remap = true)
	private static boolean showTimeIfCanBroadcast(Role instance, Operation<Boolean> original, @Local(argsOnly = true) ClientPlayerEntity player, @Local(name = "gameWorldComponent") GameWorldComponent game, @Share("isConductor")LocalBooleanRef localBooleanRef) {
		localBooleanRef.set(ConductorWorldComponent.cast(game).umbra_express$isConductor(player));
		return original.call(instance) || localBooleanRef.get();
	}

	@WrapOperation(method = "renderHud", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/client/gui/TimeRenderer$TimeNumberRenderer;render(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/DrawContext;IIIF)V"), remap = true)
	private static void renderBroadcastTimerIfConductor(TimeRenderer.TimeNumberRenderer instance, TextRenderer renderer, DrawContext context, int x, int y, int color, float delta, Operation<Void> original, @Local(argsOnly = true) ClientPlayerEntity player, @Share("isConductor")LocalBooleanRef localBooleanRef) {
		original.call(instance, renderer, context, x, y, color, delta);

		if (!localBooleanRef.get()) {
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
