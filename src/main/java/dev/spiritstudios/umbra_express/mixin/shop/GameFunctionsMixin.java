package dev.spiritstudios.umbra_express.mixin.shop;

import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.spiritstudios.umbra_express.role.MoneyManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = GameFunctions.class, remap = false)
public class GameFunctionsMixin {

    @ModifyConstant(method = "killPlayer(Lnet/minecraft/entity/player/PlayerEntity;ZLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Identifier;)V", constant = @Constant(intValue = GameConstants.MONEY_PER_KILL), remap = false)
    private static int addToBalanceOnKill(int constant, PlayerEntity victim, boolean spawnBody, PlayerEntity killer, Identifier deathReason) {
        GameWorldComponent game = GameWorldComponent.KEY.get(killer.getWorld());
        Role role = game.getRole(killer);

        return MoneyManager.ROLE_MAP.containsKey(role) ? MoneyManager.ROLE_MAP.get(role).amountGainedPerKill().apply(victim) : 0;
    }

}
