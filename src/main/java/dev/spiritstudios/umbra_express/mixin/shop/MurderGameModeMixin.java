package dev.spiritstudios.umbra_express.mixin.shop;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.game.MurderGameMode;
import dev.spiritstudios.umbra_express.role.MoneyManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = MurderGameMode.class, remap = false)
public class MurderGameModeMixin {

    @Unique private long lastInitializedTicks = 0;

    @Inject(method = "initializeGame", at = @At("RETURN"), remap = false)
    private void setLastInitializedTicks(ServerWorld serverWorld, GameWorldComponent gameWorldComponent, List<ServerPlayerEntity> players, CallbackInfo ci) {
        this.lastInitializedTicks = serverWorld.getTime();
    }

    @ModifyExpressionValue(method = "tickServerGameLoop", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/cca/GameWorldComponent;canUseKillerFeatures(Lnet/minecraft/entity/player/PlayerEntity;)Z", remap = false, ordinal = 0), remap = false)
    private boolean shouldAddToBalance(boolean original, ServerWorld serverWorld, GameWorldComponent gameWorldComponent, @Local(name = "player") ServerPlayerEntity player, @Share("moneyManager") LocalRef<MoneyManager> localRef) {
        MoneyManager moneyManager = MoneyManager.ROLE_MAP.get(gameWorldComponent.getRole(player));
        localRef.set(moneyManager);

        return moneyManager != null;
    }

    @ModifyExpressionValue(method = "tickServerGameLoop", at = @At(value = "INVOKE", target = "Ljava/util/function/Function;apply(Ljava/lang/Object;)Ljava/lang/Object;"), remap = false)
    private Object getMoney(Object original, ServerWorld serverWorld, GameWorldComponent gameWorldComponent, @Share("moneyManager") LocalRef<MoneyManager> localRef) {
        return localRef.get().passiveTicker().apply(serverWorld.getTime() + this.lastInitializedTicks);
    }

}
