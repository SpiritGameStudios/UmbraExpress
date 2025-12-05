package dev.spiritstudios.umbra_express.mixin.shop;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.game.MurderGameMode;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import dev.spiritstudios.umbra_express.role.MoneyMaker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(value = MurderGameMode.class, remap = false)
public class MurderGameModeMixin {

    @ModifyExpressionValue(method = "tickServerGameLoop", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/cca/GameWorldComponent;canUseKillerFeatures(Lnet/minecraft/entity/player/PlayerEntity;)Z", ordinal = 0), remap = false)
    private boolean shouldAddToBalance(boolean original, ServerWorld serverWorld, GameWorldComponent gameWorldComponent, @Local ServerPlayerEntity player, @Share("moneyMaker") LocalRef<MoneyMaker> localRef) {
        MoneyMaker moneyMaker = UmbraExpressRoles.MONEY_MAKERS.get(gameWorldComponent.getRole(player));
        localRef.set(moneyMaker);

        return moneyMaker != null;
    }

    @ModifyExpressionValue(method = "tickServerGameLoop", at = @At(value = "FIELD", target = "Ldev/doctor4t/trainmurdermystery/game/GameConstants;PASSIVE_MONEY_TICKER:Ljava/util/function/Function;", remap = false, opcode = Opcodes.GETSTATIC), remap = false)
    private Function<Long, Integer> getPassiveMoneyTicker(Function<Long, Integer> original, @Local ServerPlayerEntity player, @Local(argsOnly = true) GameWorldComponent game, @Share("moneyMaker") LocalRef<MoneyMaker> localRef) {
        return localRef.get().passiveTicker();
    }

}
