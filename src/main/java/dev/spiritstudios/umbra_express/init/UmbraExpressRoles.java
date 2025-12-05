package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.util.ShopEntry;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.role.MoneyMaker;
import dev.spiritstudios.umbra_express.role.RoleReplacer;
import dev.spiritstudios.umbra_express.role.RoleReplacer.ReplacementQuotient;
import dev.spiritstudios.umbra_express.role.RoleReplacer.ReplacementPredicate;
import net.minecraft.util.Util;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.doctor4t.trainmurdermystery.api.TMMRoles.CIVILIAN;
import static dev.doctor4t.trainmurdermystery.api.TMMRoles.KILLER;

@ApiStatus.NonExtendable
public interface UmbraExpressRoles {

	Map<Role, RoleAnnouncementTexts.RoleAnnouncementText> TEXTS = new HashMap<>();
	List<RoleReplacer> ROLE_REPLACEMENTS = new ArrayList<>();
	Map<Role, MoneyMaker> MONEY_MAKERS = Util.make(new HashMap<>(), map -> map.put(KILLER, MoneyMaker.KILLER_DEFAULT));

    Role CONDUCTOR = registerInnocent("conductor", 0x7604E7, true, null);
	Role MYSTIC = registerInnocent("mystic", 0xE783D5, false, null);
	Role BARTENDER = registerInnocent("bartender", 0x3DE0AF, false, MoneyMaker.builder()
		.passiveTicker(60, 20)
		.addShopEntry(new ShopEntry(TMMItems.COSMOPOLITAN.getDefaultStack(), 20, ShopEntry.Type.POISON))
		.addShopEntry(new ShopEntry(TMMItems.MOJITO.getDefaultStack(), 20, ShopEntry.Type.POISON))
		.addShopEntry(new ShopEntry(TMMItems.CHAMPAGNE.getDefaultStack(), 20, ShopEntry.Type.POISON))
		.addShopEntry(new ShopEntry(TMMItems.OLD_FASHIONED.getDefaultStack(), 20, ShopEntry.Type.POISON))
		.addShopEntry(new ShopEntry(TMMItems.MARTINI.getDefaultStack(), 20, ShopEntry.Type.POISON))
		.addShopEntry(new ShopEntry(UmbraExpressItems.ANTIDOTE.getDefaultStack(), 100, ShopEntry.Type.TOOL))
		.build()
	);
	Role LOCKSMITH = registerInnocent("locksmith", 0xFFE447, false, null);
	Role ASSASSIN = registerKiller("assassin", 0x520b04, MoneyMaker.KILLER_DEFAULT.toBuilder()
		.passiveTicker(20, 5)
		.amountGainedPerKill(200)
		.build()
	);

	static Role registerInnocent(String path, int color, boolean canSeeTime, @Nullable MoneyMaker moneyMaker) {
		return registerRole(path, color, true, false, Role.MoodType.REAL, GameConstants.getInTicks(0, 10), canSeeTime, moneyMaker);
	}

    static Role registerKiller(String path, int color, @Nullable MoneyMaker moneyMaker) {
		return registerRole(path, color, false, true, Role.MoodType.FAKE, -1, true, moneyMaker);
	}

	static Role registerRole(String path, int color, boolean isInnocent, boolean canUseKiller, Role.MoodType moodType, int maxSprintTime, boolean canSeeTime, @Nullable MoneyMaker moneyMaker) {
		Role role = new Role(UmbraExpress.id(path), color, isInnocent, canUseKiller, moodType, maxSprintTime, canSeeTime);
		TEXTS.put(role, registerText(path, color));

		if (moneyMaker != null)
			MONEY_MAKERS.put(role, moneyMaker);

		return TMMRoles.registerRole(role);
	}

	static RoleAnnouncementTexts.RoleAnnouncementText registerText(String name, int color) {
		return RoleAnnouncementTexts.registerRoleAnnouncementText(new RoleAnnouncementTexts.RoleAnnouncementText(name, color));
	}

	static void registerReplacer(Role original, Role replacement, ReplacementQuotient replacementQuotient, ReplacementPredicate replacementPredicate) {
		ROLE_REPLACEMENTS.add(new RoleReplacer(original, replacement, replacementQuotient, replacementPredicate));
	}

    static void init() {
        registerReplacer(CIVILIAN, CONDUCTOR, ReplacementQuotient.ONE_OF, ReplacementPredicate.ALWAYS);
		registerReplacer(CIVILIAN, BARTENDER, ReplacementQuotient.ONE_OF, ReplacementPredicate.ALWAYS);
		registerReplacer(CIVILIAN, LOCKSMITH, ReplacementQuotient.ONE_OF, ReplacementPredicate.ALWAYS);
		registerReplacer(CIVILIAN, MYSTIC, ReplacementQuotient.ONE_OF, ReplacementPredicate.minPlayers(8));
		registerReplacer(KILLER, ASSASSIN, ReplacementQuotient.ALL_OF, ReplacementPredicate.fromRandom(0.5F));
    }
}
