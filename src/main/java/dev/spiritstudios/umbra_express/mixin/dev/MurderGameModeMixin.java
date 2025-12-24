package dev.spiritstudios.umbra_express.mixin.dev;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerShopComponent;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.doctor4t.trainmurdermystery.game.MurderGameMode;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.duck.ConductorWorldComponent;
import dev.spiritstudios.umbra_express.event.TMMPlayerEvents;
import dev.spiritstudios.umbra_express.init.UmbraExpressConfig;
import dev.spiritstudios.umbra_express.role.MoneyManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

//@Debug(export = true)
@Mixin(value = MurderGameMode.class, remap = false, priority = 500)
public class MurderGameModeMixin {

	@Inject(method = "assignRolesAndGetKillerCount", at = @At("RETURN"), cancellable = true)
	private static void forceDevRole(@NotNull ServerWorld serverWorld, @NotNull List<ServerPlayerEntity> players, GameWorldComponent gameComponent, CallbackInfoReturnable<Integer> cir) {
		if (UmbraExpressConfig.development(serverWorld) && !players.isEmpty()) {
			GameProfile host = serverWorld.getServer().getHostProfile(); // allows carpet

			PlayerEntity player;
			if (host == null) {
				player = Util.getRandom(players, serverWorld.getRandom());
			} else {
				player = serverWorld.getPlayerByUuid(host.getId());
			}

			if (!(player instanceof ServerPlayerEntity serverPlayer) || !players.contains(serverPlayer)) {
				return;
			}

			UmbraExpressConfig.MaybeConductor maybe = UmbraExpressConfig.getDevRole();
			if (maybe == null) {
				return;
			}
			if (maybe.conductor()) {
				ConductorWorldComponent.cast(gameComponent).umbra_express$setConductor(serverPlayer.getUuid());
				return;
			}

			Role devRole = maybe.otherwise();
			if (devRole == null) {
				return;
			}

			boolean wasKiller = false;
			Role prevRole = gameComponent.getRole(player);
			if (prevRole != null && prevRole.canUseKiller()) {
				wasKiller = true;
			}

			gameComponent.addRole(player, devRole);

			TMMPlayerEvents.INITIALIZING.invoker().onInitializing(serverWorld, serverPlayer, devRole, true, gameComponent);

			cir.setReturnValue(cir.getReturnValueI() + (devRole.canUseKiller() && !wasKiller ? 1 : 0));
		}
	}

	@Definition(id = "floor", method = "Ljava/lang/Math;floor(D)D")
	@Expression("(int) floor(?)")
	@ModifyExpressionValue(method = "assignRolesAndGetKillerCount", at = @At("MIXINEXTRAS:EXPRESSION"), remap = true)
	private static int modifyKillerCount(int original, @Local(argsOnly = true, name = "arg0") ServerWorld serverWorld) {
		return UmbraExpressConfig.getKillerCount(original, serverWorld);
	}

	@Definition(id = "winStatus", local = @Local(type = GameFunctions.WinStatus.class, name = "winStatus"))
	@Definition(id = "NONE", field = "Ldev/doctor4t/trainmurdermystery/game/GameFunctions$WinStatus;NONE:Ldev/doctor4t/trainmurdermystery/game/GameFunctions$WinStatus;", remap = false)
	@Expression("winStatus != NONE")
	@ModifyExpressionValue(method = "tickServerGameLoop", at = @At("MIXINEXTRAS:EXPRESSION"), remap = true)
	private boolean endlessDev(boolean original, ServerWorld serverWorld, GameWorldComponent game) {
        if (!UmbraExpressConfig.development(serverWorld)) {
            return original;
        }
        return UmbraExpress.DEVELOPMENT && FabricLoader.getInstance().isModLoaded("carpet") && original;
    }
}
