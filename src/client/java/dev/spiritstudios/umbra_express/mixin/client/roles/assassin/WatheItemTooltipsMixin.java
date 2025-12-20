package dev.spiritstudios.umbra_express.mixin.client.roles.assassin;

import dev.doctor4t.ratatouille.util.TextUtils;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.client.util.WatheItemTooltips;
import dev.doctor4t.wathe.index.WatheItems;
import dev.spiritstudios.umbra_express.duck.HitListWorldComponent;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(value = WatheItemTooltips.class, remap = false)
public abstract class WatheItemTooltipsMixin {

	// begin credit: color hex codes taken from old TMM hitlist
	@Unique
	private static final int umbra_express$KILL_COLOR = 0x8A1B29;
	@Unique
	private static final int umbra_express$SUCCESS_COLOR = 0x1B8943;
	// end credit

	@Inject(method = "lambda$addTooltips$0", at = @At("RETURN"), remap = true)
	private static void appendHitlist(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> tooltipList, CallbackInfo ci) {
		if (!itemStack.isOf(WatheItems.LETTER)) {
			return;
		}

		if (WatheClient.gameComponent == null) {
			return;
		}

		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null) {
			return;
		}

		if (!WatheClient.gameComponent.isRole(player, UmbraExpressRoles.ASSASSIN)) {
			return;
		}

		HitListWorldComponent hitList = HitListWorldComponent.cast(WatheClient.gameComponent);

		String hitlistTranslationKey = TextUtils.getItemTranslationKey(WatheItems.LETTER) + ".hitlist";

		tooltipList.add(Text.translatable(hitlistTranslationKey + ".premise0"));
		tooltipList.add(Text.translatable(hitlistTranslationKey + ".premise1").withColor(umbra_express$KILL_COLOR));
		tooltipList.add(Text.translatable(hitlistTranslationKey + ".premise2"));
		tooltipList.add(Text.translatable(hitlistTranslationKey + ".premise3"));
		tooltipList.add(Text.translatable(hitlistTranslationKey + ".premise4"));
		tooltipList.add(Text.translatable(hitlistTranslationKey + ".premise5"));
		Text keybind = WatheClient.instinctKeybind.getBoundKeyLocalizedText().copy().styled(style -> style.withFormatting(Formatting.GOLD));
		tooltipList.add(Text.stringifiedTranslatable(hitlistTranslationKey + ".premise6", keybind));

		for (UUID uuid : hitList.umbra_express$getKilledTargets()) {
			umbra_express$appendUuid(hitlistTranslationKey, uuid, tooltipList::add, true);
		}
		UUID current = hitList.umbra_express$getTarget();
		if (current == null) {
			tooltipList.add(Text.translatable(hitlistTranslationKey + ".absent").withColor(umbra_express$SUCCESS_COLOR));
		} else {
			umbra_express$appendUuid(hitlistTranslationKey, current, tooltipList::add, false);
		}
	}

	@Unique
	private static void umbra_express$appendUuid(String hitlistTranslationKey, UUID uuid, Consumer<Text> tooltipAppender, boolean dead) {
		PlayerListEntry entry = WatheClient.PLAYER_ENTRIES_CACHE.get(uuid);
		if (entry == null) {
			return;
		}

		Text text = Text.stringifiedTranslatable(
			hitlistTranslationKey + ".target",
			entry.getProfile().getName()
		// begin credit: color hex codes taken from old TMM hitlist
		).styled(style -> dead ? style.withStrikethrough(true).withColor(umbra_express$SUCCESS_COLOR) : style.withColor(umbra_express$KILL_COLOR));
		// end credit
		tooltipAppender.accept(text);
	}
}
