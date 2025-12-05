package dev.spiritstudios.umbra_express.mixin.roles.bartender;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import dev.doctor4t.trainmurdermystery.block.FoodPlatterBlock;
import dev.doctor4t.trainmurdermystery.block_entity.BeveragePlateBlockEntity;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.item.CocktailItem;
import dev.spiritstudios.umbra_express.duck.BartenderPlate;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = FoodPlatterBlock.class, remap = false)
public class FoodPlatterBlockMixin {

    @Inject(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isCreative()Z"), cancellable = true)
    private void addCocktailItemAsBartender(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir, @Local BeveragePlateBlockEntity blockEntity) {
        if (!GameWorldComponent.KEY.get(world).isRole(player, UmbraExpressRoles.BARTENDER))
            return;

        BartenderPlate duck = BartenderPlate.cast(blockEntity);

        if (blockEntity.getStoredItems().isEmpty() || duck.umbra_express$isBartender()) {
            ItemStack stackInHand = player.getStackInHand(Hand.MAIN_HAND);

            if (!(stackInHand.getItem() instanceof CocktailItem))
                return;

            blockEntity.addItem(stackInHand);
            stackInHand.decrementUnlessCreative(1, player);

            BartenderPlate.cast(blockEntity).umbra_express$setIsBartender(true);
            blockEntity.setDrink(true);

            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @ModifyExpressionValue(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
    private int shareItemIndex(int original, @Share("itemIndex") LocalIntRef localIntRef) {
        localIntRef.set(original);
        return original;
    }

    @WrapOperation(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setStackInHand(Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;)V"))
    private void takeCocktail(PlayerEntity instance, Hand hand, ItemStack stack, Operation<Void> original, @Local BeveragePlateBlockEntity blockEntity, @Share("itemIndex") LocalIntRef localIntRef) {
        BartenderPlate duck = BartenderPlate.cast(blockEntity);

        if (duck.umbra_express$isBartender() && stack.getItem() instanceof CocktailItem) {
            List<ItemStack> storedItems = blockEntity.getStoredItems();
            storedItems.remove(localIntRef.get());

            if (storedItems.isEmpty())
                duck.umbra_express$setIsBartender(false);

            blockEntity.setDrink(false);
        }

        original.call(instance, hand, stack);

    }

}
