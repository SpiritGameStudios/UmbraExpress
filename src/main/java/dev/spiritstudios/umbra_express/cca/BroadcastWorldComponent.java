package dev.spiritstudios.umbra_express.cca;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.init.UmbraExpressConfig;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Colors;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.UUID;

public class BroadcastWorldComponent implements ServerTickingComponent, AutoSyncedComponent {

    public static final ComponentKey<BroadcastWorldComponent> KEY = ComponentRegistry.getOrCreate(UmbraExpress.id("broadcast"), BroadcastWorldComponent.class);

    public static final int COOLDOWN_MULTIPLIER = 5;

    protected boolean broadcasting = false;
    protected int announcementTicks;
    protected int cooldown = 0;
    protected UUID announcerUuid = null;

    protected final World world;

    protected boolean dirty = false;

    public BroadcastWorldComponent(World world) {
        this.world = world;
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

        if (this.dirty) {
            KEY.sync(this.world);
        }
    }

    public void markDirty() {
        this.dirty = true;
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

    public boolean isOnCooldown() {
        return this.cooldown > 0;
    }

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

    public void reset() {
        this.broadcasting = false;
        this.announcementTicks = maxBroadcastTicks();
        this.cooldown = 0;
        this.announcerUuid = null;
        this.markDirty();
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
        this.cooldown = tag.contains("cooldown") ? tag.getInt("cooldown") : 0;
        this.announcerUuid = tag.containsUuid("announcerUuid") ? tag.getUuid("announcerUuid") : null;
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
        tag.putBoolean("broadcasting", this.broadcasting);
        tag.putInt("announcementTicks", this.announcementTicks);
        tag.putInt("cooldown", this.cooldown);
        if (this.announcerUuid != null) {
            tag.putUuid("announcerUuid", this.announcerUuid);
        }
    }
}
