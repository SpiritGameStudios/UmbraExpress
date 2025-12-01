package dev.spiritstudios.umbra_express.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class UmbraExpressDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(UmbraExpressEnUsLangGenerator::new);
        pack.addProvider(UmbraExpressModelGenerator::new);
		UmbraExpressTagGenerators.addAll(pack);
	}
}
