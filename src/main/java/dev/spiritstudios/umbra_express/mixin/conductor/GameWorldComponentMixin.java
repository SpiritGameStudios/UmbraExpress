package dev.spiritstudios.umbra_express.mixin.conductor;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.spiritstudios.umbra_express.duck.ConductorWorldComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(value = GameWorldComponent.class, remap = false)
public class GameWorldComponentMixin implements ConductorWorldComponent {

	@Nullable
	@Unique
	private UUID umbra_express$conductorUuid = null;

	@Override
	public void umbra_express$setConductor(@Nullable UUID uuid) {
		this.umbra_express$conductorUuid = uuid;
	}

	@Override
	public @Nullable UUID umbra_express$getConductor() {
		return this.umbra_express$conductorUuid;
	}

	@Inject(method = "readFromNbt", at = @At("RETURN"))
	private void readConductorUuid(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup, CallbackInfo ci) {
		this.umbra_express$conductorUuid = nbt.containsUuid("umbra_express_conductor_Uuid") ? nbt.getUuid("umbra_express_conductor_Uuid") : null;
	}

	@Inject(method = "writeToNbt", at = @At("RETURN"))
	private void writeConductorUuid(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup, CallbackInfo ci) {
		if (this.umbra_express$conductorUuid != null) {
			nbt.putUuid("umbra_express_conductor_Uuid", this.umbra_express$conductorUuid);
		}
	}
}
