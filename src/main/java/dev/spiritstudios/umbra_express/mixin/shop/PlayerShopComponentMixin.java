package dev.spiritstudios.umbra_express.mixin.shop;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerShopComponent;
import dev.doctor4t.trainmurdermystery.util.ShopEntry;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(value = PlayerShopComponent.class, remap = false)
public class PlayerShopComponentMixin {

    @Shadow @Final private PlayerEntity player;

    @ModifyExpressionValue(method = "tryBuy", at = @At(value = "FIELD", target = "Ldev/doctor4t/trainmurdermystery/game/GameConstants;SHOP_ENTRIES:Ljava/util/List;"), remap = false)
    private List<ShopEntry> getShop(List<ShopEntry> original) {
        GameWorldComponent game = GameWorldComponent.KEY.get(this.player.getWorld());
        Role role = game.getRole(this.player);

        return UmbraExpressRoles.MONEY_MAKERS.containsKey(role) ? UmbraExpressRoles.MONEY_MAKERS.get(role).shop() : List.of();
    }

}
