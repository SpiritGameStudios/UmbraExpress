package dev.spiritstudios.umbra_express.mixin.client.roles.bartender;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.block_entity.BeveragePlateBlockEntity;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.api.event.CanSeePoison;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// Wow I'm injecting into my own mixin lmao - SkyNotTheLimit
@Environment(EnvType.CLIENT)
@Mixin(value = BeveragePlateBlockEntity.class, priority = 2000)
public class BeveragePlateBlockEntityMixinMixin {

	@TargetHandler(
		mixin = "dev.doctor4t.wathe.mixin.client.self.BeveragePlateBlockEntityMixin",
		name = "tickWithoutFearOfCrashing"
	)
	@WrapOperation(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Ldev/doctor4t/wathe/api/event/CanSeePoison;visible(Lnet/minecraft/entity/player/PlayerEntity;)Z"), allow = 1)
	private static boolean bartenderSeesPlatePoison(CanSeePoison instance, PlayerEntity player, Operation<Boolean> original) {
		if (original.call(instance, player)) {
			return true;
		}
		GameWorldComponent game = GameWorldComponent.KEY.get(player.getWorld());
		return game.isRole(player, UmbraExpressRoles.BARTENDER);
	}
}
