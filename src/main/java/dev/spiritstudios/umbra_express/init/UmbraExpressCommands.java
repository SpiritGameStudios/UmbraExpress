package dev.spiritstudios.umbra_express.init;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.spiritstudios.umbra_express.command.BroadcastTicksCommand;
import dev.spiritstudios.umbra_express.command.DisableRoleCommand;
import dev.spiritstudios.umbra_express.command.ForceDevelopmentCommand;
import dev.spiritstudios.umbra_express.command.PlayerCountCommand;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.NonExtendable
public class UmbraExpressCommands {
	public static boolean development = FabricLoader.getInstance().isDevelopmentEnvironment();
	public static Role devForcedRole = UmbraExpressRoles.ASSASSIN;

	public static int maxBroadcastTicks = GameConstants.getInTicks(0, 45);

	public static final List<Role> disabledRoles = new ArrayList<>();

	public static void init(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
		LiteralCommandNode<ServerCommandSource> umbraRoot = literal("umbraexpress").build();

		dispatcher.getRoot().addChild(umbraRoot);

		ForceDevelopmentCommand.register(umbraRoot);
		PlayerCountCommand.register(umbraRoot);
		DisableRoleCommand.register(umbraRoot);
		BroadcastTicksCommand.register(umbraRoot);
	}

	public static LiteralArgumentBuilder<ServerCommandSource> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}

	public static <T> RequiredArgumentBuilder<ServerCommandSource, T> argument(String name, ArgumentType<T> argumentType) {
		return RequiredArgumentBuilder.argument(name, argumentType);
	}

	// TODO: implement role disabling via commands
	public static List<Role> getDisabledRoles() {
		return disabledRoles;
	}
}
