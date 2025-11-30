package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.role.RoleReplacer;
import dev.spiritstudios.umbra_express.role.RoleReplacer.PlayerNumbers;
import dev.spiritstudios.umbra_express.role.RoleReplacer.ReplacementChecker;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

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

    Role CONDUCTOR = registerInnocent(UmbraExpress.id("conductor"), 0x7604E7, true);
	Role BARTENDER = registerInnocent(UmbraExpress.id("bartender"), 0x7604E7, false);
	Role LOCKSMITH = registerInnocent(UmbraExpress.id("locksmith"), 0x7604E7, false);
	Role ASSASSIN = registerKiller(UmbraExpress.id("assassin"), 0x520b04);

	static Role registerInnocent(Identifier id, int color, boolean canSeeTime) {
		return registerRole(new Role(id, color, true, false, Role.MoodType.REAL, GameConstants.getInTicks(0, 10), canSeeTime));
	}

	static Role registerKiller(Identifier id, int color) {
		return registerRole(new Role(id, color, false, true, Role.MoodType.FAKE, -1, true));
	}

	static Role registerRole(Role role) {
		RoleAnnouncementTexts.RoleAnnouncementText text = registerText(role.identifier().getPath(), role.color());
		TEXTS.put(role, text);
		return TMMRoles.registerRole(role);
	}

	static RoleAnnouncementTexts.RoleAnnouncementText registerText(String name, int color) {
		return RoleAnnouncementTexts.registerRoleAnnouncementText(new RoleAnnouncementTexts.RoleAnnouncementText(name, color));
	}

	static void registerReplacer(Role original, Role replacement, PlayerNumbers playerNumbers, ReplacementChecker replacementChecker) {
		ROLE_REPLACEMENTS.add(new RoleReplacer(original, replacement, playerNumbers, replacementChecker));
	}

    static void init() {
        registerReplacer(CIVILIAN, CONDUCTOR, PlayerNumbers.ONE, ReplacementChecker.ALWAYS);
		registerReplacer(CIVILIAN, BARTENDER, PlayerNumbers.ONE, ReplacementChecker.ALWAYS);
		registerReplacer(CIVILIAN, LOCKSMITH, PlayerNumbers.ONE, ReplacementChecker.ALWAYS);
		registerReplacer(KILLER, ASSASSIN, PlayerNumbers.ONE, ReplacementChecker.fromRandom(0.5F));
    }
}
