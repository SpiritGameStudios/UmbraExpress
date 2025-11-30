package dev.spiritstudios.umbra_express.role;

import dev.doctor4t.trainmurdermystery.api.Role;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.UUID;

public record RoleReplacer(Role original, Role replacement, PlayerNumbers playerNumbers, ReplacementChecker checker) {

	@FunctionalInterface
	public interface PlayerNumbers {
		PlayerNumbers ONE = ((total, ofReplacedRole) -> 1);

		int numberToTryAssign(int total, int ofReplacedRole);
	}

	@FunctionalInterface
	public interface ReplacementChecker {
		ReplacementChecker ALWAYS = (world, uuid) -> true;

		boolean shouldAssign(World world, UUID uuid);

		/**
		 * Creates a new ReplacementChecker that is based on a random
		 * @param chance the chance that the check will succeed.
		 *               At 1f, this will be basically equivalent to
		 *               {@linkplain RoleReplacer.ReplacementChecker#ALWAYS}
		 * @return the checker
		 */
		static ReplacementChecker fromRandom(float chance) {
			return (world, uuid) -> world.getRandom().nextFloat() < chance;
		}
	}
}
