package dev.spiritstudios.umbra_express.mixin.event.shop;

import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.util.ShopEntry;
import dev.spiritstudios.umbra_express.duck.Sale;
import dev.spiritstudios.umbra_express.event.DefaultShopEntryEvents;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Registries.class)
public class RegistriesMixin {

	@Inject(method = "freezeRegistries", at = @At("RETURN"))
	private static void runShopEvents(CallbackInfo ci) {
		for (ShopEntry shopEntry : GameConstants.SHOP_ENTRIES) {
			int updated = DefaultShopEntryEvents.MODIFY_PRICE.invoker()
				.updatePrice(shopEntry, ((Sale) shopEntry).umbra_express$getOriginalPrice());
			((ShopEntryAccessor) shopEntry).umbra_express$setPrice(updated);
		}
	}
}
