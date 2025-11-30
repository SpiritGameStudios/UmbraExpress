package dev.spiritstudios.umbra_express.mixin.haunting;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.trainmurdermystery.compat.TrainVoicePlugin;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.spiritstudios.umbra_express.voicechat.HauntingVoicechatPlugin;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(value = GameFunctions.class, remap = false)
public class GameFunctionsMixin {

	@WrapOperation(method= "baseInitialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;changeGameMode(Lnet/minecraft/world/GameMode;)Z"), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/world/GameMode;SPECTATOR:Lnet/minecraft/world/GameMode;", opcode = Opcodes.GETSTATIC)), remap = true)
	private static boolean addAllNonPlayersToGroup(ServerPlayerEntity instance, GameMode gameMode, Operation<Boolean> original) {
        TrainVoicePlugin.addPlayer(instance.getUuid());
        return original.call(instance, gameMode);
    }

    @WrapMethod(method = "finalizeGame")
    private static void resetHaunters(ServerWorld world, Operation<Void> original) {
        original.call(world);
        HauntingVoicechatPlugin.reset();
    }
}
