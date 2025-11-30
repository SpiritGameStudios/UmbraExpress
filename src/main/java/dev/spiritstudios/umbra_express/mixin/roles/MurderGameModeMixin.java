package dev.spiritstudios.umbra_express.mixin.roles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.game.MurderGameMode;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import dev.spiritstudios.umbra_express.role.RoleReplacer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(value = MurderGameMode.class, remap = false)
public class MurderGameModeMixin {

	@Inject(method = "assignRolesAndGetKillerCount", at = @At("RETURN"), remap = true)
	private static void assignRoles(@NotNull ServerWorld world, @NotNull List<ServerPlayerEntity> players, GameWorldComponent gameComponent, CallbackInfoReturnable<Integer> cir) {
		if (UmbraExpress.DEVELOPMENT && !players.isEmpty()) {
			gameComponent.addRole(players.getFirst(), UmbraExpress.DEV_FORCED_ROLE);
			return;
		}

		final int totalPlayers = players.size();
		for (RoleReplacer replacer : UmbraExpressRoles.ROLE_REPLACEMENTS) {
			final Role original = replacer.original();
			List<UUID> withRole = gameComponent.getAllWithRole(original);
			if (withRole.isEmpty()) {
				continue;
			}

			int numberToAssign = replacer.playerNumbers().numberToTryAssign(totalPlayers, withRole.size());
			if (numberToAssign <= 0) {
				continue;
			}

			final Role replacement = replacer.replacement();
			final RoleReplacer.ReplacementChecker checker = replacer.checker();

			for (int i = 0; i < numberToAssign; i++) {
				withRole = gameComponent.getAllWithRole(original);
				if (withRole.isEmpty()) {
					continue;
				}
				UUID uuid = Util.getRandom(withRole, world.getRandom());
				if (checker.shouldAssign(world, uuid)) {
					gameComponent.addRole(uuid, replacement);
				}
			}
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
