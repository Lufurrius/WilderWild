package net.frozenblock.wilderwild.block.entity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.frozenblock.wilderwild.WilderWild;
import net.frozenblock.wilderwild.registry.RegisterBlockEntityType;
import net.frozenblock.wilderwild.registry.RegisterGameEvents;
import net.frozenblock.wilderwild.registry.RegisterProperties;
import net.frozenblock.wilderwild.registry.RegisterSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.SculkSensorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SculkSensorBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.GameEvent.Emitter;
import net.minecraft.world.event.listener.GameEventListener;
import net.minecraft.world.event.listener.VibrationListener;
import net.minecraft.world.event.listener.VibrationListener.Callback;
import net.minecraft.world.event.listener.VibrationListener.Vibration;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Objects;

public class NewSculkSensorBlockEntity extends BlockEntity implements Callback {
    private static final Logger field_38236 = LogUtils.getLogger();
    private VibrationListener listener;
    private int lastVibrationFrequency;

    public int animTicks;
    public int prevAnimTicks;
    public int age;

    public NewSculkSensorBlockEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntityType.NEW_SCULK_SENSOR, pos, state);
        this.listener = new VibrationListener(new BlockPositionSource(this.pos), ((SculkSensorBlock)state.getBlock()).getRange(), this, (Vibration)null, 0.0F, 0);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        this.getEventListener().tick(world);
        if (SculkSensorBlock.isInactive(state) && !state.get(RegisterProperties.NOT_HICCUPPING) && world.random.nextInt(320)<=1) {
            WilderWild.log("Sensor Hiccups " + pos);
            SculkSensorBlock.setActive(null, world, pos, state, (int)(Math.random()*15));
            world.emitGameEvent(null, GameEvent.SCULK_SENSOR_TENDRILS_CLICKING, pos);
            world.emitGameEvent(null, RegisterGameEvents.SCULK_SENSOR_ACTIVATE, pos);
            world.playSound(null, pos, RegisterSounds.BLOCK_SCULK_SENSOR_HICCUP, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.7F);
        }

        this.prevAnimTicks=this.animTicks;
        if (this.animTicks > 0) { --this.animTicks; }
        ++this.age;
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.lastVibrationFrequency = nbt.getInt("last_vibration_frequency");
        this.age = nbt.getInt("age");
        if (nbt.contains("listener", 10)) {
            DataResult<?> var10000 = VibrationListener.createCodec(this).parse(new Dynamic(NbtOps.INSTANCE, nbt.getCompound("listener")));
            Logger var10001 = field_38236;
            Objects.requireNonNull(var10001);
            var10000.resultOrPartial(var10001::error).ifPresent((listener) -> {
                this.listener = (VibrationListener) listener;
            });
        }
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("last_vibration_frequency", this.lastVibrationFrequency);
        nbt.putInt("age", this.age);
        DataResult<?> var10000 = VibrationListener.createCodec(this).encodeStart(NbtOps.INSTANCE, this.listener);
        Logger var10001 = field_38236;
        Objects.requireNonNull(var10001);
        var10000.resultOrPartial(var10001::error).ifPresent((listenerNbt) -> {
            nbt.put("listener", (NbtElement) listenerNbt);
        });
    }

    public VibrationListener getEventListener() {
        return this.listener;
    }

    public int getLastVibrationFrequency() {
        return this.lastVibrationFrequency;
    }

    public boolean triggersAvoidCriterion() {
        return true;
    }

    public boolean accepts(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Emitter emitter) {
        return !this.isRemoved() && (!pos.equals(this.getPos()) || event != GameEvent.BLOCK_DESTROY && event != GameEvent.BLOCK_PLACE) && SculkSensorBlock.isInactive(this.getCachedState());
    }

    public void accept(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Entity entity, @Nullable Entity sourceEntity, float distance) {
        BlockState blockState = this.getCachedState();
        if (SculkSensorBlock.isInactive(blockState)) {
            this.animTicks=10;
            world.emitGameEvent(entity, RegisterGameEvents.SCULK_SENSOR_ACTIVATE, this.getPos());
            BlockState state = world.getBlockState(this.getPos());
            world.setBlockState(this.getPos(), state.with(RegisterProperties.NOT_HICCUPPING, true));
            this.lastVibrationFrequency = SculkSensorBlock.FREQUENCIES.getInt(event);
            SculkSensorBlock.setActive(entity, world, this.pos, blockState, getPower(distance, listener.getRange()));
        }
    }

    public void onListen() {
        this.markDirty();
    }

    public static int getPower(float distance, int range) {
        double d = (double)distance / (double)range;
        return Math.max(1, 15 - MathHelper.floor(d * 15.0D));
    }

    public void setLastVibrationFrequency(int lastVibrationFrequency) {
        this.lastVibrationFrequency = lastVibrationFrequency;
    }

    public float getTendrilPitch(float tickDelta) {
        return MathHelper.lerp(tickDelta, (float)this.prevAnimTicks, (float)this.animTicks) / 10.0F;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
