package dev.spiritstudios.umbra_express.block.entity;

import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.spiritstudios.umbra_express.UmbraExpress;
import dev.spiritstudios.umbra_express.init.UmbraExpressBlockEntities;
import dev.spiritstudios.umbra_express.init.UmbraExpressSoundEvents;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.MutableText;
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
public class CrystalBallBlockEntity extends BlockEntity {

    private static final String APPARITION_KEY = "apparition";
    private static final String APPARITION_TICKS_KEY = "apparition_ticks";
    private static final String COOLDOWN_TICKS_KEY = "cooldown_ticks";

    public static final int MAX_APPARITION_RENDER_TICKS = SharedConstants.TICKS_PER_SECOND * 8;
    public static final int MAX_COOLDOWN_TICKS = SharedConstants.TICKS_PER_MINUTE * 3;

    private static final float SOUND_PITCH_DEVIATION = 0.1F;

    @Nullable
    public PlayerEntity apparition;
    @Nullable
    public UUID apparitionUUID;

    public int apparitionTicks;
    public int cooldownTicks;

    public CrystalBallBlockEntity(BlockPos pos, BlockState state) {
        super(UmbraExpressBlockEntities.CRYSTAL_BALL, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (this.apparitionUUID != null)
            nbt.putUuid(APPARITION_KEY, this.apparitionUUID);

        nbt.putInt(APPARITION_TICKS_KEY, this.apparitionTicks);
        nbt.putInt(COOLDOWN_TICKS_KEY, this.cooldownTicks);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (nbt.contains(APPARITION_KEY))
            this.apparitionUUID = nbt.getUuid(APPARITION_KEY);
    }

    public void tick(World world) {
        if (this.isRevealingApparition()) {
            this.apparitionTicks--;
            this.markDirty();
        }

        if (this.isCoolingDown()) {
            this.cooldownTicks--;
            this.markDirty();
        }

        this.apparition = this.apparitionUUID == null ? null : world.getPlayerByUuid(this.apparitionUUID);
    }

    public boolean isCoolingDown() {
        return this.cooldownTicks > 0;
    }

    public boolean isRevealingApparition() {
        return this.apparitionTicks > 0;
    }

    public void onReveal(World world, BlockPos pos, Random random, PlayerEntity apparition, PlayerEntity mystic, boolean gameRunning) {
        this.apparition = apparition;
        this.apparitionUUID = apparition.getUuid();

        this.apparitionTicks = MAX_APPARITION_RENDER_TICKS;

        this.cooldownTicks = gameRunning ? MAX_COOLDOWN_TICKS : MAX_APPARITION_RENDER_TICKS;
        this.markDirty();

        sendApparitionMessage(apparition, mystic, gameRunning);
        playSound(world, pos, random);
    }

    public static void sendApparitionMessage(PlayerEntity apparition, PlayerEntity mystic, boolean gameRunning) {
        Text playerName = apparition.getName().copy().formatted(Formatting.LIGHT_PURPLE);
        MutableText message = Text.translatable("block.umbra_express.crystal_ball.apparition", playerName).copy();

        if (gameRunning || UmbraExpress.DEVELOPMENT) {
            message.append(ScreenTexts.SPACE);
            message.append(Text.translatable("block.umbra_express.crystal_ball.apparition.in_game_suffix").withColor(TMMRoles.CIVILIAN.color()));
        }

        mystic.sendMessage(message, true);
    }

    public void sendCooldownMessage(PlayerEntity mystic) {
        String timeString = UmbraExpress.getCooldownTimeString(this.cooldownTicks);
        mystic.sendMessage(Text.translatable("block.umbra_express.crystal_ball.cooldown", timeString), true);
    }

    private static void playSound(World world, BlockPos pos, Random random) {
        float soundPitch = MathHelper.nextBetween(random, 1.0F - SOUND_PITCH_DEVIATION, 1.0F + SOUND_PITCH_DEVIATION);
        world.playSoundAtBlockCenter(pos, UmbraExpressSoundEvents.CRYSTAL_BALL_REVEAL, SoundCategory.BLOCKS, 2.0F, soundPitch, true);
    }

}
