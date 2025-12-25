package dev.spiritstudios.umbra_express.asm;

import com.chocohead.mm.api.ClassTinkerers;
import dev.spiritstudios.umbra_express.role.task.BroadcastTask;

import java.util.function.Function;

public class PlayerMoodComponentTaskEarlyRiser implements Runnable {
	@Override
	public void run() {
		final String className = "dev.doctor4t.trainmurdermystery.cca.PlayerMoodComponent$Task";
		ClassTinkerers.enumBuilder(className, Function.class)
			.addEnum("BROADCAST", (Function) nbt -> new BroadcastTask())
			.build();
	}
}
