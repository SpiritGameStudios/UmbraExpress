package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.ratatouille.util.registrar.BlockEntityTypeRegistrar;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.block.entity.BroadcastButtonBlockEntity;
import dev.spiritstudios.umbra_express.block.entity.CrystalBallBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface UmbraExpressBlockEntities {

    BlockEntityTypeRegistrar REGISTRAR = new BlockEntityTypeRegistrar(UmbraExpress.MOD_ID);

    BlockEntityType<BroadcastButtonBlockEntity> BROADCAST_BUTTON = REGISTRAR.create("broadcast_button", BlockEntityType.Builder.create(BroadcastButtonBlockEntity::new, UmbraExpressBlocks.BROADCAST_BUTTON));
    BlockEntityType<CrystalBallBlockEntity> CRYSTAL_BALL = REGISTRAR.create("crystal_ball", BlockEntityType.Builder.create(CrystalBallBlockEntity::new, UmbraExpressBlocks.CRYSTAL_BALL));

    static void init() {
        REGISTRAR.registerEntries();
    }
}
