package dev.spiritstudios.umbra_express.mixin.optimization;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = GameWorldComponent.class, remap = false, priority = 1000000)
public class GameWorldComponentMixin {

	@Shadow
	@Final
	public static ComponentKey<GameWorldComponent> KEY;
	@Shadow
	@Final
	private World world;

	@Unique
	private boolean umbra_express$dirty = false;

	/**
	 * @author SkyNotTheLimit
	 * @reason avoid silent fails
	 */
	@Overwrite
	public void sync() {
		this.umbra_express$dirty = true;
	}

	@WrapMethod(method = "serverTick")
	private void syncIfDirty(Operation<Void> original) {
		original.call();
		if (this.umbra_express$dirty) {
			this.umbra_express$dirty = false;
			KEY.sync(this.world);
		}
	}
}
