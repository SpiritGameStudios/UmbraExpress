package dev.spiritstudios.umbra_express;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

import static net.minecraft.SharedConstants.TICKS_PER_SECOND;

public class UmbraExpressGameRules {
	public static final GameRules.Key<GameRules.IntRule> MAX_BROADCAST_TICKS = GameRuleRegistry.register(
		"maxBroadcastTicks",
		GameRules.Category.MISC,
		GameRuleFactory.createIntRule(45 * TICKS_PER_SECOND, 0)
	);

	public static void init() {
		// NO-OP
	}
}
