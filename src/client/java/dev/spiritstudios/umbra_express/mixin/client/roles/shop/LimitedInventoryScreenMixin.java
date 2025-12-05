package dev.spiritstudios.umbra_express.mixin.client.roles.shop;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.gui.screen.ingame.LimitedInventoryScreen;
import dev.doctor4t.trainmurdermystery.util.ShopEntry;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(value = LimitedInventoryScreen.class, remap = false)
public class LimitedInventoryScreenMixin {

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/cca/GameWorldComponent;canUseKillerFeatures(Lnet/minecraft/entity/player/PlayerEntity;)Z"), remap = false)
    private boolean canUseStoreGui(GameWorldComponent instance, PlayerEntity player, Operation<Boolean> original, @Share("shop") LocalRef<List<ShopEntry>> localRef) {
        Role role = instance.getRole(player);

        if (!UmbraExpressRoles.MONEY_MAKERS.containsKey(role))
            return false;

        List<ShopEntry> shop = UmbraExpressRoles.MONEY_MAKERS.get(role).shop();
        localRef.set(shop);

        return !shop.isEmpty();
    }

    @ModifyExpressionValue(method = "init", at = @At(value = "FIELD", target = "Ldev/doctor4t/trainmurdermystery/game/GameConstants;SHOP_ENTRIES:Ljava/util/List;"), remap = false)
    private List<ShopEntry> getShop(List<ShopEntry> original, @Share("shop") LocalRef<List<ShopEntry>> localRef) {
        return localRef.get();
    }

}
