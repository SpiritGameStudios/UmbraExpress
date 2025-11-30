package dev.spiritstudios.umbra_express.init;

import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.cca.PlayerMoodComponent;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class UmbraExpressRoles {

    public static final Role CONDUCTOR = TMMRoles.registerRole(
            new Role(
                    UmbraExpress.id("conductor"),
                    0x5E0DE0,
                    true,
                    false,
                    Role.MoodType.REAL,
                    GameConstants.getInTicks(0, 10),
                    true
            )
    );

    public static final RoleAnnouncementTexts.RoleAnnouncementText CONDUCTOR_TEXT = RoleAnnouncementTexts.registerRoleAnnouncementText(
            new RoleAnnouncementTexts.RoleAnnouncementText("conductor", 0x5E0DE0)
    );

    public static void init() {
        // NO-OP
    }

    public static class BroadcastTask implements PlayerMoodComponent.TrainTask {

        public static final String NAME = "broadcast";

        public static PlayerMoodComponent.Task taskType = null;

        @Override
        public boolean isFulfilled(PlayerEntity player) {
            BroadcastWorldComponent broadcast = BroadcastWorldComponent.KEY.get(player.getWorld());
            return broadcast.isBroadcasting() || broadcast.isOnCooldown();
        }

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public PlayerMoodComponent.Task getType() {
            return taskType;
        }

        @Override
        public NbtCompound toNbt() {
            NbtCompound nbt = new NbtCompound();
            nbt.putString("type", NAME);
            return nbt;
        }
    }
}
