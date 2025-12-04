package dev.spiritstudios.umbra_express.duck;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;

import java.util.List;
import java.util.UUID;

public interface HitListWorldComponent {

	default void umbra_express$rerollTarget() {
		throw new UnsupportedOperationException("Duck interface");
	}

	default UUID umbra_express$getTarget() {
		throw new UnsupportedOperationException("Duck interface");
	}

	default void umbra_express$addKilledTarget() {
		throw new UnsupportedOperationException("Duck interface");
	}

	default List<UUID> umbra_express$getKilledTargets() {
		throw new UnsupportedOperationException("Duck interface");
	}

	default void umbra_express$reset() {
		throw new UnsupportedOperationException("Duck interface");
	}

	/**
	 * avoids having to suppress the warning for every cast
	 * @param game the game world instance to cast
	 * @return the hit list component
	 */
    static HitListWorldComponent cast(GameWorldComponent game) {
		assert game instanceof HitListWorldComponent;
		return (HitListWorldComponent) game;
	}
}
