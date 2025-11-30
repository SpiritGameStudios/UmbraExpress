package dev.spiritstudios.umbra_express.datagen;

import dev.doctor4t.ratatouille.util.TextUtils;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.spiritstudios.umbra_express.init.UmbraExpressBlocks;
import dev.spiritstudios.umbra_express.init.UmbraExpressItems;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class UmbraExpressEnUsLangGenerator extends FabricLanguageProvider {

	public static final String INNOCENT_GOAL = "Stay safe and survive till the end of the ride.";
	public static final String PASSENGERS_WIN = "Passengers Win!";
	public static final String KILLERS_WIN = "Killers Win!";
	public static final String KILLER_GOAL = "Eliminate a passenger to succeed, before time runs out.";
	public static final String KILLER_GOALS = "Eliminate all civilians before time runs out.";

	public UmbraExpressEnUsLangGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
		generateForInnocent(UmbraExpressRoles.CONDUCTOR, translationBuilder);
		generateForInnocent(UmbraExpressRoles.BARTENDER, translationBuilder);
		generateForInnocent(UmbraExpressRoles.LOCKSMITH, translationBuilder);

		generateForKiller(UmbraExpressRoles.ASSASSIN, translationBuilder);

		translationBuilder.add("task.broadcast", "broadcasting.");

        UmbraExpressBlocks.REGISTRAR.generateLang(wrapperLookup, translationBuilder);
		//UmbraExpressItems.REGISTRAR.generateLang(wrapperLookup, translationBuilder);

		String masterKeyTranslation = TextUtils.getItemTranslationKey(UmbraExpressItems.MASTER_KEY);
		translationBuilder.add(masterKeyTranslation, "Locksmith's Lockpick");
		translationBuilder.add(masterKeyTranslation + ".tooltip", "Use on any locked door to open it (no cooldown)");
	}

	public static void generateForInnocent(Role role, TranslationBuilder translationBuilder) {
		generate(role.identifier().getPath(), translationBuilder, INNOCENT_GOAL, INNOCENT_GOAL, PASSENGERS_WIN);
	}

	public static void generateForKiller(Role role, TranslationBuilder translationBuilder) {
		generate(role.identifier().getPath(), translationBuilder, KILLER_GOAL, KILLER_GOALS, KILLERS_WIN);
	}

	public static void generate(String role, TranslationBuilder translationBuilder, String goal, String goals, String win) {
		role = role.toLowerCase(Locale.ROOT);
		String roleTitle = role.substring(0, 1).toUpperCase(Locale.ROOT) + role.substring(1);
		translationBuilder.add("announcement.role." + role, roleTitle + "!");
		translationBuilder.add("announcement.title." + role, roleTitle + "s");
		translationBuilder.add("announcement.goal." + role, goal);
		translationBuilder.add("announcement.goals." + role, goals);
		translationBuilder.add("announcement.win." + role, win);
	}
}
