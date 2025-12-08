package dev.spiritstudios.umbra_express.role;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.util.ShopEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class CustomShopEntry extends ShopEntry {

    public final Identifier texture;

    public CustomShopEntry(ItemStack stack, int price) {
        this(stack, price, TMM.id("gui/shop_slot"));
    }

    public CustomShopEntry(ItemStack stack, int price, Identifier texture) {
        super(stack, price, Type.WEAPON); // arbitrary
        this.texture = texture;
    }

}
