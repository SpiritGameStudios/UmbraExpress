package dev.spiritstudios.umbra_express.datagen;

import dev.doctor4t.trainmurdermystery.index.TMMBlocks;
import dev.doctor4t.trainmurdermystery.index.tag.TMMItemTags;
import dev.spiritstudios.umbra_express.init.UmbraExpressItems;
import dev.spiritstudios.umbra_express.init.UmbraExpressTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class UmbraExpressTagGenerators {

	public static void addAll(FabricDataGenerator.Pack pack) {
		BlockTagProvider blockTagProvider = pack.addProvider(BlockTagProvider::new);
		pack.addProvider((output, registriesFuture) -> new ItemTagProvider(output, registriesFuture, blockTagProvider));
	}

	public static class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
		public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, BlockTagProvider blockTagProvider) {
			super(output, completableFuture, blockTagProvider);
		}

		@SuppressWarnings("unused")
		public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			getOrCreateTagBuilder(TMMItemTags.PSYCHOSIS_ITEMS)
				.add(UmbraExpressItems.ANTIDOTE)
				.addOptional(Identifier.of("tmm-construct", "thetiscope"));
		}
	}

	public static class BlockTagProvider extends FabricTagProvider.BlockTagProvider {

		public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			getOrCreateTagBuilder(UmbraExpressTags.HAUNTING_INTERACTABLE)
				.add(TMMBlocks.TRIMMED_LANTERN)
				.add(TMMBlocks.WALL_LAMP);
		}
	}
}
