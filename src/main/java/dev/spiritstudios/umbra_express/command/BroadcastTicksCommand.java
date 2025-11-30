package dev.spiritstudios.umbra_express.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import dev.spiritstudios.umbra_express.init.UmbraExpressCommands;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

import static dev.spiritstudios.umbra_express.init.UmbraExpressCommands.argument;
import static dev.spiritstudios.umbra_express.init.UmbraExpressCommands.literal;

@ApiStatus.NonExtendable
public interface BroadcastTicksCommand {

	static void register(LiteralCommandNode<ServerCommandSource> root) {
		LiteralCommandNode<ServerCommandSource> maxBroadcastTicks = literal("maxbroadcastticks").build();

		LiteralCommandNode<ServerCommandSource> set = literal("set")
			.then(
				argument("value", IntegerArgumentType.integer(0))
					.executes(context -> {
						UmbraExpressCommands.maxBroadcastTicks = IntegerArgumentType.getInteger(context, "value");
						context.getSource().sendFeedback(
							() -> Text.stringifiedTranslatable(
								"commands.umbraexpress.maxbroadcastticks.set",
								UmbraExpressCommands.maxBroadcastTicks),
							true);
						return 1;
					})
			).build();

		LiteralCommandNode<ServerCommandSource> get = literal("get").executes(context -> {
			context.getSource().sendFeedback(
				() -> Text.stringifiedTranslatable(
					"commands.umbraexpress.maxbroadcastticks.get",
					UmbraExpressCommands.maxBroadcastTicks),
				false);
			return 1;
		}).build();

		LiteralCommandNode<ServerCommandSource> reset = literal("reset").executes(context -> {
			UmbraExpressCommands.maxBroadcastTicks = BroadcastWorldComponent.DEFAULT_BROADCAST_TICKS;
			context.getSource().sendFeedback(
				() -> Text.stringifiedTranslatable(
					"commands.umbraexpress.maxbroadcastticks.reset",
					UmbraExpressCommands.maxBroadcastTicks),
				true);
			return 1;
		}).build();

		root.addChild(maxBroadcastTicks);

		maxBroadcastTicks.addChild(set);
		maxBroadcastTicks.addChild(get);
		maxBroadcastTicks.addChild(reset);
	}
}
