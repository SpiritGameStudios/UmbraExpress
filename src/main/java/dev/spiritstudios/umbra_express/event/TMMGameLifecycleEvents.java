package dev.spiritstudios.umbra_express.event;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

@SuppressWarnings({"CodeBlock2Expr", "unused"})
public class TMMGameLifecycleEvents {

    public static final Event<Start> START = EventFactory.createArrayBacked(Start.class, callbacks -> {
        return (serverWorld, game) -> {
            for (Start callback : callbacks)
                callback.onStart(serverWorld, game);
        };
    });

    public static final Event<Initializing> INITIALIZING = EventFactory.createArrayBacked(Initializing.class, callbacks -> {
        return (serverWorld, game, readyPlayerList) -> {
            for (Initializing callback : callbacks)
                callback.onInitializing(serverWorld, game, readyPlayerList);
        };
    });

    public static final Event<Initialized> INITIALIZED = EventFactory.createArrayBacked(Initialized.class, callbacks -> {
        return (serverWorld, game) -> {
            for (Initialized callback : callbacks)
                callback.onInitialized(serverWorld, game);
        };
    });

    public static final Event<Tick> TICK = EventFactory.createArrayBacked(Tick.class, callbacks -> {
        return (serverWorld, game) -> {
            for (Tick callback : callbacks)
                callback.onTick(serverWorld, game);
        };
    });

    public static final Event<Finalizing> FINALIZING = EventFactory.createArrayBacked(Finalizing.class, callbacks -> {
        return (serverWorld, game) -> {
            for (Finalizing callback : callbacks)
                callback.onFinalizing(serverWorld, game);
        };
    });

    public static final Event<Finalized> FINALIZED = EventFactory.createArrayBacked(Finalized.class, callbacks -> {
        return (serverWorld, game) -> {
            for (Finalized callback : callbacks)
                callback.onFinalized(serverWorld, game);
        };
    });

    public static final Event<Stop> STOP = EventFactory.createArrayBacked(Stop.class, callbacks -> {
        return (serverWorld, game) -> {
            for (Stop callback : callbacks)
                callback.onStop(serverWorld, game);
        };
    });

    @FunctionalInterface
    public interface Start {
        void onStart(ServerWorld serverWorld, GameWorldComponent game);
    }

    @FunctionalInterface
    public interface Initializing {
        void onInitializing(ServerWorld serverWorld, GameWorldComponent game, List<ServerPlayerEntity> readyPlayerList);
    }

    @FunctionalInterface
    public interface Initialized {
        void onInitialized(ServerWorld serverWorld, GameWorldComponent game);
    }

    @FunctionalInterface
    public interface Finalizing {
        void onFinalizing(ServerWorld serverWorld, GameWorldComponent game);
    }

    @FunctionalInterface
    public interface Finalized {
        void onFinalized(ServerWorld serverWorld, GameWorldComponent game);
    }

    @FunctionalInterface
    public interface Tick {
        void onTick(ServerWorld serverWorld, GameWorldComponent game);
    }

    @FunctionalInterface
    public interface Stop {
        void onStop(ServerWorld serverWorld, GameWorldComponent game);
    }

}
