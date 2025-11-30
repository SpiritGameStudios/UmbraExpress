package dev.spiritstudios.umbra_express.role;

import dev.doctor4t.trainmurdermystery.api.Role;

public record RoleReplacer(Role original, Role replacement, PlayerNumbers playerNumbers) {

	@FunctionalInterface
	public interface PlayerNumbers {
		int numberToAssign(int total, int ofReplacedRole);
	}
}
