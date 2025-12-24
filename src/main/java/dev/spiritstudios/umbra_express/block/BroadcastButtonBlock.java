package dev.spiritstudios.umbra_express.block;

import dev.doctor4t.trainmurdermystery.block.ElevatorButtonBlock;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.spiritstudios.umbra_express.block.entity.BroadcastButtonBlockEntity;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import dev.spiritstudios.umbra_express.duck.ConductorWorldComponent;
import dev.spiritstudios.umbra_express.init.UmbraExpressBlockEntities;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import dev.spiritstudios.umbra_express.voicechat.ConductorVoicechatPlugin;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BroadcastButtonBlock extends ElevatorButtonBlock implements BlockEntityProvider {

    public BroadcastButtonBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BroadcastButtonBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        boolean client = world.isClient;

        if (state.get(POWERED)) {
            // if powered, deactivate announcements
            if (!client) {
                BroadcastWorldComponent broadcast = BroadcastWorldComponent.KEY.get(world);
                broadcast.setBroadcasting(false);
                if (broadcast.getAnnouncerUuid() != null) {
                    ConductorVoicechatPlugin.reset();
                    broadcast.setAnnouncerUuid(null);
                }
                world.scheduleBlockTick(pos, this, 1);
            }
            return ActionResult.success(client);
        }

        if (!client && player instanceof ServerPlayerEntity serverPlayer) {
            GameWorldComponent game = GameWorldComponent.KEY.get(world);
            if (game.isRunning()) {
                if (ConductorWorldComponent.cast(game).umbra_express$isConductor(serverPlayer)) {
                    BroadcastWorldComponent.KEY.get(world).setBroadcasting(true);
                }
            } else {
                ConductorVoicechatPlugin.vanillaBroadcast(serverPlayer);
            }
        }

        this.powerOn(state, world, pos, player);

        return ActionResult.success(client);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (type != UmbraExpressBlockEntities.BROADCAST_BUTTON || world.isClient()) {
            return null;
        }
        return ((world1, pos, state1, blockEntity) -> {
            if (BroadcastWorldComponent.KEY.get(world1).isOnCooldown() && state1.get(POWERED)) {
                world1.scheduleBlockTick(pos, this, 1);
            }
        });
    }
}
