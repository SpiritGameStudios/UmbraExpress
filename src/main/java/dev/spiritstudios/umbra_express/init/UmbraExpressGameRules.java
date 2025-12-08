package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.block.entity.CrystalBallBlockEntity;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface UmbraExpressGameRules {
	CustomGameRuleCategory CATEGORY = new CustomGameRuleCategory(
		UmbraExpress.id("category"),
		Text.translatable("gamerule.category.umbra_express").formatted(Formatting.BOLD, Formatting.YELLOW)
	);

	GameRules.Key<GameRules.IntRule> MAX_BROADCAST_TICKS = register(
		"maxBroadcastTicks",
		GameRuleFactory.createIntRule(
			GameConstants.getInTicks(0, 45),
			0
		)
	);
	GameRules.Key<GameRules.BooleanRule> FORCE_DEVELOPMENT = register(
		"forceDevelopment",
		GameRuleFactory.createBooleanRule(false)
	);
	GameRules.Key<GameRules.BooleanRule> MODIFY_MIN_COUNT = register(
		"modifyMinPlayerCount",
		GameRuleFactory.createBooleanRule(false)
	);
	GameRules.Key<GameRules.IntRule> MIN_PLAYER_COUNT = register(
		"minPlayerCount",
		GameRuleFactory.createIntRule(6, 0)
	);
	GameRules.Key<GameRules.BooleanRule> MODIFY_KILLER_COUNT = register(
		"modifyKillerCount",
		GameRuleFactory.createBooleanRule(false)
	);
	GameRules.Key<GameRules.IntRule> FORCE_KILLER_COUNT = register(
		"forceKillerCount",
		GameRuleFactory.createIntRule(1, 0)
	);
	GameRules.Key<GameRules.IntRule> CRYSTAL_BALL_COOLDOWN_TICKS = register(
		"crystalBallCooldownTime",
		GameRuleFactory.createIntRule(
			GameConstants.getInTicks(3, 0),
			CrystalBallBlockEntity.MIN_COOLDOWN_TICKS
		)
	);
	GameRules.Key<GameRules.BooleanRule> INTERACTIVE_BARTENDING = register(
		"interactiveBartending",
		GameRuleFactory.createBooleanRule(false)
	);

	static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Type<T> type) {
		return GameRuleRegistry.register(UmbraExpress.MOD_ID + ':' + name, CATEGORY, type);
	}

	static void init() {
		// NO-OP
	}
}
