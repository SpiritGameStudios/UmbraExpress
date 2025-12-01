package dev.spiritstudios.umbra_express.datagen;

import dev.doctor4t.ratatouille.util.TextUtils;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.spiritstudios.umbra_express.init.UmbraExpressBlocks;
import dev.spiritstudios.umbra_express.init.UmbraExpressGameRules;
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

		translationBuilder.add("itemGroup.umbra_express.group", "Umbra Express");

		String masterKeyTranslation = TextUtils.getItemTranslationKey(UmbraExpressItems.MASTER_KEY);
		translationBuilder.add(masterKeyTranslation, "Locksmith's Lockpick");
		translationBuilder.add(masterKeyTranslation + ".tooltip", "Use on any locked door to open it (no cooldown)");

		String letterHitList = TextUtils.getItemTranslationKey(TMMItems.LETTER) + ".hitlist";
		translationBuilder.add(letterHitList + ".premise0", "");
		translationBuilder.add(letterHitList + ".premise1", "==== ASSASSIN INSTRUCTIONS ==== ");
		translationBuilder.add(letterHitList + ".premise2", "");
		// Translations credit - old TMM hitman letter instructions
		translationBuilder.add(letterHitList + ".premise3", "Thank you for taking this job. Please eliminate the following targets:");
		translationBuilder.add(letterHitList + ".premise4", "Please do so with the utmost discretion and do not get caught. Good luck.");
		translationBuilder.add(letterHitList + ".premise5", "");
		translationBuilder.add(letterHitList + ".premise6", "P.S.: Don't forget to use your instinct [%s] and use the train's exterior to relocate.");
		translationBuilder.add(letterHitList + ".target", "- %s");
		// end credit
		translationBuilder.add(letterHitList + ".absent", "Thank you for eliminating the targets. We have no further requests at this moment.");

		translationBuilder.add("gamerule.category.umbra_express", "Umbra Express");
		translationBuilder.add(UmbraExpressGameRules.MAX_BROADCAST_TICKS.getTranslationKey(), "Max Broadcast Ticks");
		translationBuilder.add(UmbraExpressGameRules.FORCE_DEVELOPMENT.getTranslationKey(), "Force Development");
		translationBuilder.add(UmbraExpressGameRules.MODIFY_MIN_COUNT.getTranslationKey(), "Modify Min Player Count");
		translationBuilder.add(UmbraExpressGameRules.MIN_PLAYER_COUNT.getTranslationKey(), "Min Player Count");
		translationBuilder.add(UmbraExpressGameRules.MODIFY_KILLER_COUNT.getTranslationKey(), "Modify Killer Count");
		translationBuilder.add(UmbraExpressGameRules.FORCE_KILLER_COUNT.getTranslationKey(), "Forced Killer Count");
		translationBuilder.add("umbra_express.midnightconfig.forceDevRole", "Force Dev Role");
		translationBuilder.add("umbra_express.midnightconfig.disabledRoles", "Disabled Roles");

		translationBuilder.add("commands.umbra_express.roles.list.enabled", "Enabled roles: %s");
		translationBuilder.add("commands.umbra_express.roles.list.disabled", "Disabled roles: %s");
		translationBuilder.add("commands.umbra_express.roles.list.all", "All roles: %s");

		translationBuilder.add("commands.umbra_express.roles.clearDisabledRoles", "Successfully cleared disabled roles (all roles are now enabled)");
		translationBuilder.add("commands.umbra_express.roles.toggle.invalid", "No role with id %s was found!");
		translationBuilder.add("commands.umbra_express.roles.toggle.get.true", "Role %s is currently disabled");
		translationBuilder.add("commands.umbra_express.roles.toggle.get.false", "Role %s is currently enabled");
		translationBuilder.add("commands.umbra_express.roles.toggle.set.alreadydisabled", "Role %s is already disabled!");
		translationBuilder.add("commands.umbra_express.roles.toggle.set.alreadyenabled", "Role %s is already enabled!");
		translationBuilder.add("commands.umbra_express.roles.toggle.set.true", "Role %s is now disabled");
		translationBuilder.add("commands.umbra_express.roles.toggle.set.false", "Role %s is now enabled");
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
