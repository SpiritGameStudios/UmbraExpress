package dev.spiritstudios.umbra_express.cca;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class CrystalBallWorldComponent extends CooldownWorldComponent {

	public static final ComponentKey<CrystalBallWorldComponent> KEY = ComponentRegistry.getOrCreate(UmbraExpress.id("crystal_ball"), CrystalBallWorldComponent.class);

	private static final String CAN_VIEW_KEY = "can_view";

	protected boolean canViewInLobby = true;

	public CrystalBallWorldComponent(World world) {
		super(world);
	}

	public void setCooldownTicks(int cooldownTicks) {
		this.cooldown = cooldownTicks;
		this.markDirty();
	}

	public void setCanView(boolean canViewInLobby) {
		this.canViewInLobby = canViewInLobby;
		this.markDirty();
	}

	public boolean canView(PlayerEntity viewer) {
		return this.canView(GameWorldComponent.KEY.get(this.world), viewer);
	}

	public boolean canView(GameWorldComponent game, PlayerEntity viewer) {
		return game.isRunning() ? game.getRole(viewer).equals(UmbraExpressRoles.MYSTIC) : this.canViewInLobby;
	}

	public boolean canViewInLobby() {
		return this.canViewInLobby;
	}

	@Override
	public void writeToNbt(NbtCompound nbt, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
		nbt.putBoolean(CAN_VIEW_KEY, this.canViewInLobby);
		super.writeToNbt(nbt, registryLookup);
	}

	@Override
	public void readFromNbt(NbtCompound nbt, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
		this.canViewInLobby = nbt.contains(CAN_VIEW_KEY) && nbt.getBoolean(CAN_VIEW_KEY);
		super.readFromNbt(nbt, registryLookup);
	}

	@Override
	protected void sync() {
		KEY.sync(this.world);
	}
}
