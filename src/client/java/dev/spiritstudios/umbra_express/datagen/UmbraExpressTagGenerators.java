package dev.spiritstudios.umbra_express.datagen;

import dev.doctor4t.trainmurdermystery.index.tag.TMMItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class UmbraExpressTagGenerators {

	public static void addAll(FabricDataGenerator.Pack pack) {
		pack.addProvider(ItemTagProvider::new);
	}

	public static class ItemTagProvider extends FabricTagProvider.ItemTagProvider {
		@SuppressWarnings("unused")
		public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, BlockTagProvider blockTagProvider) {
			super(output, completableFuture, blockTagProvider);
		}

		public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
			getOrCreateTagBuilder(TMMItemTags.PSYCHOSIS_ITEMS).addOptional(Identifier.of("tmm-construct", "thetiscope"));
		}
	}
}
