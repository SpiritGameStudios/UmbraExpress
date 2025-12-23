package dev.spiritstudios.umbra_express.init;

import dev.spiritstudios.umbra_express.UmbraExpress;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface UmbraExpressTags {

	TagKey<Block> HAUNTING_INTERACTABLE = TagKey.of(RegistryKeys.BLOCK, UmbraExpress.id("haunting_interactable"));
}
