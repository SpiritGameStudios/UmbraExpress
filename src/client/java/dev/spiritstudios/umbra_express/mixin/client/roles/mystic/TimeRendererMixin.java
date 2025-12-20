package dev.spiritstudios.umbra_express.mixin.client.roles.mystic;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import dev.doctor4t.wathe.cca.GameTimeComponent;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.client.gui.TimeRenderer;
import dev.doctor4t.wathe.client.gui.TimeRenderer.TimeNumberRenderer;
import dev.spiritstudios.umbra_express.UmbraExpressClient;
import dev.spiritstudios.umbra_express.cca.CrystalBallWorldComponent;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(value = TimeRenderer.class, remap = false)
public class TimeRendererMixin {

	@ModifyExpressionValue(method = "renderHud", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/api/Role;canSeeTime()Z", remap = false), remap = true)
	private static boolean mysticCooldown(boolean original, @Share("isMystic")LocalBooleanRef localBooleanRef, @Local(argsOnly = true, name = "arg1") ClientPlayerEntity player, @Local(name = "gameWorldComponent") GameWorldComponent gameWorldComponent) {
		boolean mystic = gameWorldComponent.isRole(player, UmbraExpressRoles.MYSTIC) && CrystalBallWorldComponent.KEY.get(player.getWorld()).isOnCooldown();
		localBooleanRef.set(mystic);
		return original || mystic;
	}

	@WrapOperation(method = "renderHud", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/cca/GameTimeComponent;getTime()I", remap = false), remap = true)
	private static int replaceTimeWithMystic(GameTimeComponent instance, Operation<Integer> original, @Local(argsOnly = true, name = "arg1") ClientPlayerEntity player, @Share("isMystic") LocalBooleanRef localBooleanRef) {
		if (localBooleanRef.get()) {
			return CrystalBallWorldComponent.KEY.get(player.getWorld()).getTicksForRendering();
		}
		return original.call(instance);
	}

	@WrapOperation(method = "renderHud", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/client/gui/TimeRenderer$TimeNumberRenderer;render(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/DrawContext;IIIF)V"), remap = true)
	private static void changeMysticCooldownColor(TimeNumberRenderer instance, TextRenderer renderer, DrawContext context, int x, int y, int color, float delta, Operation<Void> original, @Share("isMystic") LocalBooleanRef localBooleanRef) {
		if (localBooleanRef.get()) {
			UmbraExpressClient.transformCooldownHudMatrices(context, delta);
			color = ColorHelper.Argb.lerp(0.8F, color, ColorHelper.Argb.fullAlpha(UmbraExpressRoles.MYSTIC.color()));
		}

		original.call(instance, renderer, context, x, y, color, delta);
	}
}
