package dev.spiritstudios.umbra_express.mixin.haunting;

import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.spiritstudios.umbra_express.voicechat.HauntingVoicechatPlugin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(GameWorldComponent.class)
public class GameWorldComponentMixin {

    @Shadow
    @Final
    private World world;

    @Inject(method = "addRole(Ljava/util/UUID;Ldev/doctor4t/trainmurdermystery/api/Role;)V", at = @At("HEAD"))
    private void addKiller(UUID uuid, Role role, CallbackInfo ci) {
        if (!role.canUseKiller()) {
            return;
        }

        PlayerEntity player = this.world.getPlayerByUuid(uuid);
        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return;
        }

        HauntingVoicechatPlugin.initHauntForKiller(serverPlayer);
    }
}
