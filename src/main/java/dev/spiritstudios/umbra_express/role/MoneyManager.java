package dev.spiritstudios.umbra_express.role;

import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.api.WatheRoles;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.util.ShopEntry;
import net.minecraft.SharedConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public record MoneyManager(PassiveTicker passiveTicker, Function<PlayerEntity, Integer> amountGainedPerKill, int startingAmount, List<ShopEntry> shop) {

    public static final MoneyManager KILLER_DEFAULT = new MoneyManager(PassiveTicker.KILLER_DEFAULT, target -> GameConstants.MONEY_PER_KILL, GameConstants.MONEY_START, GameConstants.SHOP_ENTRIES);
    public static final Map<Role, MoneyManager> ROLE_MAP = Util.make(new HashMap<>(), map -> map.put(WatheRoles.KILLER, KILLER_DEFAULT));

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

    public static MoneyManager register(Role role, MoneyManager moneyManager) {
        return ROLE_MAP.put(role, moneyManager);
    }

    /**
     * Call this method from onInitialize() to apply changes to an existing money manager, e.g. to add items to the base killer's shop.
     * @param role The role whose money manager is being changed.
     * @param operation The function applied onto a {@link Builder} instance of the role's money manager.
     */
    @SuppressWarnings("unused")
    public static MoneyManager compute(Role role, UnaryOperator<Builder> operation) {
        return ROLE_MAP.computeIfPresent(role, (role1, moneyManager) -> operation.apply(moneyManager.toBuilder()).build());
    }

    @SuppressWarnings("unused")
    public static class Builder {

        private PassiveTicker passiveTicker = time -> 0;
        private Function<PlayerEntity, Integer> amountGainedPerKill = target -> 0;
        private int startingAmount = 0;
        private final List<ShopEntry> shop = new ArrayList<>(List.of());

        public Builder passiveTicker(int tickTimeSecs, int amountGainedPerTick) {
            return this.passiveTicker(PassiveTicker.of(tickTimeSecs, amountGainedPerTick));
        }

        public Builder passiveTicker(PassiveTicker passiveTicker) {
            this.passiveTicker = passiveTicker;
            return this;
        }

		public Builder amountGainedPerKill(int amount) {
			return this.amountGainedPerKill(target -> amount);
		}

        public Builder amountGainedPerKill(Function<PlayerEntity, Integer> amountGainedPerKill) {
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

        public MoneyManager build() {
            return new MoneyManager(this.passiveTicker, this.amountGainedPerKill, this.startingAmount, this.shop);
        }

        public MoneyManager buildAndRegister(Role role) {
            return register(role, this.build());
        }

    }

    public interface PassiveTicker extends Function<Long, Integer> {

        PassiveTicker KILLER_DEFAULT = of(10, 5);

        static PassiveTicker of(int tickTimeSecs, int amountGainedPerTick) {
            return time -> time % ((long) tickTimeSecs * SharedConstants.TICKS_PER_SECOND) == 0 ? amountGainedPerTick : 0;
        }
    }
}
