package dev.spiritstudios.umbra_express.mixin.roles;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.cca.GameRoundEndComponent;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GameRoundEndComponent.class, remap = false)
public class GameRoundEndComponentMixin {

	@Definition(id = "KILLER", field = "Ldev/doctor4t/wathe/client/gui/RoleAnnouncementTexts;KILLER:Ldev/doctor4t/wathe/client/gui/RoleAnnouncementTexts$RoleAnnouncementText;")
	@Expression("? == KILLER")
	@WrapOperation(method = "didWin", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean fixWinCondition(Object left, Object right, Operation<Boolean> original) {
		return UmbraExpressRoles.fixWinCondition(left, right, original);
	}
}
