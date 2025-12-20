package dev.spiritstudios.umbra_express.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.api.WatheRoles;
import dev.spiritstudios.umbra_express.init.UmbraExpressConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import static dev.spiritstudios.umbra_express.init.UmbraExpressCommands.argument;
import static dev.spiritstudios.umbra_express.init.UmbraExpressCommands.literal;

@ApiStatus.NonExtendable
public interface DisableRoleCommand {

	DynamicCommandExceptionType INVALID_ROLE = new DynamicCommandExceptionType((obj) -> Text.stringifiedTranslatable("commands.umbra_express.roles.toggle.invalid", obj));
	DynamicCommandExceptionType ALREADY_DISABLED = new DynamicCommandExceptionType((obj) -> Text.stringifiedTranslatable("commands.umbra_express.roles.toggle.set.alreadydisabled", obj));
	DynamicCommandExceptionType ALREADY_ENABLED = new DynamicCommandExceptionType((obj) -> Text.stringifiedTranslatable("commands.umbra_express.roles.toggle.set.alreadyenabled", obj));

	static void register(LiteralCommandNode<ServerCommandSource> parent) {
		LiteralCommandNode<ServerCommandSource> toggle = literal("toggle").build();

		ArgumentCommandNode<ServerCommandSource, Identifier> roleSelector = argument(
			"role",
			IdentifierArgumentType.identifier()
		).suggests(
			(context, builder) ->
				CommandSource.suggestIdentifiers(
					WatheRoles.ROLES.stream().map(Role::identifier),
					builder
				)
		).executes(DisableRoleCommand::executeGetToggle).build();

		LiteralCommandNode<ServerCommandSource> disable = literal("disable")
			.executes(context -> executeSetToggle(context, true)).build();


		LiteralCommandNode<ServerCommandSource> enable = literal("enable")
			.executes(context -> executeSetToggle(context, false)).build();

		LiteralCommandNode<ServerCommandSource> clear = literal("clearDisabledRoles")
			.executes(context -> {
				UmbraExpressConfig.disabledRoles.clear();
				UmbraExpressConfig.trySaveChanges();

				context.getSource().sendFeedback(() -> Text.translatable("commands.umbra_express.roles.clearDisabledRoles"), true);
				return Command.SINGLE_SUCCESS;
			})
			.build();

		parent.addChild(toggle);
		parent.addChild(clear);

		toggle.addChild(roleSelector);

		roleSelector.addChild(disable);
		roleSelector.addChild(enable);
	}

	static int executeGetToggle(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Role role = getRoleArgument(context);

		boolean disabled = UmbraExpressConfig.disabledRoles.contains(role.identifier().toString());

		context.getSource().sendFeedback(() -> Text.stringifiedTranslatable("commands.umbra_express.roles.toggle.get." + disabled, role.identifier()), false);
		return Command.SINGLE_SUCCESS;
	}

	static int executeSetToggle(CommandContext<ServerCommandSource> context, boolean disable) throws CommandSyntaxException {
		Role role = getRoleArgument(context);

		Identifier id = role.identifier();

		boolean disabled = UmbraExpressConfig.disabledRoles.contains(id.toString());

		if (disable == disabled) {
			if (disable) {
				throw ALREADY_DISABLED.create(id);
			} else {
				throw ALREADY_ENABLED.create(id);
			}
		}

		if (disable) {
			UmbraExpressConfig.disabledRoles.add(id.toString());
		} else {
			UmbraExpressConfig.disabledRoles.remove(id.toString());
		}

		UmbraExpressConfig.trySaveChanges();

		context.getSource().sendFeedback(() -> Text.stringifiedTranslatable("commands.umbra_express.roles.toggle.set." + disable, id), true);
		return Command.SINGLE_SUCCESS;
	}

	static Role getRoleArgument(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		Identifier id = IdentifierArgumentType.getIdentifier(context, "role");

		for (Role role : WatheRoles.ROLES) {
			if (role.identifier().equals(id)) {
				return role;
			}
		}

		throw INVALID_ROLE.create(id);
	}
}
