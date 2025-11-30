package dev.spiritstudios.umbra_express.datagen;

import dev.doctor4t.trainmurdermystery.datagen.TMMModelGen;
import dev.spiritstudios.umbra_express.init.UmbraExpressBlocks;
import dev.spiritstudios.umbra_express.mixin.roles.conductor.TMMModelGenAccessor;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

public class UmbraExpressModelGenerator extends TMMModelGen {

    public UmbraExpressModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        ((TMMModelGenAccessor) this).umbra_express$invokeRegisterButton(blockStateModelGenerator, UmbraExpressBlocks.BROADCAST_BUTTON);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
