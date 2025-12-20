package dev.spiritstudios.umbra_express.mixin.component.poison;

import dev.doctor4t.wathe.index.WatheDataComponentTypes;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Mixin(targets = "net.minecraft.component.ComponentChanges$1")
public class ComponentChangesPacketCodecMixin {

	@ModifyVariable(method = "encode(Lnet/minecraft/network/RegistryByteBuf;Lnet/minecraft/component/ComponentChanges;)V", at = @At("HEAD"), index = 2, argsOnly = true)
	private ComponentChanges removePoison(ComponentChanges original) {
		ComponentChanges.Builder builder = ComponentChanges.builder();
		for (Map.Entry<ComponentType<?>, Optional<?>> entry : original.entrySet()) {
			ComponentType<?> type = entry.getKey();
			if (Objects.equals(WatheDataComponentTypes.POISONER, type)) {
				continue;
			}
			entry.getValue()
				.ifPresentOrElse(value -> {
					//noinspection unchecked, rawtypes
					builder.add((ComponentType) type, value);
				},
				() -> builder.remove(type)
			);
		}
		return builder.build();
	}
}
