package dev.spiritstudios.umbra_express.mixin.event.shop;

import dev.doctor4t.wathe.util.ShopEntry;
import dev.spiritstudios.umbra_express.duck.Sale;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ShopEntry.class, remap = false)
public class ShopEntryMixin implements Sale {

	@Unique
	private int umbra_express$originalPrice = 0;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void capturePrice(ItemStack stack, int price, ShopEntry.Type type, CallbackInfo ci) {
		this.umbra_express$originalPrice = price;
	}

	@Override
	public int umbra_express$getOriginalPrice() {
		return this.umbra_express$originalPrice;
	}
}
