package dev.spiritstudios.umbra_express.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ForceDevelopmentCommand {

	static void register(LiteralCommandNode<ServerCommandSource> root) {

	}
}
