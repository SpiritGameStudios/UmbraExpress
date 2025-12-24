package dev.spiritstudios.umbra_express.mixin.conductor;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.spiritstudios.umbra_express.duck.ConductorWorldComponent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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
}
