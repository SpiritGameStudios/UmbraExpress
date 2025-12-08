package dev.spiritstudios.umbra_express.datagen;

import dev.doctor4t.trainmurdermystery.datagen.TMMModelGen;
import dev.spiritstudios.umbra_express.init.UmbraExpressBlocks;
import dev.spiritstudios.umbra_express.init.UmbraExpressItems;
import dev.spiritstudios.umbra_express.mixin.roles.conductor.TMMModelGenAccessor;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class UmbraExpressModelGenerator extends TMMModelGen {

    public UmbraExpressModelGenerator(FabricDataOutput output) {
        super(output);
    }

    @SuppressWarnings("CastToIncompatibleInterface")
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        ((TMMModelGenAccessor) this).umbra_express$invokeRegisterButton(blockStateModelGenerator, UmbraExpressBlocks.BROADCAST_BUTTON);
        blockStateModelGenerator.registerSimpleState(UmbraExpressBlocks.CRYSTAL_BALL);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(UmbraExpressItems.ANTIDOTE, Models.GENERATED);
    }

}
