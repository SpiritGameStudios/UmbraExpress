package dev.spiritstudios.umbra_express.mixin.roles;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.client.gui.RoleAnnouncementTexts;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RoleAnnouncementTexts.RoleAnnouncementText.class)
public class RoleAnnouncementTextMixin {

	@Definition(id = "KILLER", field = "Ldev/doctor4t/trainmurdermystery/client/gui/RoleAnnouncementTexts;KILLER:Ldev/doctor4t/trainmurdermystery/client/gui/RoleAnnouncementTexts$RoleAnnouncementText;")
	@Expression("this == KILLER")
	@WrapOperation(method = {"getEndText", "getLoseText"}, at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean fixWinCondition(Object left, Object right, Operation<Boolean> original) {
		return UmbraExpressRoles.fixWinCondition(left, right, original);
	}
}
