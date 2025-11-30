package dev.spiritstudios.umbra_express.role;

import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;

import java.util.List;
import java.util.UUID;

public record RoleReplacer(Role original, Role replacement, PlayerNumbers playerNumbers, ReplacementChecker checker) {

	public void replace(ServerWorld serverWorld, GameWorldComponent gameComponent, List<Role> disabled, int totalPlayers) {
		if (disabled.contains(this.replacement)) {
			return;
		}

		List<UUID> withRole = gameComponent.getAllWithRole(this.original);
		if (withRole.isEmpty()) {
			return;
		}

		int numberToAssign = this.playerNumbers.numberToTryAssign(totalPlayers, withRole.size());
		if (numberToAssign <= 0) {
			return;
		}

		for (int i = 0; i < numberToAssign; i++) {
			withRole = gameComponent.getAllWithRole(this.original);
			if (withRole.isEmpty()) {
				continue;
			}
			UUID uuid = Util.getRandom(withRole, serverWorld.getRandom());
			if (this.checker.shouldAssign(serverWorld, uuid)) {
				gameComponent.addRole(uuid, this.replacement);
			}
		}
	}

	@FunctionalInterface
	public interface PlayerNumbers {
		PlayerNumbers ONE = ((total, ofReplacedRole) -> 1);

		int numberToTryAssign(int total, int ofReplacedRole);
	}

	@FunctionalInterface
	public interface ReplacementChecker {
		ReplacementChecker ALWAYS = (serverWorld, uuid) -> true;

		boolean shouldAssign(ServerWorld serverWorld, UUID uuid);

		/**
		 * Creates a new ReplacementChecker that is based on a random
		 * @param chance the chance that the check will succeed.
		 *               At 1f, this will be basically equivalent to
		 *               {@linkplain RoleReplacer.ReplacementChecker#ALWAYS}
		 * @return the checker
		 */
		static ReplacementChecker fromRandom(float chance) {
			return (serverWorld, uuid) -> serverWorld.getRandom().nextFloat() < chance;
		}
	}
}
