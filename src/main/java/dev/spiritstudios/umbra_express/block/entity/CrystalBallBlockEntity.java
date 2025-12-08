package dev.spiritstudios.umbra_express.block.entity;

import dev.doctor4t.trainmurdermystery.block_entity.SyncingBlockEntity;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.spiritstudios.umbra_express.cca.CrystalBallWorldComponent;
import dev.spiritstudios.umbra_express.init.UmbraExpressBlockEntities;
import dev.spiritstudios.umbra_express.init.UmbraExpressConfig;
import dev.spiritstudios.umbra_express.init.UmbraExpressSoundEvents;
import dev.spiritstudios.umbra_express.network.PlaySoundInUIS2CPayload;
import dev.spiritstudios.umbra_express.network.WiggleCrystalBallCooldownHudS2CPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author axialeaa
 */
public class CrystalBallBlockEntity extends SyncingBlockEntity {

    private static final String APPARITION_KEY = "apparition";
    private static final String APPARITION_TICKS_KEY = "apparition_ticks";

    public static final int MAX_APPARITION_RENDER_TICKS = GameConstants.getInTicks(0, 8);
    public static final int MIN_COOLDOWN_TICKS = GameConstants.getInTicks(0, 10);

    private static final float SOUND_PITCH_DEVIATION = 0.1F;

    @Nullable
    public PlayerEntity apparition;
    @Nullable
    public UUID apparitionUUID;

    public int apparitionTicks;

    public CrystalBallBlockEntity(BlockPos pos, BlockState state) {
        super(UmbraExpressBlockEntities.CRYSTAL_BALL, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (this.apparitionUUID != null)
            nbt.putUuid(APPARITION_KEY, this.apparitionUUID);

        nbt.putInt(APPARITION_TICKS_KEY, this.apparitionTicks);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		this.apparitionUUID = nbt.contains(APPARITION_KEY) ? nbt.getUuid(APPARITION_KEY) : null;
		this.apparitionTicks = nbt.contains(APPARITION_TICKS_KEY) ? nbt.getInt(APPARITION_TICKS_KEY) : 0;
    }

    public void tick(World world) {
        if (this.isRevealingApparition()) {
            this.apparitionTicks--;
            this.markDirty();
        }

        this.apparition = this.apparitionUUID == null ? null : world.getPlayerByUuid(this.apparitionUUID);
    }

    public boolean isRevealingApparition() {
        return this.apparitionTicks > 0;
    }

    public void onReveal(ServerWorld world, Random random, ServerPlayerEntity apparition, ServerPlayerEntity mystic, boolean gameRunning) {
        // this.apparition = apparition;
        this.apparitionUUID = apparition.getUuid();
        this.apparitionTicks = MAX_APPARITION_RENDER_TICKS;

        CrystalBallWorldComponent.KEY.get(world)
			.setCooldownTicks(
				gameRunning ? UmbraExpressConfig.crystalBallCooldownTicks(world) : MIN_COOLDOWN_TICKS
			);

        this.sync();

        sendApparitionMessage(apparition, mystic);
        playSound(mystic, random);
    }

    public static void sendApparitionMessage(ServerPlayerEntity apparition, ServerPlayerEntity mystic) {
        Text playerName = apparition.getName().copy().formatted(Formatting.LIGHT_PURPLE);
        mystic.sendMessage(Text.translatable("block.umbra_express.crystal_ball.apparition", playerName), true);
    }

    public void onClickDuringCooldown(ServerPlayerEntity mystic) {
        ServerPlayNetworking.send(mystic, new WiggleCrystalBallCooldownHudS2CPayload());
    }

    private static void playSound(ServerPlayerEntity mystic, Random random) {
        float soundPitch = MathHelper.nextBetween(random, 1.0F - SOUND_PITCH_DEVIATION, 1.0F + SOUND_PITCH_DEVIATION);
        PlaySoundInUIS2CPayload payload = new PlaySoundInUIS2CPayload(UmbraExpressSoundEvents.CRYSTAL_BALL_REVEAL, soundPitch, 1.0F);

        ServerPlayNetworking.send(mystic, payload);
    }

}
