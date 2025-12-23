package dev.spiritstudios.umbra_express.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.spiritstudios.umbra_express.cca.CrystalBallWorldComponent;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

import static dev.spiritstudios.umbra_express.init.UmbraExpressCommands.PERMISSION_CHECKER;
import static dev.spiritstudios.umbra_express.init.UmbraExpressCommands.argument;
import static dev.spiritstudios.umbra_express.init.UmbraExpressCommands.literal;

@ApiStatus.NonExtendable
public interface LobbyApparitionCommand {

	static void register(LiteralCommandNode<ServerCommandSource> parent) {
		LiteralCommandNode<ServerCommandSource> lobbyApparition = literal("lobbyapparition")
			.executes(context -> {
				boolean view = CrystalBallWorldComponent.KEY.get(context.getSource().getWorld()).canViewInLobby();
				context.getSource().sendFeedback(() -> Text.translatable("commands.umbra_express.lobby_apparition.get." + view), false);
				return Command.SINGLE_SUCCESS;
			})
			.build();

		LiteralCommandNode<ServerCommandSource> set = literal("set")
			.requires(PERMISSION_CHECKER)
			.then(
				argument("value", BoolArgumentType.bool())
					.executes(LobbyApparitionCommand::executeSet)
			)
			.build();

		parent.addChild(lobbyApparition);

		lobbyApparition.addChild(set);
	}

	static int executeSet(CommandContext<ServerCommandSource> context) {
		boolean value = BoolArgumentType.getBool(context, "value");
		CrystalBallWorldComponent.KEY.get(context.getSource().getWorld()).setCanView(value);
		context.getSource().sendFeedback(() -> Text.translatable("commands.umbra_express.lobby_apparition.set." + value), false);
		return Command.SINGLE_SUCCESS;
	}
}
