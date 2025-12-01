package dev.spiritstudios.umbra_express.duck;

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
}
