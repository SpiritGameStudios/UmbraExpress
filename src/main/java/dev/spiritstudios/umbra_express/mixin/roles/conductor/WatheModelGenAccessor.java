package dev.spiritstudios.umbra_express.mixin.roles.conductor;

import dev.doctor4t.wathe.datagen.WatheModelGen;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WatheModelGen.class)
public interface WatheModelGenAccessor {

    @Invoker("registerButton")
    void umbra_express$invokeRegisterButton(BlockStateModelGenerator generator, Block block);
}
