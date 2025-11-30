package dev.spiritstudios.umbra_express.mixin.roles.conductor;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import dev.spiritstudios.umbra_express.voicechat.ConductorVoicechatPlugin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GameFunctions.class, remap = false)
public class GameFunctionsMixin {

	@Inject(method= "initializeGame", at = @At("RETURN"), remap = true)
	private static void initConductor(ServerWorld serverWorld, CallbackInfo ci) {
        ConductorVoicechatPlugin.reset();
        BroadcastWorldComponent.KEY.get(serverWorld).reset();
        GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(serverWorld);

        for (ServerPlayerEntity serverPlayer : serverWorld.getPlayers()) {
            if (UmbraExpressRoles.CONDUCTOR.equals(gameWorldComponent.getRole(serverPlayer))) {
                continue;
            }
            ConductorVoicechatPlugin.addReceiver(serverPlayer);
        }
    }

    @WrapMethod(method = "finalizeGame", remap = true)
    private static void resetBroadcast(ServerWorld world, Operation<Void> original) {
        original.call(world);
        ConductorVoicechatPlugin.reset();
        BroadcastWorldComponent.KEY.get(world).reset();
    }

    @Inject(method = "killPlayer(Lnet/minecraft/entity/player/PlayerEntity;ZLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Identifier;)V", at = @At("RETURN"))
    private static void stopBroadcastOnConductorDeath(PlayerEntity victim, boolean spawnBody, PlayerEntity killer, Identifier deathReason, CallbackInfo ci) {
        World world = victim.getWorld();
        if (victim.isSpectator() && GameWorldComponent.KEY.get(world).isRole(victim, UmbraExpressRoles.CONDUCTOR)) {
            BroadcastWorldComponent.KEY.get(world).setBroadcasting(false);
        }
    }
}
