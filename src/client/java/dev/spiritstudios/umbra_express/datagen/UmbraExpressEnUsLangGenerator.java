package dev.spiritstudios.umbra_express.datagen;

import dev.spiritstudios.umbra_express.init.UmbraExpressBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class UmbraExpressEnUsLangGenerator extends FabricLanguageProvider {

    public UmbraExpressEnUsLangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add("announcement.role.conductor", "Conductor!");
        translationBuilder.add("announcement.title.conductor", "Conductors");
        translationBuilder.add("announcement.goal.conductor", "Stay safe and survive till the end of the ride.");
        translationBuilder.add("announcement.goals.conductor", "Stay safe and survive till the end of the ride.");
        translationBuilder.add("announcement.win.conductor", "Passengers Win!");
        translationBuilder.add("task.broadcast", "broadcasting.");

        UmbraExpressBlocks.REGISTRAR.generateLang(wrapperLookup, translationBuilder);
    }
}
