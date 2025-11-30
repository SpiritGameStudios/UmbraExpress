package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.ratatouille.util.registrar.ItemRegistrar;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.item.LockpickItem;
import dev.spiritstudios.umbra_express.UmbraExpress;
import net.minecraft.item.Item;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("unchecked")
@ApiStatus.NonExtendable
public interface UmbraExpressItems {

    ItemRegistrar REGISTRAR = new ItemRegistrar(UmbraExpress.MOD_ID);

	Item MASTER_KEY = REGISTRAR.create("master_key", new Item(new Item.Settings().maxCount(1)), TMMItems.EQUIPMENT_GROUP);

	static void init() {
        REGISTRAR.registerEntries();
    }
}
