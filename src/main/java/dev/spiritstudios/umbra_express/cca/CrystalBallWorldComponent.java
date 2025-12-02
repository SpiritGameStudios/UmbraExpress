package dev.spiritstudios.umbra_express.cca;

import dev.spiritstudios.umbra_express.UmbraExpress;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class CrystalBallWorldComponent extends CooldownWorldComponent {

	public static final ComponentKey<CrystalBallWorldComponent> KEY = ComponentRegistry.getOrCreate(UmbraExpress.id("crystal_ball"), CrystalBallWorldComponent.class);

	public CrystalBallWorldComponent(World world) {
		super(world);
	}

	public void setCooldownTicks(int cooldownTicks) {
		this.cooldown = cooldownTicks;
	}

	@Override
	protected void sync() {
		KEY.sync(this.world);
	}
}
