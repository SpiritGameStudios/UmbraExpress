package dev.spiritstudios.umbra_express.mixin.dev;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerShopComponent;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.doctor4t.trainmurdermystery.game.MurderGameMode;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.init.UmbraExpressConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Debug(export = true)
@Mixin(value = MurderGameMode.class, remap = false, priority = 500)
public class MurderGameModeMixin {

	@Inject(method = "assignRolesAndGetKillerCount", at = @At("RETURN"), cancellable = true)
	private static void forceDevRole(@NotNull ServerWorld serverWorld, @NotNull List<ServerPlayerEntity> players, GameWorldComponent gameComponent, CallbackInfoReturnable<Integer> cir) {
		if (UmbraExpressConfig.development(serverWorld) && !players.isEmpty()) {
			GameProfile host = serverWorld.getServer().getHostProfile(); // allows carpet
			if (host == null) {
				return;
			}

			PlayerEntity player = serverWorld.getPlayerByUuid(host.getId());
			if (player == null) {
				return;
			}

			Role devRole = UmbraExpressConfig.getDevRole();
			if (devRole == null) {
				return;
			}

			gameComponent.addRole(player, devRole);

			boolean devKiller = devRole.canUseKiller();
			if (devKiller) {
				PlayerShopComponent.KEY.get(player).setBalance(GameConstants.MONEY_START);
			}
			cir.setReturnValue(cir.getReturnValueI() + (devKiller ? 1 : 0));
		}
	}

	@Definition(id = "floor", method = "Ljava/lang/Math;floor(D)D")
	@Expression("(int) floor(?)")
	@ModifyExpressionValue(method = "assignRolesAndGetKillerCount", at = @At("MIXINEXTRAS:EXPRESSION"), remap = true)
	private static int modifyKillerCount(int original, @Local(argsOnly = true) ServerWorld serverWorld) {
		return UmbraExpressConfig.getKillerCount(original, serverWorld);
	}

	@Definition(id = "winStatus", local = @Local(type = GameFunctions.WinStatus.class, name = "winStatus"))
	@Definition(id = "NONE", field = "Ldev/doctor4t/trainmurdermystery/game/GameFunctions$WinStatus;NONE:Ldev/doctor4t/trainmurdermystery/game/GameFunctions$WinStatus;", remap = false)
	@Expression("winStatus != NONE")
	@ModifyExpressionValue(method = "tickServerGameLoop", at = @At("MIXINEXTRAS:EXPRESSION"), remap = true)
	private boolean endlessDev(boolean original, ServerWorld serverWorld, GameWorldComponent game) {
        if (UmbraExpressConfig.development(serverWorld)) {
			if (UmbraExpress.DEVELOPMENT && FabricLoader.getInstance().isModLoaded("carpet")) {
				return original;
			}
			return false;
        }
		return original;
	}
}
