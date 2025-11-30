package dev.spiritstudios.umbra_express.block.entity;

import dev.spiritstudios.umbra_express.init.UmbraExpressBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class CrystalBallBlockEntity extends BlockEntity {

    private static final String PLAYER_KEY = "player";
    public static final int MAX_APPARITION_TICKS = 140;
    public PlayerEntity player;
    public int apparitionTickCountdownTime;

    public CrystalBallBlockEntity(BlockPos pos, BlockState state) {
        this(UmbraExpressBlockEntities.CRYSTAL_BALL, pos, state);
    }

    public CrystalBallBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (this.player != null)
            nbt.putUuid(PLAYER_KEY, this.player.getUuid());
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (nbt.contains(PLAYER_KEY) && this.getWorld() != null)
            this.player = this.getWorld().getPlayerByUuid(nbt.getUuid(PLAYER_KEY));
    }

    public void decrementApparitionCountdown() {
        if (this.apparitionTickCountdownTime > 0) {
            this.apparitionTickCountdownTime--;
            this.markDirty();
        }
    }

}
