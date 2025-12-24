package dev.spiritstudios.umbra_express.duck;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public interface ConductorWorldComponent {

	default @Nullable UUID umbra_express$getConductor() {
		throw new UnsupportedOperationException("Duck interface");
	}

	default void umbra_express$setConductor(@Nullable UUID uuid) {
		throw new UnsupportedOperationException("Duck interface");
	}

	default boolean umbra_express$isConductor(@Nullable UUID uuid) {
		return uuid != null && Objects.equals(uuid, this.umbra_express$getConductor());
	}

	default boolean umbra_express$isConductor(@Nullable PlayerEntity player) {
		return player != null && this.umbra_express$isConductor(player.getUuid());
	}

	/**
	 * avoids having to suppress the warning for every cast
	 * @param game the game world instance to cast
	 * @return the conductor component
	 */
	static ConductorWorldComponent cast(GameWorldComponent game) {
		assert game instanceof ConductorWorldComponent;
		return (ConductorWorldComponent) game;
	}
}
