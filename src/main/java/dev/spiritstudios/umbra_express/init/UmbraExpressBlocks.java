package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.ratatouille.util.registrar.BlockRegistrar;
import dev.doctor4t.trainmurdermystery.index.TMMBlocks;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.block.BroadcastButtonBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("unchecked")
@ApiStatus.NonExtendable
public interface UmbraExpressBlocks {

    BlockRegistrar REGISTRAR = new BlockRegistrar(UmbraExpress.MOD_ID);

    Block BROADCAST_BUTTON = REGISTRAR.createWithItem("broadcast_button", new BroadcastButtonBlock(AbstractBlock.Settings.copy(TMMBlocks.ELEVATOR_BUTTON)), TMMItems.DECORATION_GROUP);

    static void init() {
        REGISTRAR.registerEntries();
    }
}
