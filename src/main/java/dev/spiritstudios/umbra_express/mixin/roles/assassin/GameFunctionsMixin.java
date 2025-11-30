package dev.spiritstudios.umbra_express.mixin.roles.assassin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerShopComponent;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.spiritstudios.umbra_express.duck.HitListWorldComponent;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;

@Mixin(value = GameFunctions.class, remap = false)
public class GameFunctionsMixin {

	@WrapOperation(method = "killPlayer(Lnet/minecraft/entity/player/PlayerEntity;ZLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Identifier;)V", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/cca/PlayerShopComponent;addToBalance(I)V", remap = false), remap = true)
	private static void killTarget(PlayerShopComponent instance, int amount, Operation<Void> original, PlayerEntity victim, boolean spawnBody, PlayerEntity killer, Identifier deathReason) {
		GameWorldComponent game = GameWorldComponent.KEY.get(killer.getWorld());
		if (!game.isRole(killer, UmbraExpressRoles.ASSASSIN)) {
			original.call(instance, amount);
			return;
		}

		HitListWorldComponent hitlist = (HitListWorldComponent) game;
		if (Objects.equals(victim.getUuid(), hitlist.umbra_express$getTarget())) {
			hitlist.umbra_express$addKilledTarget();
			original.call(instance, amount * 2);
			return;
		}

		original.call(instance, amount);
	}

	@WrapMethod(method = "killPlayer(Lnet/minecraft/entity/player/PlayerEntity;ZLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Identifier;)V" , remap = true)
	private static void rerollTarget(PlayerEntity victim, boolean spawnBody, PlayerEntity killer, Identifier deathReason, Operation<Void> original) {
		original.call(victim, spawnBody, killer, deathReason);
		((HitListWorldComponent) GameWorldComponent.KEY.get(victim.getWorld())).umbra_express$rerollTarget();
	}

	@WrapMethod(method = "finalizeGame")
	private static void resetHitlist(ServerWorld world, Operation<Void> original) {
		original.call(world);
		((HitListWorldComponent) GameWorldComponent.KEY.get(world)).umbra_express$reset();
	}

	@WrapMethod(method = "initializeGame")
	private static void initializeHitlist(ServerWorld world, Operation<Void> original) {
		original.call(world);
		HitListWorldComponent hitlist = (HitListWorldComponent) GameWorldComponent.KEY.get(world);
		hitlist.umbra_express$reset();
		hitlist.umbra_express$rerollTarget();
	}
}
