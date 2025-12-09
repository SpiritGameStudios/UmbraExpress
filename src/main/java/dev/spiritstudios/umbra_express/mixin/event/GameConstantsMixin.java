package dev.spiritstudios.umbra_express.mixin.event;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.util.ShopEntry;
import dev.spiritstudios.umbra_express.event.DefaultShopEntryEvents;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;

@Debug(export = true)
@Mixin(GameConstants.class)
public interface GameConstantsMixin {

	// lambda SHOP_ENTRIES
	@WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "()Ljava/util/ArrayList;"))
	private static ArrayList<ShopEntry> customArrayList(Operation<ArrayList<ShopEntry>> original) {
		// this totally breaks mod compat lol
		return new ArrayList<>() {
			@Override
			public boolean add(ShopEntry element) {
				modifyShopEntry(element);
				return super.add(element);
			}

			@Override
			public void add(int index, ShopEntry element) {
				modifyShopEntry(element);
				super.add(index, element);
			}

			private void modifyShopEntry(ShopEntry entry) {
				int updated = DefaultShopEntryEvents.MODIFY_PRICE.invoker().updatePrice(entry, entry.price());
				((ShopEntryAccessor) entry).umbra_express$setPrice(updated);
			}
		};
	}
}
