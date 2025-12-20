package dev.spiritstudios.umbra_express.mixin.roles.bartender;

import dev.doctor4t.wathe.block_entity.BeveragePlateBlockEntity;
import dev.spiritstudios.umbra_express.duck.BartenderPlate;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BeveragePlateBlockEntity.class, remap = false)
public abstract class BeveragePlateBlockEntityMixin implements BartenderPlate {

    @Shadow(remap = false) protected abstract void sync();

    @Unique private static final String BARTENDER_KEY = "bartender";

    @Unique private boolean bartender = false;

    @Override
    public boolean umbra_express$isBartender() {
        return this.bartender;
    }

    @Override
    public void umbra_express$setIsBartender(boolean bartender) {
        this.bartender = bartender;
    }

    @Override
    public void umbra_express$invokeSync() {
        this.sync();
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void writeIsInteractable(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        nbt.putBoolean(BARTENDER_KEY, this.bartender);
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void readIsInteractable(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        this.bartender = nbt.contains(BARTENDER_KEY) && nbt.getBoolean(BARTENDER_KEY);
    }

}
