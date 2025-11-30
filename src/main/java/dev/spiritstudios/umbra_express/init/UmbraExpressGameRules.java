package dev.spiritstudios.umbra_express.init;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.ApiStatus;

import static net.minecraft.SharedConstants.TICKS_PER_SECOND;

@ApiStatus.NonExtendable
public interface UmbraExpressGameRules {
	GameRules.Key<GameRules.IntRule> MAX_BROADCAST_TICKS = GameRuleRegistry.register(
		"maxBroadcastTicks",
		GameRules.Category.MISC,
		GameRuleFactory.createIntRule(45 * TICKS_PER_SECOND, 0)
	);

	static void init() {
		// NO-OP
	}
}
