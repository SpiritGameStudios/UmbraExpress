package dev.spiritstudios.umbra_express.mixin.client.roles.locksmith;

import dev.doctor4t.trainmurdermystery.client.util.TMMItemTooltips;
import dev.spiritstudios.umbra_express.init.UmbraExpressItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(value = TMMItemTooltips.class, remap = false)
public abstract class TMMItemTooltipsMixin {

	@Shadow(remap = true)
	private static void addTooltipForItem(Item item, @NotNull ItemStack itemStack, List<Text> tooltipList) {
		throw new UnsupportedOperationException("Mixin shadow");
	}

	@Inject(method = "lambda$addTooltips$0", at = @At("RETURN"), remap = true)
	private static void addLocksmithTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> tooltipList, CallbackInfo ci) {
		addTooltipForItem(UmbraExpressItems.MASTER_KEY, itemStack, tooltipList);
	}
}
