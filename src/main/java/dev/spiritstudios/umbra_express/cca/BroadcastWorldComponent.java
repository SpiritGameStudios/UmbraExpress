package dev.spiritstudios.umbra_express.cca;

import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.init.UmbraExpressConfig;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Colors;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

import java.util.UUID;

public class BroadcastWorldComponent extends CooldownWorldComponent {

    public static final ComponentKey<BroadcastWorldComponent> KEY = ComponentRegistry.getOrCreate(UmbraExpress.id("broadcast"), BroadcastWorldComponent.class);

    public static final int COOLDOWN_MULTIPLIER = 5;

    protected boolean broadcasting = false;
    protected int announcementTicks;
    protected UUID announcerUuid = null;

    public BroadcastWorldComponent(World world) {
        super(world);
		this.announcementTicks = maxBroadcastTicks();
    }

    @Override
    public void serverTick() {
        if (broadcasting) {
            // decrement countdown if broadcasting, when countdown = 0, stop
            if (announcementTicks > 0) {
                announcementTicks--;
                this.markDirty();
            } else {
                this.setBroadcasting(false);
            }
        } else {
            // if not broadcasting, decrement cooldown
            if (this.cooldown > 0) {
                this.cooldown--;
                this.markDirty();
            } else {
                this.cooldown = 0;
                this.announcementTicks = maxBroadcastTicks();
            }
        }

        if (this.world.getTime() % 200 == 0) {
            this.markDirty();
        }

		this.syncIfDirty();
	}

	@Override
	protected void sync() {
		KEY.sync(this.world);
	}

    public int getRenderColor() {
        if (this.isOnCooldown()) {
            return Colors.RED;
        }
        if (this.broadcasting) {
            return Colors.GREEN;
        }
        return Colors.WHITE;
    }

	@Override
    public int getTicksForRendering() {
        return this.isOnCooldown() ? this.cooldown : this.announcementTicks;
    }

    public void setBroadcasting(boolean broadcasting) {
        if (this.broadcasting == broadcasting) {
            return;
        }

        if (broadcasting) {
            if (this.isOnCooldown()) {
                return;
            }
            cooldown = 0;
        } else {
            cooldown = (maxBroadcastTicks() - announcementTicks) * getCooldownMultiplier(this.world);
            announcementTicks = 0;
        }

        this.broadcasting = broadcasting;
        this.markDirty();
    }

	@Override
    public void reset() {
        this.broadcasting = false;
        this.announcementTicks = maxBroadcastTicks();
        this.announcerUuid = null;
        super.reset();
    }

    public static int getCooldownMultiplier(World world) {
        return GameWorldComponent.KEY.get(world).isRunning() ? COOLDOWN_MULTIPLIER : 0;
    }

    public void setAnnouncerUuid(UUID uuid) {
        this.announcerUuid = uuid;
    }

    public UUID getAnnouncerUuid() {
        return this.announcerUuid;
    }

    public boolean isBroadcasting() {
        return this.broadcasting;
    }

	public int maxBroadcastTicks() {
		return UmbraExpressConfig.maxBroadcastTicks(this.world);
	}

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
        this.broadcasting = tag.contains("broadcasting") && tag.getBoolean("broadcasting");
        this.announcementTicks = tag.contains("announcementTicks") ? tag.getInt("announcementTicks") : 0;
        this.announcerUuid = tag.containsUuid("announcerUuid") ? tag.getUuid("announcerUuid") : null;
		super.readFromNbt(tag, registryLookup);
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
        tag.putBoolean("broadcasting", this.broadcasting);
        tag.putInt("announcementTicks", this.announcementTicks);
        if (this.announcerUuid != null) {
            tag.putUuid("announcerUuid", this.announcerUuid);
        }
		super.writeToNbt(tag, registryLookup);
    }
}
