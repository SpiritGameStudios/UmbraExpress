package dev.spiritstudios.umbra_express.mixin.event;

import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.game.MurderGameMode;
import dev.spiritstudios.umbra_express.event.TMMPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(MurderGameMode.class)
public class MurderGameModeMixin {
	@Inject(method = "assignRolesAndGetKillerCount", at = @At("RETURN"))
	private static void give(@NotNull ServerWorld serverWorld, @NotNull List<ServerPlayerEntity> readyPlayerList, GameWorldComponent game, CallbackInfoReturnable<Integer> cir) {
		TMMPlayerEvents.Initializing invoker = TMMPlayerEvents.INITIALIZING.invoker();

		for (ServerPlayerEntity serverPlayerEntity : serverWorld.getPlayers()) {
			Role role = game.getRole(serverPlayerEntity);
			boolean playing = readyPlayerList.contains(serverPlayerEntity);

			invoker.onInitializing(serverWorld, serverPlayerEntity, role, playing, game);
		}
	}
}
