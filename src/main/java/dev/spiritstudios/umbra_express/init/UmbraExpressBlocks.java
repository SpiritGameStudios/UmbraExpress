package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.ratatouille.util.registrar.BlockRegistrar;
import dev.doctor4t.wathe.index.WatheBlocks;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.block.BroadcastButtonBlock;
import dev.spiritstudios.umbra_express.block.CrystalBallBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("unchecked")
@ApiStatus.NonExtendable
public interface UmbraExpressBlocks {

    BlockRegistrar REGISTRAR = new BlockRegistrar(UmbraExpress.MOD_ID);

    Block BROADCAST_BUTTON = REGISTRAR.createWithItem("broadcast_button", new BroadcastButtonBlock(AbstractBlock.Settings.copy(WatheBlocks.ELEVATOR_BUTTON)), UmbraExpressItems.ITEM_GROUP);
    Block CRYSTAL_BALL = REGISTRAR.createWithItem("crystal_ball", new CrystalBallBlock(AbstractBlock.Settings.create()
        .sounds(BlockSoundGroup.AMETHYST_CLUSTER)
        .mapColor(MapColor.PALE_PURPLE)
        .nonOpaque()
        .luminance(state -> 7)
        .strength(0.3F)
    ), UmbraExpressItems.ITEM_GROUP);

    static void init() {
        REGISTRAR.registerEntries();
    }
}
