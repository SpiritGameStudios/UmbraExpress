package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.block.entity.CrystalBallBlockEntity;
import dev.spiritstudios.umbra_express.cca.ApparitionViewerComponent;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface UmbraExpressGameRules {
	CustomGameRuleCategory CATEGORY = new CustomGameRuleCategory(
		UmbraExpress.id("category"),
		Text.translatable("gamerule.category.umbra_express").formatted(Formatting.BOLD, Formatting.YELLOW));

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
			CrystalBallBlockEntity.MAX_APPARITION_RENDER_TICKS
		)
	);
	GameRules.Key<GameRules.BooleanRule> SHOW_APPARITION_IN_LOBBY = register(
		"showApparitionInLobby",
		GameRuleFactory.createBooleanRule(
			true,
			UmbraExpressGameRules::getShowApparitionInLobbyCallback
		)
	);

	static void getShowApparitionInLobbyCallback(MinecraftServer server, GameRules.BooleanRule rule) {
		for (ServerPlayerEntity serverPlayerEntity : server.getPlayerManager().getPlayerList()) {
			GameWorldComponent game = GameWorldComponent.KEY.get(serverPlayerEntity.getWorld());
			ApparitionViewerComponent.KEY.get(serverPlayerEntity).setCanView(game, rule.get());
		}
	}

	static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Type<T> type) {
		return GameRuleRegistry.register(UmbraExpress.MOD_ID + ":" + name, CATEGORY, type);
	}

	static void init() {
		// NO-OP
	}
}
