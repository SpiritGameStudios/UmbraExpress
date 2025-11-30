package dev.spiritstudios.umbra_express.mixin.roles.conductor;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.spiritstudios.umbra_express.block.BroadcastButtonBlock;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import net.minecraft.block.Block;
import net.minecraft.block.ButtonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ButtonBlock.class)
public class ButtonBlockMixin {

    @WrapWithCondition(method = {"powerOn", "tryPowerWithProjectiles"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;scheduleBlockTick(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"))
    private boolean noSchedule(World instance, BlockPos blockPos, Block block, int delay) {
        if (block instanceof BroadcastButtonBlock) {
            return BroadcastWorldComponent.KEY.get(instance).isOnCooldown();
        }
        return true;
    }
}
