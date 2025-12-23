package dev.spiritstudios.umbra_express.init;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.spiritstudios.umbra_express.command.DisableRoleCommand;
import dev.spiritstudios.umbra_express.command.ListRoleCommand;
import dev.spiritstudios.umbra_express.command.LobbyApparitionCommand;
import dev.spiritstudios.umbra_express.command.WhoAmICommand;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Predicate;

@ApiStatus.NonExtendable
public interface UmbraExpressCommands {

	int MIN_PERMISSION_LEVEL = 2;
	Predicate<ServerCommandSource> PERMISSION_CHECKER = source -> source.hasPermissionLevel(MIN_PERMISSION_LEVEL);

	static void init(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
		LiteralCommandNode<ServerCommandSource> umbraRoot = literal("umbraexpress").build();

		LiteralCommandNode<ServerCommandSource> roles = literal("roles").build();

		dispatcher.getRoot().addChild(umbraRoot);

		umbraRoot.addChild(roles);
		LobbyApparitionCommand.register(umbraRoot);

		DisableRoleCommand.register(roles);
		ListRoleCommand.register(roles);
		WhoAmICommand.register(roles);
	}

	static LiteralArgumentBuilder<ServerCommandSource> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}

	static <T> RequiredArgumentBuilder<ServerCommandSource, T> argument(String name, ArgumentType<T> argumentType) {
		return RequiredArgumentBuilder.argument(name, argumentType);
	}
}
