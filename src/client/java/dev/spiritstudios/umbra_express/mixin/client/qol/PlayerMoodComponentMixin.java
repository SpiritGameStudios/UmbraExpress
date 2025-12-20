package dev.spiritstudios.umbra_express.mixin.client.qol;

import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.UUID;

//@Debug(export = true)
@Environment(EnvType.CLIENT)
@Mixin(value = PlayerMoodComponent.class, remap = false)
public class PlayerMoodComponentMixin {

	@Shadow
	@Final
	private HashMap<UUID, ItemStack> psychosisItems;

	@Inject(method = "clientTick", at = @At(value = "RETURN", ordinal = 0))
	private void clearPsychosisIfGameOver(CallbackInfo ci) {
		if (this.psychosisItems.isEmpty()) {
			return;
		}

		this.psychosisItems.clear();
	}
}
