package dev.spiritstudios.umbra_express.mixin.client.roles.shop;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.doctor4t.wathe.client.gui.screen.ingame.LimitedInventoryScreen;
import dev.doctor4t.wathe.util.ShopEntry;
import dev.spiritstudios.umbra_express.role.CustomShopEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LimitedInventoryScreen.StoreItemWidget.class, remap = false)
public class StoreItemWidgetMixin {

    @Shadow(remap = false) @Final public ShopEntry entry;

    @ModifyExpressionValue(method = "renderWidget", at = @At(value = "INVOKE", target = "Ldev/doctor4t/wathe/util/ShopEntry$Type;getTexture()Lnet/minecraft/util/Identifier;"), remap = false)
    private Identifier getTexture(Identifier original) {
        return this.entry instanceof CustomShopEntry textureShopEntry ? textureShopEntry.texture : original;
    }

}
