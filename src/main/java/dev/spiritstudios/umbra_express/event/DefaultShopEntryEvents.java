package dev.spiritstudios.umbra_express.event;

import dev.doctor4t.wathe.util.ShopEntry;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface DefaultShopEntryEvents {
	Event<ModifyPrice> MODIFY_PRICE = EventFactory.createArrayBacked(ModifyPrice.class,
		callbacks -> (shopEntry, price) -> {
			for (ModifyPrice callback : callbacks) {
				price = callback.updatePrice(shopEntry, price);
			}
			return price;
		}
	);

	@FunctionalInterface
	interface ModifyPrice {
		int updatePrice(ShopEntry shopEntry, int currentPrice);
	}
}
