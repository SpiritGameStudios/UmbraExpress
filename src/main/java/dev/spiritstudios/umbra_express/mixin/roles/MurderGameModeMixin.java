package dev.spiritstudios.umbra_express.mixin.roles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.game.MurderGameMode;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.init.UmbraExpressCommands;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import dev.spiritstudios.umbra_express.role.RoleReplacer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = MurderGameMode.class, remap = false)
public class MurderGameModeMixin {

	@Inject(method = "assignRolesAndGetKillerCount", at = @At("RETURN"), remap = true)
	private static void assignRoles(@NotNull ServerWorld world, @NotNull List<ServerPlayerEntity> players, GameWorldComponent gameComponent, CallbackInfoReturnable<Integer> cir) {
		if (UmbraExpress.DEVELOPMENT && !players.isEmpty()) {
			gameComponent.addRole(players.getFirst(), UmbraExpress.DEV_FORCED_ROLE);
			return;
		}

		List<Role> disabled = UmbraExpressCommands.getDisabledRoles();

		final int totalPlayers = players.size();
		for (RoleReplacer replacer : UmbraExpressRoles.ROLE_REPLACEMENTS) {
			replacer.replace(world, gameComponent, disabled, totalPlayers);
		}
	}

	@WrapOperation(method = "initializeGame", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;indexOf(Ljava/lang/Object;)I"), remap = true)
	private int customAnnouncementTexts(ArrayList<RoleAnnouncementTexts.RoleAnnouncementText> instance, Object o, Operation<Integer> original, @Local ServerPlayerEntity serverPlayerEntity, @Local(argsOnly = true) GameWorldComponent gameWorldComponent) {
		Role role = gameWorldComponent.getRole(serverPlayerEntity);
		if (UmbraExpressRoles.TEXTS.containsKey(role)) {
			return original.call(instance, UmbraExpressRoles.TEXTS.get(role));
		}
		return original.call(instance, o);
	}
}
