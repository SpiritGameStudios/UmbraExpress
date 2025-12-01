package dev.spiritstudios.umbra_express.mixin.roles.locksmith;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.trainmurdermystery.block.SmallDoorBlock;
import dev.doctor4t.trainmurdermystery.block.TrainDoorBlock;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.spiritstudios.umbra_express.init.UmbraExpressItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({SmallDoorBlock.class, TrainDoorBlock.class})
public class DoorBlocksMixin {

	@WrapOperation(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
	private boolean locksmithHasAMasterKey(ItemStack instance, Item item, Operation<Boolean> original) {
		if (item.equals(TMMItems.LOCKPICK)) {
			return original.call(instance, item) || original.call(instance, UmbraExpressItems.MASTER_KEY);
		}
		return original.call(instance, item);
	}
}
