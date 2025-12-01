package dev.spiritstudios.umbra_express.block;

import com.mojang.serialization.MapCodec;
import dev.doctor4t.trainmurdermystery.cca.AreasWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerMoodComponent;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.spiritstudios.umbra_express.block.entity.CrystalBallBlockEntity;
import dev.spiritstudios.umbra_express.init.UmbraExpressBlockEntities;
import dev.spiritstudios.umbra_express.init.UmbraExpressParticles;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author axialeaa
 */
public class CrystalBallBlock extends BlockWithEntity {

    private static final MapCodec<CrystalBallBlock> CODEC = createCodec(CrystalBallBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

    private static final VoxelShape BASE_SHAPE = Block.createCuboidShape(2, 0, 2, 14, 3, 14);
    private static final VoxelShape BALL_SHAPE = Block.createCuboidShape(3, 3, 3, 13, 13, 13);

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
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity mystic, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof CrystalBallBlockEntity blockEntity))
            return ActionResult.PASS;

        GameWorldComponent game = GameWorldComponent.KEY.get(world);
        boolean gameRunning = game.isRunning();

        if (gameRunning && !game.getRole(mystic).equals(UmbraExpressRoles.MYSTIC))
            return ActionResult.PASS;

        if (blockEntity.isCoolingDown()) {
            blockEntity.sendCooldownMessage(mystic);
            return ActionResult.CONSUME;
        }

        Random random = world.getRandom();
        chooseNewApparitionPlayer(game, world, mystic, random).ifPresent(apparition -> blockEntity.onReveal(world, pos, random, apparition, mystic, gameRunning));

        return ActionResult.success(world.isClient());
    }

    private static Optional<? extends PlayerEntity> chooseNewApparitionPlayer(GameWorldComponent game, World world, PlayerEntity mystic, Random random) {
        Stream<? extends PlayerEntity> playerStream = world.getPlayers().stream();

        if (game.isRunning())
            playerStream = playerStream.filter(player -> isPlayerRevealable(world, player, mystic, game));

        List<? extends PlayerEntity> players = playerStream.toList();

        return Util.getRandomOrEmpty(players, random);
    }

    private static boolean isPlayerRevealable(World world, PlayerEntity player, PlayerEntity mystic, GameWorldComponent game) {
        if (player.equals(mystic) || GameFunctions.isPlayerEliminated(player))
            return false;

        AreasWorldComponent areas = AreasWorldComponent.KEY.get(world);

        if (!areas.getPlayArea().contains(player.getPos()))
            return false;

        return game.getRole(player).isInnocent() || hasLowMood(mystic);
    }

    private static boolean hasLowMood(PlayerEntity player) {
        return PlayerMoodComponent.KEY.get(player).isLowerThanMid();
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!(world.getBlockEntity(pos) instanceof CrystalBallBlockEntity blockEntity) || !blockEntity.isRevealingApparition())
            return;

        Box ball = BALL_SHAPE.getBoundingBox().offset(pos).contract(0.6F);

        for (int i = 0; i < random.nextBetween(1, 3); i++) {
            double x = MathHelper.nextBetween(random, (float) ball.getMin(Direction.Axis.X), (float) ball.getMax(Direction.Axis.X));
            double y = MathHelper.nextBetween(random, (float) ball.getMin(Direction.Axis.Y), (float) ball.getMax(Direction.Axis.Y));
            double z = MathHelper.nextBetween(random, (float) ball.getMin(Direction.Axis.Z), (float) ball.getMax(Direction.Axis.Z));

            world.addParticle(UmbraExpressParticles.CRYSTAL_BALL_SPARKLE, true, x, y, z, 0, -0.001, 0);
        }
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
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World ignoredWorld, BlockState ignoredState, BlockEntityType<T> type) {
        return validateTicker(type, UmbraExpressBlockEntities.CRYSTAL_BALL, (world, pos, state, blockEntity) -> blockEntity.tick(world));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

}
