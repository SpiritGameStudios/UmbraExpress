package dev.spiritstudios.umbra_express.mixin.client;

import dev.doctor4t.wathe.client.util.WatheItemTooltips;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(value = WatheItemTooltips.class, remap = false)
public interface TMMItemTooltipsAccessor {

	@Invoker(value = "addTooltipForItem", remap = false)
	static void umbra_express$invokeAddTooltipForItem(Item item, @NotNull ItemStack itemStack, List<Text> tooltipList) {
		throw new UnsupportedOperationException("Mixin shadow");
	}

}
