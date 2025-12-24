package dev.spiritstudios.umbra_express.mixin.roles;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.game.MurderGameMode;
import dev.spiritstudios.umbra_express.duck.ConductorWorldComponent;
import dev.spiritstudios.umbra_express.init.UmbraExpressConfig;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import dev.spiritstudios.umbra_express.role.RoleReplacer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
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
		List<Identifier> disabled = UmbraExpressConfig.getDisabledRoles();

		final int totalPlayers = players.size();
		for (RoleReplacer replacer : UmbraExpressRoles.ROLE_REPLACEMENTS) {
			if (disabled.contains(replacer.replacement().identifier())) {
				continue;
			}
			replacer.replace(world, gameComponent, totalPlayers);
		}

		if (!disabled.contains(UmbraExpressRoles.CONDUCTOR)) {
			Util.getRandomOrEmpty(players, world.getRandom())
				.ifPresent(player ->
					ConductorWorldComponent.cast(gameComponent).umbra_express$setConductor(player.getUuid())
				);
		}
	}

	@WrapOperation(method = "initializeGame", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;indexOf(Ljava/lang/Object;)I"), remap = true)
	private int customAnnouncementTexts(ArrayList<RoleAnnouncementTexts.RoleAnnouncementText> instance, Object o, Operation<Integer> original, @Local(name = "player") ServerPlayerEntity serverPlayerEntity, @Local(argsOnly = true, name = "arg2") GameWorldComponent gameWorldComponent) {
		Role role = gameWorldComponent.getRole(serverPlayerEntity);
        return original.call(instance, UmbraExpressRoles.TEXTS.containsKey(role) ? UmbraExpressRoles.TEXTS.get(role) : o);
    }
}
