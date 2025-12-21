package dev.spiritstudios.umbra_express.role;

import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;

import java.util.List;
import java.util.UUID;

public record RoleReplacer(Role original, Role replacement, ReplacementQuotient replacementQuotient, ReplacementPredicate replacementPredicate) {

	public void replace(ServerWorld serverWorld, GameWorldComponent gameComponent, int totalPlayers) {
		List<UUID> withRole = gameComponent.getAllWithRole(this.original);
		if (withRole.isEmpty()) {
			return;
		}

		int numberToAssign = this.replacementQuotient.get(totalPlayers, withRole.size());
		if (numberToAssign <= 0) {
			return;
		}

		for (int i = 0; i < numberToAssign; i++) {
			withRole = gameComponent.getAllWithRole(this.original);
			if (withRole.isEmpty()) {
				return;
			}
			UUID uuid = Util.getRandom(withRole, serverWorld.getRandom());

            if (serverWorld.getPlayerByUuid(uuid) instanceof ServerPlayerEntity serverPlayer) {
                if (this.replacementPredicate.shouldAssign(totalPlayers, serverWorld, serverPlayer)) {
                    gameComponent.addRole(uuid, this.replacement);
                }
            }
        }
	}

	@FunctionalInterface
	public interface ReplacementQuotient {
		ReplacementQuotient ALL_OF = (total, withRole) -> withRole;
		ReplacementQuotient ONE_OF = (total, withRole) -> 1;

		int get(int total, int withRole);
	}

	@FunctionalInterface
	public interface ReplacementPredicate {
		ReplacementPredicate ALWAYS = (totalPlayers, serverWorld, serverPlayer) -> true;

		boolean shouldAssign(int totalPlayers, ServerWorld serverWorld, ServerPlayerEntity serverPlayer);

		/**
		 * Creates a new ReplacementPredicate that is based on a random
		 * @param chance the chance that the check will succeed.
		 *               At 1f, this will be equivalent to
		 *               {@linkplain ReplacementPredicate#ALWAYS}
		 * @return the predicate
		 */
		static ReplacementPredicate fromRandom(float chance) {
			return (totalPlayers, serverWorld, uuid) -> serverWorld.getRandom().nextFloat() <= chance;
		}

		/**
		 * Creates a new ReplacementPredicate that is based on a random
		 * @param players the number of players required to join the game.
		 * @return the checker
		 */
		static ReplacementPredicate minPlayers(int players) {
			return (totalPlayers, serverWorld, serverPlayer) -> totalPlayers >= players;
		}
	}

}
