package dev.spiritstudios.umbra_express.role.task;

import com.chocohead.mm.api.ClassTinkerers;
import dev.doctor4t.trainmurdermystery.cca.PlayerMoodComponent;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class BroadcastTask implements PlayerMoodComponent.TrainTask {

	public static final String NAME = "broadcast";

	public static final PlayerMoodComponent.Task BROADCAST = ClassTinkerers.getEnum(PlayerMoodComponent.Task.class, "BROADCAST");

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
		return BROADCAST;
	}

	@Override
	public NbtCompound toNbt() {
		NbtCompound nbt = new NbtCompound();
		nbt.putString("type", NAME);
		return nbt;
	}
}
