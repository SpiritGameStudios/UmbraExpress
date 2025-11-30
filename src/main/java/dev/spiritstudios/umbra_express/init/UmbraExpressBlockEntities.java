package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.ratatouille.util.registrar.BlockEntityTypeRegistrar;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.block.entity.BroadcastButtonBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface UmbraExpressBlockEntities {

    BlockEntityTypeRegistrar REGISTRAR = new BlockEntityTypeRegistrar(UmbraExpress.MOD_ID);

    BlockEntityType<BroadcastButtonBlockEntity> BROADCAST_BUTTON = REGISTRAR.create("broadcast_button", BlockEntityType.Builder.create(BroadcastButtonBlockEntity::new, UmbraExpressBlocks.BROADCAST_BUTTON));

    static void init() {
        REGISTRAR.registerEntries();
    }
}
