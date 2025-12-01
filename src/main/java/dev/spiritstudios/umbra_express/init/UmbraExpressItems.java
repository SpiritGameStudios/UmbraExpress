package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.ratatouille.util.registrar.ItemRegistrar;
import dev.spiritstudios.umbra_express.UmbraExpress;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("unchecked")
@ApiStatus.NonExtendable
public interface UmbraExpressItems {

	ItemRegistrar REGISTRAR = new ItemRegistrar(UmbraExpress.MOD_ID);

	RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, UmbraExpress.id("item_group"));

	Item MASTER_KEY = REGISTRAR.create("master_key", new Item(new Item.Settings().maxCount(1)), ITEM_GROUP);

	static void init() {
        REGISTRAR.registerEntries();

		Registry.register(Registries.ITEM_GROUP, ITEM_GROUP,
			FabricItemGroup.builder()
				.displayName(Text.translatable("itemGroup.umbra_express.group"))
				.icon(
					() -> UmbraExpressBlocks.BROADCAST_BUTTON.asItem().getDefaultStack()
				)
				.build()
		);
    }
}
