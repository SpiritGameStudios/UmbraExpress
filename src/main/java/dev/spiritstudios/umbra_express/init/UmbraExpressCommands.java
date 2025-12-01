package dev.spiritstudios.umbra_express.init;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.spiritstudios.umbra_express.command.DisableRoleCommand;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface UmbraExpressCommands {

	static void init(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
		LiteralCommandNode<ServerCommandSource> umbraRoot = literal("umbra_express").build();

		dispatcher.getRoot().addChild(umbraRoot);

		DisableRoleCommand.register(umbraRoot);
	}

	static LiteralArgumentBuilder<ServerCommandSource> literal(String name) {
		return LiteralArgumentBuilder.literal(name);
	}

	static <T> RequiredArgumentBuilder<ServerCommandSource, T> argument(String name, ArgumentType<T> argumentType) {
		return RequiredArgumentBuilder.argument(name, argumentType);
	}
}
