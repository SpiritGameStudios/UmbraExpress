package dev.spiritstudios.umbra_express.role;

import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.util.ShopEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record MoneyMaker(PassiveTicker passiveTicker, int amountGainedPerKill, int startingAmount, List<ShopEntry> shop) {

    public static final MoneyMaker KILLER_DEFAULT = new MoneyMaker(PassiveTicker.KILLER_DEFAULT, GameConstants.MONEY_PER_KILL, GameConstants.MONEY_START, GameConstants.SHOP_ENTRIES);

    public Builder toBuilder() {
        return builder()
            .passiveTicker(this.passiveTicker)
            .amountGainedPerKill(this.amountGainedPerKill)
            .startingAmount(this.startingAmount)
            .addShopEntries(this.shop);
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("unused")
    public static class Builder {

        private PassiveTicker passiveTicker = time -> 0;
        private int amountGainedPerKill = 0;
        private int startingAmount = 0;
        private final List<ShopEntry> shop = new ArrayList<>(List.of());

        public Builder passiveTicker(int tickTimeSecs, int amountGainedPerTick) {
            return this.passiveTicker(PassiveTicker.of(tickTimeSecs, amountGainedPerTick));
        }

        public Builder passiveTicker(PassiveTicker passiveTicker) {
            this.passiveTicker = passiveTicker;
            return this;
        }

        public Builder amountGainedPerKill(int amountGainedPerKill) {
            this.amountGainedPerKill = amountGainedPerKill;
            return this;
        }

        public Builder startingAmount(int startingAmount) {
            this.startingAmount = startingAmount;
            return this;
        }

        public Builder addShopEntry(ShopEntry shopEntry) {
            this.shop.add(shopEntry);
            return this;
        }

        public Builder addShopEntries(List<ShopEntry> shopEntries) {
            this.shop.addAll(shopEntries);
            return this;
        }

        public MoneyMaker build() {
            return new MoneyMaker(this.passiveTicker, this.amountGainedPerKill, this.startingAmount, this.shop);
        }

    }

    public interface PassiveTicker extends Function<Long, Integer> {

        PassiveTicker KILLER_DEFAULT = of(10, 5);

        static PassiveTicker of(int tickTimeSecs, int amountGainedPerTick) {
            return time -> time % (tickTimeSecs * 20L) == 0 ? amountGainedPerTick : 0;
        }

    }

}
