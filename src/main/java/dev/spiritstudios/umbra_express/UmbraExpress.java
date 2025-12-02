package dev.spiritstudios.umbra_express;

import dev.doctor4t.trainmurdermystery.block.NeonPillarBlock;
import dev.doctor4t.trainmurdermystery.block.NeonTubeBlock;
import dev.doctor4t.trainmurdermystery.block.OrnamentBlock;
import dev.doctor4t.trainmurdermystery.block.ToggleableFacingLightBlock;
import dev.doctor4t.trainmurdermystery.cca.AreasWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.compat.TrainVoicePlugin;
import dev.spiritstudios.umbra_express.init.*;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
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
		UmbraExpressParticles.init();
		UmbraExpressSoundEvents.init();

		CommandRegistrationCallback.EVENT.register(UmbraExpressCommands::init);

		ServerPlayerEvents.JOIN.register(player -> {
			ServerWorld serverWorld = player.getServerWorld();

			GameWorldComponent game = GameWorldComponent.KEY.get(serverWorld);
			GameWorldComponent.GameStatus gameStatus = game.getGameStatus();

			if (gameStatus != GameWorldComponent.GameStatus.ACTIVE && gameStatus != GameWorldComponent.GameStatus.STARTING)
				return;

			player.changeGameMode(GameMode.SPECTATOR);
			TrainVoicePlugin.addPlayer(player.getUuid());

			AreasWorldComponent.PosWithOrientation spectatorSpawnPos = AreasWorldComponent.KEY.get(serverWorld).getSpectatorSpawnPos();
			player.teleport(serverWorld, spectatorSpawnPos.pos.getX(), spectatorSpawnPos.pos.getY(), spectatorSpawnPos.pos.getZ(), spectatorSpawnPos.yaw, spectatorSpawnPos.pitch);
		});
    }

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public static boolean canBeUsedByGhost(Block block) {
		return block instanceof ToggleableFacingLightBlock || block instanceof NeonPillarBlock || block instanceof NeonTubeBlock || block instanceof OrnamentBlock;
	}

	public static String getCooldownTimeString(int cooldownTicks, boolean roundUp) {
		int seconds = cooldownTicks / SharedConstants.TICKS_PER_SECOND;

		if (roundUp) {
			seconds++;
		}

		int minutes = seconds / 60;
		seconds %= 60;

		return String.format("%02d:%02d", minutes, seconds);
	}
}
