package dev.spiritstudios.umbra_express.mixin.event;

import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.trainmurdermystery.api.GameMode;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.spiritstudios.umbra_express.event.TMMGameLifecycleEvents;
import dev.spiritstudios.umbra_express.event.TMMPlayerEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = GameFunctions.class, remap = false)
public class GameFunctionsMixin {

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/cca/GameWorldComponent;setGameStatus(Ldev/doctor4t/trainmurdermystery/cca/GameWorldComponent$GameStatus;)V"), remap = false)
    private static void onStart(ServerWorld world, GameMode gameMode, int time, CallbackInfo ci, @Local GameWorldComponent game) {
        TMMGameLifecycleEvents.START.invoker().onStart(world, game);
    }

    @Inject(method = "initializeGame", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/api/GameMode;initializeGame(Lnet/minecraft/server/world/ServerWorld;Ldev/doctor4t/trainmurdermystery/cca/GameWorldComponent;Ljava/util/List;)V"), remap = false)
    private static void onInitializing(ServerWorld serverWorld, CallbackInfo ci, @Local GameWorldComponent gameWorldComponent, @Local List<ServerPlayerEntity> readyPlayerList) {
        TMMGameLifecycleEvents.INITIALIZING.invoker().onInitializing(serverWorld, gameWorldComponent, readyPlayerList);
        TMMPlayerEvents.Initializing invoker = TMMPlayerEvents.INITIALIZING.invoker();

        for (ServerPlayerEntity serverPlayerEntity : serverWorld.getPlayers()) {
            Role role = gameWorldComponent.getRole(serverPlayerEntity);
            boolean playing = readyPlayerList.contains(serverPlayerEntity);

            invoker.onInitializing(serverWorld, serverPlayerEntity, role, playing, gameWorldComponent);
        }
    }

    @Inject(method = "initializeGame", at = @At("RETURN"), remap = false)
    private static void onInitialized(ServerWorld serverWorld, CallbackInfo ci, @Local GameWorldComponent gameWorldComponent) {
        TMMGameLifecycleEvents.INITIALIZED.invoker().onInitialized(serverWorld, gameWorldComponent);
    }

    @Inject(method = "finalizeGame", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/api/GameMode;finalizeGame(Lnet/minecraft/server/world/ServerWorld;Ldev/doctor4t/trainmurdermystery/cca/GameWorldComponent;)V", remap = false))
    private static void onFinalizing(ServerWorld world, CallbackInfo ci, @Local GameWorldComponent gameWorldComponent) {
        TMMGameLifecycleEvents.FINALIZING.invoker().onFinalizing(world, gameWorldComponent);
    }

    @Inject(method = "finalizeGame", at = @At("RETURN"), remap = false)
    private static void onFinalized(ServerWorld world, CallbackInfo ci, @Local GameWorldComponent gameWorldComponent) {
        TMMGameLifecycleEvents.FINALIZED.invoker().onFinalized(world, gameWorldComponent);
    }

    @Inject(method = "stopGame", at = @At("RETURN"), remap = false)
    private static void onStop(ServerWorld world, CallbackInfo ci, @Local GameWorldComponent component) {
        TMMGameLifecycleEvents.STOP.invoker().onStop(world, component);
    }

    @Inject(method = "killPlayer(Lnet/minecraft/entity/player/PlayerEntity;ZLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Identifier;)V", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/cca/GameWorldComponent;isInnocent(Lnet/minecraft/entity/player/PlayerEntity;)Z"), remap = false)
    private static void onPlayerDied(PlayerEntity victim, boolean spawnBody, @Nullable PlayerEntity killer, Identifier deathReason, CallbackInfo ci, @Local GameWorldComponent gameWorldComponent) {
        TMMPlayerEvents.DIED.invoker().onDied(victim.getWorld(), victim, killer, deathReason, gameWorldComponent);
    }
}
