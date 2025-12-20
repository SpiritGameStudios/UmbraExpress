package dev.spiritstudios.umbra_express.mixin.event.shop;

import dev.doctor4t.wathe.util.ShopEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ShopEntry.class, remap = false)
public interface ShopEntryAccessor {

	@Mutable
	@Accessor("price")
	void umbra_express$setPrice(int price);
}
