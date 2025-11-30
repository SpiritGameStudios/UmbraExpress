package dev.spiritstudios.umbra_express.mixin.roles.conductor;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.game.MurderGameMode;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(value = MurderGameMode.class, remap = false)
public class MurderGameModeMixin {

    @Inject(method = "assignRolesAndGetKillerCount", at = @At("RETURN"), remap = true)
    private static void assignOneConductor(@NotNull ServerWorld world, @NotNull List<ServerPlayerEntity> players, GameWorldComponent gameComponent, CallbackInfoReturnable<Integer> cir) {
        List<UUID> civilians = gameComponent.getAllWithRole(TMMRoles.CIVILIAN);
        if (civilians.isEmpty()) {
            return;
        }
        gameComponent.addRole(Util.getRandom(civilians, world.getRandom()), UmbraExpressRoles.CONDUCTOR);
    }

    @WrapOperation(method = "initializeGame", at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;indexOf(Ljava/lang/Object;)I"), remap = true)
    private int conductorAnnouncementText(ArrayList<RoleAnnouncementTexts.RoleAnnouncementText> instance, Object o, Operation<Integer> original, @Local ServerPlayerEntity serverPlayerEntity, @Local(argsOnly = true) GameWorldComponent gameWorldComponent) {
        if (gameWorldComponent.isRole(serverPlayerEntity, UmbraExpressRoles.CONDUCTOR)) {
            return original.call(instance, UmbraExpressRoles.CONDUCTOR_TEXT);
        }
        return original.call(instance, o);
    }
}
