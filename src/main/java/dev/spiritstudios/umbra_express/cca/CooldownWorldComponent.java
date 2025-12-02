package dev.spiritstudios.umbra_express.cca;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public abstract class CooldownWorldComponent implements ServerTickingComponent, AutoSyncedComponent {

	protected int cooldown = 0;

	protected final World world;

	protected boolean dirty = false;

	public CooldownWorldComponent(World world) {
		this.world = world;
	}

	@Override
	public void serverTick() {
		if (this.cooldown > 0) {
			this.cooldown--;
			this.markDirty();
		}

		if (this.world.getTime() % 200 == 0) {
			this.markDirty();
		}

		this.syncIfDirty();
	}

	protected final void syncIfDirty() {
		if (this.dirty) {
			this.dirty = false;
			sync();
		}
	}

	protected void sync() {
	}

	public void markDirty() {
		this.dirty = true;
	}

	public boolean isOnCooldown() {
		return this.cooldown > 0;
	}

	public int getTicksForRendering() {
		return this.cooldown;
	}

	public void reset() {
		this.cooldown = 0;
		this.markDirty();
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
		this.cooldown = tag.contains("cooldown") ? tag.getInt("cooldown") : 0;
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
		tag.putInt("cooldown", this.cooldown);
	}

	public static void resetAll(World world) {
		BroadcastWorldComponent.KEY.get(world).reset();
		CrystalBallWorldComponent.KEY.get(world).reset();
	}
}
