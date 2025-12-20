package dev.spiritstudios.umbra_express.event;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.NonExtendable
public interface TMMGameLifecycleEvents {

    Event<Start> START = EventFactory.createArrayBacked(Start.class,
		callbacks -> (serverWorld, game) -> {
			for (Start callback : callbacks) {
				callback.onStart(serverWorld, game);
			}
		}
	);

    Event<Initializing> INITIALIZING = EventFactory.createArrayBacked(Initializing.class,
		callbacks -> (serverWorld, game, readyPlayerList) -> {
			for (Initializing callback : callbacks) {
				callback.onInitializing(serverWorld, game, readyPlayerList);
			}
		}
	);

    Event<Initialized> INITIALIZED = EventFactory.createArrayBacked(Initialized.class,
		callbacks -> (serverWorld, game) -> {
			for (Initialized callback : callbacks) {
				callback.onInitialized(serverWorld, game);
			}
		}
	);

    Event<Tick> TICK = EventFactory.createArrayBacked(Tick.class,
		callbacks -> (serverWorld, game) -> {
			for (Tick callback : callbacks) {
				callback.onTick(serverWorld, game);
			}
		}
	);

    Event<Finalizing> FINALIZING = EventFactory.createArrayBacked(Finalizing.class,
		callbacks -> (serverWorld, game) -> {
			for (Finalizing callback : callbacks) {
				callback.onFinalizing(serverWorld, game);
			}
		}
	);

    Event<Finalized> FINALIZED = EventFactory.createArrayBacked(Finalized.class,
		callbacks -> (serverWorld, game) -> {
			for (Finalized callback : callbacks) {
				callback.onFinalized(serverWorld, game);
			}
		}
	);

    Event<Stop> STOP = EventFactory.createArrayBacked(Stop.class,
		callbacks -> (serverWorld, game) -> {
			for (Stop callback : callbacks) {
				callback.onStop(serverWorld, game);
			}
		}
	);

    @FunctionalInterface
    interface Start {
        void onStart(ServerWorld serverWorld, GameWorldComponent game);
    }

    @FunctionalInterface
    interface Initializing {
        void onInitializing(ServerWorld serverWorld, GameWorldComponent game, List<ServerPlayerEntity> readyPlayerList);
    }

    @FunctionalInterface
    interface Initialized {
        void onInitialized(ServerWorld serverWorld, GameWorldComponent game);
    }

    @FunctionalInterface
    interface Finalizing {
        void onFinalizing(ServerWorld serverWorld, GameWorldComponent game);
    }

    @FunctionalInterface
    interface Finalized {
        void onFinalized(ServerWorld serverWorld, GameWorldComponent game);
    }

    @FunctionalInterface
    interface Tick {
        void onTick(ServerWorld serverWorld, GameWorldComponent game);
    }

    @FunctionalInterface
    interface Stop {
        void onStop(ServerWorld serverWorld, GameWorldComponent game);
    }
}
