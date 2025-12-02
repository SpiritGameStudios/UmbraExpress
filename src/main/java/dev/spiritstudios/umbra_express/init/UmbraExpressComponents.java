package dev.spiritstudios.umbra_express.init;

import dev.spiritstudios.umbra_express.cca.ApparitionViewerComponent;
import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;

public class UmbraExpressComponents implements WorldComponentInitializer, EntityComponentInitializer {

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(BroadcastWorldComponent.KEY, BroadcastWorldComponent::new);
    }

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, ApparitionViewerComponent.KEY).respawnStrategy(RespawnCopyStrategy.CHARACTER).end(ApparitionViewerComponent::new);
    }

}
