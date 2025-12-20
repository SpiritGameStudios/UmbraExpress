package dev.spiritstudios.umbra_express.item;

import dev.doctor4t.wathe.cca.PlayerPoisonComponent;
import dev.doctor4t.wathe.game.GameConstants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AntidoteItem extends Item {

    public AntidoteItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!(entity instanceof PlayerEntity playerEntity))
            return ActionResult.PASS;

        PlayerPoisonComponent poisonComponent = PlayerPoisonComponent.KEY.get(playerEntity);

        if (poisonComponent.poisonTicks == -1)
            return ActionResult.PASS;

        poisonComponent.reset();
        World world = user.getWorld();

        if (!world.isClient())
            world.playSound(user, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.PLAYERS, 0.5F, MathHelper.nextBetween(world.getRandom(), 0.8F, 1.2F));

        if (!user.isCreative()) {
            user.getStackInHand(hand).decrement(1);
            user.getItemCooldownManager().set(this, GameConstants.getInTicks(1, 0));
        }

        return ActionResult.SUCCESS;
    }

}
