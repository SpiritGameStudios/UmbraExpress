package dev.spiritstudios.umbra_express.init;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.duck.HitListWorldComponent;
import dev.spiritstudios.umbra_express.role.CustomShopEntry;
import dev.spiritstudios.umbra_express.role.MoneyManager;
import dev.spiritstudios.umbra_express.role.RoleReplacer;
import dev.spiritstudios.umbra_express.role.RoleReplacer.ReplacementQuotient;
import dev.spiritstudios.umbra_express.role.RoleReplacer.ReplacementPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static dev.doctor4t.trainmurdermystery.api.TMMRoles.CIVILIAN;
import static dev.doctor4t.trainmurdermystery.api.TMMRoles.KILLER;

@ApiStatus.NonExtendable
public interface UmbraExpressRoles {

	Map<Role, RoleAnnouncementTexts.RoleAnnouncementText> TEXTS = new HashMap<>();
	Map<Role, Function<PlayerEntity, List<ItemStack>>> ITEM_GIVERS = new HashMap<>();
	List<RoleReplacer> ROLE_REPLACEMENTS = new ArrayList<>();

    Role CONDUCTOR = registerInnocent("conductor", 0x7604E7, true);
	Role MYSTIC = registerInnocent("mystic", 0xE783D5, false);
	Role BARTENDER = registerInnocent("bartender", 0x3DE0AF, false);
	Role LOCKSMITH = registerInnocent("locksmith", 0xFFE447, false);
	Role ASSASSIN = registerKiller("assassin", 0x520b04);

    static Role registerInnocent(String path, int color, boolean canSeeTime) {
		return registerRole(path, color, true, false, Role.MoodType.REAL, GameConstants.getInTicks(0, 10), canSeeTime);
	}

    static Role registerKiller(String path, int color) {
		return registerRole(path, color, false, true, Role.MoodType.FAKE, -1, true);
	}

	static Role registerRole(String path, int color, boolean isInnocent, boolean canUseKiller, Role.MoodType moodType, int maxSprintTime, boolean canSeeTime) {
		Role role = new Role(UmbraExpress.id(path), color, isInnocent, canUseKiller, moodType, maxSprintTime, canSeeTime);
		TEXTS.put(role, registerText(path, color));

		return TMMRoles.registerRole(role);
	}

	static RoleAnnouncementTexts.RoleAnnouncementText registerText(String name, int color) {
		return RoleAnnouncementTexts.registerRoleAnnouncementText(new RoleAnnouncementTexts.RoleAnnouncementText(name, color));
	}

	static void registerReplacer(Role original, Role replacement, ReplacementQuotient replacementQuotient, ReplacementPredicate replacementPredicate) {
		ROLE_REPLACEMENTS.add(new RoleReplacer(original, replacement, replacementQuotient, replacementPredicate));
	}

	static boolean equals(RoleAnnouncementTexts.RoleAnnouncementText one, RoleAnnouncementTexts.RoleAnnouncementText two) {
		if (one == null || two == null) {
			return false;
		}
		return one.equals(two) ||
			one.colour == two.colour &&
			Objects.equals(one.roleText, two.roleText) &&
				Objects.equals(one.titleText, two.titleText) &&
				Objects.equals(one.welcomeText, two.welcomeText) &&
				Objects.equals(one.winText, two.winText);
	}

	static boolean fixWinCondition(Object left, Object right, Operation<Boolean> original) {
		if (original.call(left, right)) {
			return true;
		}

		if (!(left instanceof RoleAnnouncementTexts.RoleAnnouncementText text) || !(right instanceof RoleAnnouncementTexts.RoleAnnouncementText killer)) {
			return false;
		}

		Role role = null;
		for (Map.Entry<Role, RoleAnnouncementTexts.RoleAnnouncementText> entry : UmbraExpressRoles.TEXTS.entrySet()) {
			if (UmbraExpressRoles.equals(entry.getValue(), text)) {
				role = entry.getKey();
				break;
			}
		}
		return role != null && role.canUseKiller();
	}

    static void init() {
        registerReplacer(CIVILIAN, CONDUCTOR, ReplacementQuotient.ONE_OF, ReplacementPredicate.ALWAYS);
		registerReplacer(CIVILIAN, BARTENDER, ReplacementQuotient.ONE_OF, ReplacementPredicate.ALWAYS);
		registerReplacer(CIVILIAN, LOCKSMITH, ReplacementQuotient.ONE_OF, ReplacementPredicate.ALWAYS);
		registerReplacer(CIVILIAN, MYSTIC, ReplacementQuotient.ONE_OF, ReplacementPredicate.minPlayers(8));
		registerReplacer(KILLER, ASSASSIN, ReplacementQuotient.ALL_OF, ReplacementPredicate.fromRandom(0.5F));

		ITEM_GIVERS.put(LOCKSMITH, player -> List.of(UmbraExpressItems.MASTER_KEY.getDefaultStack()));

		MoneyManager.builder()
			.passiveTicker(10, 5)
			.addShopEntry(new CustomShopEntry(UmbraExpressItems.ANTIDOTE.getDefaultStack(), 150, UmbraExpress.id("gui/shop_slot_helpful")) {
				@Override
				public boolean onBuy(@NotNull PlayerEntity player) {
					return !player.getInventory().contains(this.stack()) && super.onBuy(player);
				}
			})
			.buildAndRegister(BARTENDER);

		MoneyManager.KILLER_DEFAULT.toBuilder()
			.passiveTicker(20, 5)
			.amountGainedPerKill(target -> {
				HitListWorldComponent hitlist = HitListWorldComponent.cast(GameWorldComponent.KEY.get(target.getWorld()));
				if (Objects.equals(target.getUuid(), hitlist.umbra_express$getTarget())) {
					return GameConstants.MONEY_PER_KILL * 2;
				}
				return GameConstants.MONEY_PER_KILL;
			})
			.buildAndRegister(ASSASSIN);
    }
}
