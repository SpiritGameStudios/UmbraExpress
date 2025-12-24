package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.spiritstudios.umbra_express.UmbraExpress;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static dev.spiritstudios.umbra_express.init.UmbraExpressGameRules.*;

// Powered by Midnightlib
public class UmbraExpressConfig extends MidnightConfig {

	// TODO: implement proper role forcing
	@Entry
	public static String forceDevRole = UmbraExpressRoles.ASSASSIN.identifier().toString();
	@Entry
	public static boolean accurateInstinctHighlights = true;
	@Entry
	public static List<String> disabledRoles = new ArrayList<>();

	public static boolean development(World world) {
		return UmbraExpress.DEVELOPMENT || world.getGameRules().getBoolean(UmbraExpressGameRules.FORCE_DEVELOPMENT);
	}

	public static int maxBroadcastTicks(World world) {
		return world.getGameRules().getInt(MAX_BROADCAST_TICKS);
	}

	public static int crystalBallCooldownTicks(World world) {
		return world.getGameRules().getInt(CRYSTAL_BALL_COOLDOWN_TICKS);
	}

	public static int getMinPlayerCount(int original, World world) {
		GameRules gameRules = world.getGameRules();
		if (!gameRules.getBoolean(UmbraExpressGameRules.MODIFY_MIN_COUNT)) {
			return original;
		}
		int otherMin = gameRules.getInt(UmbraExpressGameRules.MIN_PLAYER_COUNT);
		return Math.min(original, otherMin);
	}

	public static int getKillerCount(int original, World world) {
		GameRules gameRules = world.getGameRules();
		if (!gameRules.getBoolean(UmbraExpressGameRules.MODIFY_KILLER_COUNT)) {
			return original;
		}
		int killerCount = gameRules.getInt(UmbraExpressGameRules.FORCE_KILLER_COUNT);
		return Math.max(original, killerCount);
	}

	@Nullable
	public static MaybeConductor getDevRole() {
		Identifier id = Identifier.tryParse(forceDevRole);
		if (id == null) return null;

		if (id.equals(UmbraExpressRoles.CONDUCTOR)) {
			return new MaybeConductor(true, null);
		}

		for (Role maybe : TMMRoles.ROLES) {
			if (maybe.identifier().equals(id)) {
				return new MaybeConductor(false, maybe);
			}
		}

		return null;
	}

	public static List<Identifier> getDisabledRoles() {
		return UmbraExpressConfig.disabledRoles.stream().map(Identifier::tryParse).filter(Objects::nonNull).toList();
	}

	public static void trySaveChanges() {
		try {
			MidnightConfig.write(UmbraExpress.MOD_ID);
		} catch (Throwable throwable) {
			UmbraExpress.LOGGER.error("An error occurred while saving the config!", throwable);
		}
	}

	public record MaybeConductor(boolean conductor, Role otherwise) {
	}
}
