package dev.spiritstudios.umbra_express.event;

import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"CodeBlock2Expr", "unused"})
public class TMMPlayerEvents {

    public static final Event<Initializing> INITIALIZING = EventFactory.createArrayBacked(Initializing.class, callbacks -> {
        return (serverWorld, serverPlayer, role, playing, game) -> {
            for (Initializing callback : callbacks)
                callback.onInitializing(serverWorld, serverPlayer, role, playing, game);
        };
    });

    public static final Event<Tick> TICK = EventFactory.createArrayBacked(Tick.class, callbacks -> {
        return (serverWorld, serverPlayer, role, playing, game) -> {
            for (Tick callback : callbacks)
                callback.onTick(serverWorld, serverPlayer, role, playing, game);
        };
    });

    public static final Event<Died> DIED = EventFactory.createArrayBacked(Died.class, callbacks -> {
        return (world, player, playerRole, killer, killerRole, deathReason, game) -> {
            for (Died callback : callbacks)
                callback.onDied(world, player, playerRole, killer, killerRole, deathReason, game);
        };
    });

    @FunctionalInterface
    public interface Died {
        void onDied(World world, PlayerEntity player, Role playerRole, @Nullable PlayerEntity killer, @Nullable Role killerRole, Identifier deathReason, GameWorldComponent game);
    }

    @FunctionalInterface
    public interface Initializing {
        void onInitializing(ServerWorld serverWorld, ServerPlayerEntity serverPlayer, Role role, boolean playing, GameWorldComponent game);
    }

    @FunctionalInterface
    public interface Tick {
        void onTick(ServerWorld serverWorld, ServerPlayerEntity serverPlayer, Role role, boolean playing, GameWorldComponent game);
    }

}
