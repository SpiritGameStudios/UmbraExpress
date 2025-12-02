package dev.spiritstudios.umbra_express.init;

import dev.spiritstudios.umbra_express.cca.BroadcastWorldComponent;
import org.ladysnake.cca.api.v3.world.WorldComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.world.WorldComponentInitializer;

public class UmbraExpressWorldComponents implements WorldComponentInitializer {

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(BroadcastWorldComponent.KEY, BroadcastWorldComponent::new);
    }
}
