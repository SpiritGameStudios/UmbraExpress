package dev.spiritstudios.umbra_express.block.entity;

import dev.spiritstudios.umbra_express.init.UmbraExpressBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class BroadcastButtonBlockEntity extends BlockEntity {

    public BroadcastButtonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public BroadcastButtonBlockEntity(BlockPos pos, BlockState state) {
        this(UmbraExpressBlockEntities.BROADCAST_BUTTON, pos, state);
    }
}
