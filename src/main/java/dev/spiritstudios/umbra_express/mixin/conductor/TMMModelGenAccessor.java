package dev.spiritstudios.umbra_express.mixin.conductor;

import dev.doctor4t.trainmurdermystery.datagen.TMMModelGen;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TMMModelGen.class)
public interface TMMModelGenAccessor {

    @Invoker("registerButton")
    void umbra_express$invokeRegisterButton(BlockStateModelGenerator generator, Block block);
}
