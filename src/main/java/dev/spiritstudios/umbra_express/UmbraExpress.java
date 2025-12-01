package dev.spiritstudios.umbra_express;

import dev.doctor4t.trainmurdermystery.block.NeonPillarBlock;
import dev.doctor4t.trainmurdermystery.block.NeonTubeBlock;
import dev.doctor4t.trainmurdermystery.block.OrnamentBlock;
import dev.doctor4t.trainmurdermystery.block.ToggleableFacingLightBlock;
import dev.spiritstudios.umbra_express.init.UmbraExpressCommands;
import dev.spiritstudios.umbra_express.init.UmbraExpressBlocks;
import dev.spiritstudios.umbra_express.init.UmbraExpressConfig;
import dev.spiritstudios.umbra_express.init.UmbraExpressGameRules;
import dev.spiritstudios.umbra_express.init.UmbraExpressItems;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import dev.spiritstudios.umbra_express.init.UmbraExpressBlockEntities;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UmbraExpress implements ModInitializer {
    public static final String MOD_ID = "umbra_express";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final boolean DEVELOPMENT = FabricLoader.getInstance().isDevelopmentEnvironment();

	@Override
    public void onInitialize() {
		MidnightConfig.init(MOD_ID, UmbraExpressConfig.class);

		UmbraExpressGameRules.init();
		UmbraExpressRoles.init();
		UmbraExpressBlocks.init();
		UmbraExpressItems.init();
		UmbraExpressBlockEntities.init();

		CommandRegistrationCallback.EVENT.register(UmbraExpressCommands::init);
    }

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public static boolean canBeUsedByGhost(Block block) {
		return block instanceof ToggleableFacingLightBlock || block instanceof NeonPillarBlock || block instanceof NeonTubeBlock || block instanceof OrnamentBlock;
	}
}
