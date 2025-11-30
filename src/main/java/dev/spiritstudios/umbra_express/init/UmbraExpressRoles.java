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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.doctor4t.trainmurdermystery.api.TMMRoles.CIVILIAN;
import static dev.doctor4t.trainmurdermystery.api.TMMRoles.KILLER;

public class UmbraExpressRoles {

	public static final List<Role> ROLES = new ArrayList<>();
	public static final Map<Role, RoleAnnouncementTexts.RoleAnnouncementText> TEXTS = new HashMap<>();
	public static final List<RoleReplacer> ROLE_REPLACEMENTS = new ArrayList<>();

    public static final Role CONDUCTOR = registerInnocent(UmbraExpress.id("conductor"), 0x7604E7, true);
	public static final Role BARTENDER = registerInnocent(UmbraExpress.id("bartender"), 0x7604E7, false);
	public static final Role LOCKSMITH = registerInnocent(UmbraExpress.id("locksmith"), 0x7604E7, false);
	public static final Role ASSASSIN = registerKiller(UmbraExpress.id("assassin"), 0x520b04);

	public static Role registerInnocent(Identifier id, int color, boolean canSeeTime) {
		return registerRole(new Role(id, color, true, false, Role.MoodType.REAL, GameConstants.getInTicks(0, 10), canSeeTime));
	}

	public static Role registerKiller(Identifier id, int color) {
		return registerRole(new Role(id, color, false, true, Role.MoodType.FAKE, -1, true));
	}

	public static Role registerRole(Role role) {
		RoleAnnouncementTexts.RoleAnnouncementText text = registerText(role.identifier().getPath(), role.color());
		TEXTS.put(role, text);
		return TMMRoles.registerRole(role);
	}

	public static RoleAnnouncementTexts.RoleAnnouncementText registerText(String name, int color) {
		return RoleAnnouncementTexts.registerRoleAnnouncementText(new RoleAnnouncementTexts.RoleAnnouncementText(name, color));
	}

	public static void registerReplacer(Role original, Role replacement, PlayerNumbers playerNumbers, ReplacementChecker replacementChecker) {
		ROLE_REPLACEMENTS.add(new RoleReplacer(original, replacement, playerNumbers, replacementChecker));
	}

    public static void init() {
        registerReplacer(CIVILIAN, CONDUCTOR, PlayerNumbers.ONE, ReplacementChecker.ALWAYS);
		registerReplacer(CIVILIAN, BARTENDER, PlayerNumbers.ONE, ReplacementChecker.ALWAYS);
		registerReplacer(CIVILIAN, LOCKSMITH, PlayerNumbers.ONE, ReplacementChecker.ALWAYS);
		registerReplacer(KILLER, ASSASSIN, PlayerNumbers.ONE , ReplacementChecker.fromRandom(0.5F));
    }
}
