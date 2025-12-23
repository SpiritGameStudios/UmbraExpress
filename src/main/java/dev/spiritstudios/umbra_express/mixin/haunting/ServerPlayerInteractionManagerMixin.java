package dev.spiritstudios.umbra_express.mixin.haunting;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.umbra_express.UmbraExpress;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Definition(id = "gameMode", field = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;gameMode:Lnet/minecraft/world/GameMode;")
    @Definition(id = "SPECTATOR", field = "Lnet/minecraft/world/GameMode;SPECTATOR:Lnet/minecraft/world/GameMode;")
    @Expression("this.gameMode == SPECTATOR")
    @ModifyExpressionValue(method = "interactBlock", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean bypassSpectatorCheck(boolean original, @Local(argsOnly = true) BlockHitResult hitResult, @Local(argsOnly = true) World world) {
        if (!original) {
            return false;
        }

        BlockPos blockPos = hitResult.getBlockPos();
        if (blockPos == null) {
            return true;
        }
        if (world == null) {
            return true;
        }

        return !UmbraExpress.canBeUsedByGhost(world.getBlockState(blockPos));
    }
}
