package dev.spiritstudios.umbra_express.mixin.roles.assassin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.game.MurderGameMode;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.minecraft.server.network.ServerPlayerEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(value = MurderGameMode.class, remap = false)
public class MurderGameModeMixin {

	@ModifyExpressionValue(method = "tickServerGameLoop", at = @At(value = "FIELD", target = "Ldev/doctor4t/trainmurdermystery/game/GameConstants;PASSIVE_MONEY_TICKER:Ljava/util/function/Function;", remap = false, opcode = Opcodes.GETSTATIC), remap = true)
	private static Function<Long, Integer> getAssassinPassiveTicker(Function<Long, Integer> original, @Local ServerPlayerEntity player, @Local(argsOnly = true)GameWorldComponent game) {
		if (game.isRole(player, UmbraExpressRoles.ASSASSIN)) {
			return UmbraExpressRoles.ASSASSIN_PASSIVE_MONEY_TICKER;
		}
		return original;
	}
}
