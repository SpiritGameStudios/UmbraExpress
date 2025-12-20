package dev.spiritstudios.umbra_express.role.task;

import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class BroadcastTask implements PlayerMoodComponent.TrainTask {

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
