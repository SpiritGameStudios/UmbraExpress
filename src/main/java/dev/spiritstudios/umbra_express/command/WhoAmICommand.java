package dev.spiritstudios.umbra_express.command;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

import static dev.spiritstudios.umbra_express.init.UmbraExpressCommands.literal;

@ApiStatus.NonExtendable
public interface WhoAmICommand {

	SimpleCommandExceptionType NOT_PLAYER_SOURCE = new SimpleCommandExceptionType(Text.translatable("commands.umbra_express.roles.find.notplayersource"));

	static void register(LiteralCommandNode<ServerCommandSource> parent) {
		LiteralCommandNode<ServerCommandSource> whoAmI = literal("find")
			.executes(context -> {
				ServerCommandSource source = context.getSource();
				if (!(source.getEntity() instanceof PlayerEntity player)) {
					throw NOT_PLAYER_SOURCE.create();
				}
				GameWorldComponent game = GameWorldComponent.KEY.get(player.getWorld());
				if (!game.isRunning()) {
					source.sendFeedback(() -> Text.translatable("commands.umbra_express.roles.find.notrunning"), false);
					return 0;
				}
				Role role = game.getRole(player);
				Text text = role == null ? Text.literal("null") : Text.translatable("announcement.role." + role.identifier().getPath());
				source.sendFeedback(() -> Text.stringifiedTranslatable("commands.umbra_express.roles.find.result", player.getDisplayName(), text), true);
				return 1;
			})
			.build();

		parent.addChild(whoAmI);
	}
}
