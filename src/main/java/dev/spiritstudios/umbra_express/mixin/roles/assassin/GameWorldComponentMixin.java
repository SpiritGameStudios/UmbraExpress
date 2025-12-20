package dev.spiritstudios.umbra_express.mixin.roles.assassin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.doctor4t.wathe.api.Role;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.spiritstudios.umbra_express.duck.HitListWorldComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Debug(export = true)
@Mixin(value = GameWorldComponent.class, remap = false)
public abstract class GameWorldComponentMixin implements HitListWorldComponent {

	@Shadow
	@Final
	private World world;

	@Shadow
	public abstract Role getRole(PlayerEntity player);

	@Shadow
	public abstract void sync();

	@Shadow
	protected abstract NbtList nbtFromUuidList(List<UUID> list);

	@Shadow
	protected abstract ArrayList<UUID> uuidListFromNbt(NbtCompound nbtCompound, String listName);

	@Unique
	private final List<UUID> umbra_express$killedTargets = new ArrayList<>();

	@Nullable
	@Unique
	private UUID umbra_express$assassinationTarget;

	@Override
	public void umbra_express$rerollTarget() {
		List<? extends PlayerEntity> targets = this.world.getPlayers()
			.stream()
			.filter(player -> {
				if (!GameFunctions.isPlayerAliveAndSurvival(player)) {
					return false;
				}
				return !this.getRole(player).canUseKiller();
			}).toList();
		if (targets.isEmpty()) {
			this.umbra_express$assassinationTarget = null;
			sync();
			return;
		}
		this.umbra_express$assassinationTarget = Util.getRandom(targets, this.world.getRandom()).getUuid();
		sync();
	}

	@Override
	public UUID umbra_express$getTarget() {
		return this.umbra_express$assassinationTarget;
	}

	@Override
	public void umbra_express$addKilledTarget() {
		if (this.umbra_express$assassinationTarget == null) {
			return;
		}

		this.umbra_express$killedTargets.add(umbra_express$assassinationTarget);
	}

	@Override
	public List<UUID> umbra_express$getKilledTargets() {
		return this.umbra_express$killedTargets;
	}

	@Override
	public void umbra_express$reset() {
		this.umbra_express$killedTargets.clear();
		this.umbra_express$assassinationTarget = null;
	}

	@WrapMethod(method = "readFromNbt", remap = true)
	private void readAssassinationTarget(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup, Operation<Void> original) {
		original.call(nbt, wrapperLookup);
		if (nbt.containsUuid("umbra_express_assassinationTarget")) {
			this.umbra_express$assassinationTarget = nbt.getUuid("umbra_express_assassinationTarget");
		} else {
			this.umbra_express$assassinationTarget = null;
		}
		this.umbra_express$killedTargets.clear();
		if (nbt.contains("umbra_express_killedTargets")) {
			this.umbra_express$killedTargets.addAll(uuidListFromNbt(nbt, "umbra_express_killedTargets"));
		}
	}

	@WrapMethod(method = "writeToNbt", remap = true)
	private void writeAssassinationTarget(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup, Operation<Void> original) {
		original.call(nbt, wrapperLookup);
		if (this.umbra_express$assassinationTarget != null) {
			nbt.putUuid("umbra_express_assassinationTarget", this.umbra_express$assassinationTarget);
		}
		if (!this.umbra_express$killedTargets.isEmpty()) {
			nbt.put("umbra_express_killedTargets", nbtFromUuidList(this.umbra_express$killedTargets));
		}
	}
}
