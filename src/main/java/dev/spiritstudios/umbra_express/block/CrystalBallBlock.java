package dev.spiritstudios.umbra_express.block;

import com.mojang.serialization.MapCodec;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import dev.spiritstudios.umbra_express.block.entity.CrystalBallBlockEntity;
import dev.spiritstudios.umbra_express.init.UmbraExpressBlockEntities;
import dev.spiritstudios.umbra_express.init.UmbraExpressSoundEvents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class CrystalBallBlock extends BlockWithEntity {

    public static final VoxelShape BASE_SHAPE = Block.createCuboidShape(2, 0, 2, 14, 3, 14);
    public static final VoxelShape BALL_SHAPE = Block.createCuboidShape(3, 3, 3, 13, 13, 13);

    public static final MapCodec<CrystalBallBlock> CODEC = createCodec(CrystalBallBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

    public CrystalBallBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.union(BASE_SHAPE, BALL_SHAPE);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof CrystalBallBlockEntity blockEntity))
            return ActionResult.PASS;

        if (blockEntity.apparitionTickCountdownTime > 0)
            return ActionResult.CONSUME;

        if (!world.isClient()) {
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), UmbraExpressSoundEvents.CRYSTAL_BALL_REVEAL, SoundCategory.BLOCKS, 2.0F, 1.0F);
        }

        GameWorldComponent game = GameWorldComponent.KEY.get(world);
        Stream<? extends PlayerEntity> playerStream = world.getPlayers().stream();

        if (game.isRunning())
            playerStream = playerStream.filter(playerEntity -> game.getRole(playerEntity).isInnocent());

        blockEntity.apparitionTickCountdownTime = CrystalBallBlockEntity.MAX_APPARITION_TICKS;
        blockEntity.player = Util.getRandom(playerStream.toList(), world.getRandom());

        blockEntity.markDirty();

        return ActionResult.success(world.isClient());
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrystalBallBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World ignored, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, UmbraExpressBlockEntities.CRYSTAL_BALL, (world1, pos, blockState, blockEntity) -> blockEntity.decrementApparitionCountdown());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

}
