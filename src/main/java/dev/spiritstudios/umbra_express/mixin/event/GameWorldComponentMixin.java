package dev.spiritstudios.umbra_express.mixin.event;

import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.spiritstudios.umbra_express.event.TMMGameLifecycleEvents;
import dev.spiritstudios.umbra_express.event.TMMPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameWorldComponent.class, remap = false)
public class GameWorldComponentMixin {

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Ldev/doctor4t/wathe/api/GameMode;tickServerGameLoop(Lnet/minecraft/server/world/ServerWorld;Ldev/doctor4t/wathe/cca/GameWorldComponent;)V"), remap = false)
    private void onTick(CallbackInfo ci, @Local(name = "serverWorld") ServerWorld serverWorld) {
        TMMGameLifecycleEvents.TICK.invoker().onTick(serverWorld, (GameWorldComponent) (Object) this);
    }

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Ldev/doctor4t/wathe/game/GameFunctions;isPlayerAliveAndSurvival(Lnet/minecraft/entity/player/PlayerEntity;)Z", ordinal = 2), remap = false)
    private void onPlayerTick(CallbackInfo ci, @Local(name = "serverWorld") ServerWorld serverWorld, @Local(name = "player") ServerPlayerEntity player) {
        GameWorldComponent game = GameWorldComponent.KEY.get(serverWorld);
        TMMPlayerEvents.TICK.invoker().onTick(serverWorld, player, game.getRole(player), GameFunctions.isPlayerAliveAndSurvival(player), game);
    }

}
