package dev.spiritstudios.umbra_express.mixin.conductor;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerMoodComponent;
import dev.spiritstudios.umbra_express.duck.ConductorWorldComponent;
import dev.spiritstudios.umbra_express.role.task.BroadcastTask;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Objects;

@Mixin(value = PlayerMoodComponent.class, remap = false)
public class PlayerMoodComponentMixin {

    @Unique
    private boolean umbra_express$hasDoneAnnouncement = false;

    @Shadow
    @Final
    public Map<PlayerMoodComponent.Task, PlayerMoodComponent.TrainTask> tasks;

    @Shadow
    @Final
    private PlayerEntity player;

    @WrapOperation(method = "readFromNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;contains(Ljava/lang/String;)Z"), remap = true)
    private boolean decodeProperly(NbtCompound instance, String key, Operation<Boolean> original) {
        if (instance.contains(key, NbtElement.STRING_TYPE)) {
            String maybe = instance.getString(key);
            if (maybe.equals(BroadcastTask.NAME)) {
                PlayerMoodComponent.Task typeEnum = BroadcastTask.BROADCAST;
                if (typeEnum != null) {
                    this.tasks.put(typeEnum, typeEnum.setFunction.apply(instance));
                }
            }
            return false;
        }
        return original.call(instance, key);
    }

    @Inject(method = "generateTask", at = @At(value = "INVOKE", target = "Ldev/doctor4t/trainmurdermystery/cca/PlayerMoodComponent$Task;values()[Ldev/doctor4t/trainmurdermystery/cca/PlayerMoodComponent$Task;", shift = At.Shift.BEFORE), cancellable = true)
    private void returnAnnouncementEarly(CallbackInfoReturnable<PlayerMoodComponent.TrainTask> cir) {
        if (ConductorWorldComponent.cast(GameWorldComponent.KEY.get(this.player.getWorld())).umbra_express$isConductor(this.player) && !this.umbra_express$hasDoneAnnouncement) {
            this.umbra_express$hasDoneAnnouncement = true;
            cir.setReturnValue(new BroadcastTask());
        }
    }

    @ModifyExpressionValue(method = "generateTask", at = @At(value = "INVOKE", target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z"))
    private boolean neverFindNaturally(boolean original, @Local PlayerMoodComponent.Task task) {
        return original || Objects.equals(BroadcastTask.BROADCAST, task);
    }

    @WrapMethod(method = "writeToNbt")
    private void addHasAnnounced(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup, Operation<Void> original) {
        original.call(tag, registryLookup);
        tag.putBoolean("umbra_express_hasDoneAnnouncement", this.umbra_express$hasDoneAnnouncement);
    }

    @WrapMethod(method = "readFromNbt")
    private void readHasAnnounced(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup, Operation<Void> original) {
        original.call(tag, registryLookup);
        if (tag.contains("umbra_express_hasDoneAnnouncement")) {
            this.umbra_express$hasDoneAnnouncement = tag.getBoolean("umbra_express_hasDoneAnnouncement");
        }
    }

    @WrapMethod(method = "reset")
    private void resetAnnouncementBool(Operation<Void> original) {
        original.call();
        this.umbra_express$hasDoneAnnouncement = false;
    }
}
