package dev.spiritstudios.umbra_express.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.spiritstudios.umbra_express.init.UmbraExpressConfig;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.spiritstudios.umbra_express.init.UmbraExpressCommands.literal;

@ApiStatus.NonExtendable
public interface ListRoleCommand {

	static void register(LiteralCommandNode<ServerCommandSource> parent) {
		LiteralCommandNode<ServerCommandSource> list = literal("list").build();

		LiteralCommandNode<ServerCommandSource> enabled = literal("enabled")
			.executes(context -> executeList(context, true, false))
			.build();

		LiteralCommandNode<ServerCommandSource> disabled = literal("disabled")
			.executes(context -> executeList(context, false, true))
			.build();

		LiteralCommandNode<ServerCommandSource> all = literal("all")
			.executes(context -> executeList(context, false, false))
			.build();

		parent.addChild(list);

		list.addChild(enabled);
		list.addChild(disabled);
		list.addChild(all);
	}

	static int executeList(CommandContext<ServerCommandSource> context, boolean enabled, boolean disabled) {
		ServerCommandSource source = context.getSource();
		List<Identifier> output = new ArrayList<>();
		List<Identifier> disabledRoles = UmbraExpressConfig.getDisabledRoles();
		for (Role role : TMMRoles.ROLES) {
			Identifier id = role.identifier();
			if (enabled) {
				if (!disabledRoles.contains(id)) {
					output.add(id);
				}
			} else if (disabled) {
				if (disabledRoles.contains(id)) {
					output.add(id);
				}
			} else {
				output.add(id);
			}
		}

		String sub;
		if (enabled) {
			sub = "enabled";
		} else if (disabled) {
			sub = "disabled";
		} else {
			sub = "all";
		}

		source.sendFeedback(() -> Text.stringifiedTranslatable("commands.umbra_express.roles.list." + sub, Arrays.toString(output.toArray())), false);

		return 1;
	}
}
