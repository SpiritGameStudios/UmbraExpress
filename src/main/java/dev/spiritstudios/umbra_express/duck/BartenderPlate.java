package dev.spiritstudios.umbra_express.duck;

import dev.doctor4t.trainmurdermystery.block_entity.BeveragePlateBlockEntity;

public interface BartenderPlate {

    default void umbra_express$setIsBartender(boolean bartender) {
        throw new UnsupportedOperationException("Duck interface");
    }

    default boolean umbra_express$isBartender() {
        throw new UnsupportedOperationException("Duck interface");
    }

    default void umbra_express$invokeSync() {
        throw new UnsupportedOperationException("Duck interface");
    }

    /**
     * avoids having to suppress the warning for every cast
     * @param blockEntity the block entity instance to cast
     * @return the bartender plate
     */
    static BartenderPlate cast(BeveragePlateBlockEntity blockEntity) {
        assert blockEntity instanceof BartenderPlate;
        return (BartenderPlate) blockEntity;
    }

}
