package dev.spiritstudios.umbra_express;

import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.block.NeonPillarBlock;
import dev.doctor4t.trainmurdermystery.block.NeonTubeBlock;
import dev.doctor4t.trainmurdermystery.block.OrnamentBlock;
import dev.doctor4t.trainmurdermystery.block.ToggleableFacingLightBlock;
import dev.spiritstudios.umbra_express.command.UmbraExpressCommands;
import dev.spiritstudios.umbra_express.init.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UmbraExpress implements ModInitializer {
    public static final String MOD_ID = "umbra_express";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final boolean DEVELOPMENT = FabricLoader.getInstance().isDevelopmentEnvironment();
	public static final Role DEV_FORCED_ROLE = UmbraExpressRoles.CONDUCTOR;

    @Override
    public void onInitialize() {
		UmbraExpressGameRules.init();
		UmbraExpressRoles.init();
		UmbraExpressBlocks.init();
		UmbraExpressItems.init();
		UmbraExpressBlockEntities.init();
		UmbraExpressParticles.init();
		UmbraExpressSoundEvents.init();

		CommandRegistrationCallback.EVENT.register(UmbraExpressCommands::init);
    }

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public static boolean canBeUsedByGhost(Block block) {
		return block instanceof ToggleableFacingLightBlock || block instanceof NeonPillarBlock || block instanceof NeonTubeBlock || block instanceof OrnamentBlock;
	}

	public static String getCooldownTimeString(int cooldownTicks) {
		int seconds = cooldownTicks / SharedConstants.TICKS_PER_SECOND + 1;

		int minutes = seconds / 60;
		seconds %= 60;

		return String.format("%02d:%02d", minutes, seconds);
	}
}
