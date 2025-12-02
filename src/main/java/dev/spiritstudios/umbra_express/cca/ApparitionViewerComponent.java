package dev.spiritstudios.umbra_express.cca;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.init.UmbraExpressRoles;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class ApparitionViewerComponent implements AutoSyncedComponent {

    private static final String CAN_VIEW_KEY = "can_view";
    public static final ComponentKey<ApparitionViewerComponent> KEY = ComponentRegistry.getOrCreate(UmbraExpress.id("apparition_viewer"), ApparitionViewerComponent.class);

    private boolean canView;
    private final PlayerEntity viewer;

    public ApparitionViewerComponent(PlayerEntity viewer) {
        this.viewer = viewer;
    }

    public boolean canView() {
        return this.canView;
    }

    public void setCanView(GameWorldComponent game, boolean gameruleEnabled) {
        this.setCanView(game.isRunning() ? game.getRole(this.viewer).equals(UmbraExpressRoles.MYSTIC) : gameruleEnabled);
    }

    public void setCanView(boolean canView) {
        this.canView = canView;
        this.sync();
    }

    @Override
    public void readFromNbt(NbtCompound tag, @NotNull RegistryWrapper.WrapperLookup registryLookup) {
        this.canView = tag.contains(CAN_VIEW_KEY) && tag.getBoolean(CAN_VIEW_KEY);
    }

    @Override
    public void writeToNbt(NbtCompound tag, @NotNull RegistryWrapper.WrapperLookup registryLookup) {
        tag.putBoolean(CAN_VIEW_KEY, this.canView);
    }

    public void sync() {
        KEY.sync(this.viewer);
    }

}
