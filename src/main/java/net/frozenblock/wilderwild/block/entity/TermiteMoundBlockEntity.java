package net.frozenblock.wilderwild.block.entity;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.frozenblock.wilderwild.WilderWild;
import net.frozenblock.wilderwild.init.WWBlockEntityTypes;
import net.frozenblock.wilderwild.init.WWBlockStateProperties;
import net.frozenblock.wilderwild.init.WWBlockTags;
import net.frozenblock.wilderwild.init.WWBlocks;
import net.frozenblock.wilderwild.init.WWSoundEvents;
import net.frozenblock.wilderwild.util.MathUtil;
import net.frozenblock.wilderwild.util.NetworkUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TermiteMoundBlockEntity extends BlockEntity {
    private final ArrayList<Termite> termites = new ArrayList<>();
    public int ticksToNextTermite;
    public int ticksToCheckLight;
    public int lastLight;

    public TermiteMoundBlockEntity(BlockPos pos, BlockState state) {
        super(WWBlockEntityTypes.TERMITE_MOUND.get(), pos, state);
    }

    public void addTermite(BlockPos pos) {
        Termite termite = new Termite(pos, pos, 0, 0, 0, this.getBlockState().getValue(WWBlockStateProperties.NATURAL));
        this.termites.add(termite);
    }

    public void tick(Level level, BlockPos pos) {
        if (this.ticksToCheckLight > 0) {
            --this.ticksToCheckLight;
        } else {
            this.lastLight = getLightLevel(level, this.worldPosition);
            this.ticksToCheckLight = 100;
        }

        int maxTermites = maxTermites(level, this.lastLight, this.getBlockState().getValue(WWBlockStateProperties.NATURAL));
        ArrayList<Termite> termitesToRemove = new ArrayList<>();
        for (Termite termite : this.termites) {
            if (termite.tick(level)) {
                NetworkUtil.sendParticles(level, Vec3.atCenterOf(termite.pos), termite.eating ? 5 : 9);
            } else {
                level.playSound(null, termite.pos, SoundEvents.BEEHIVE_ENTER, SoundSource.NEUTRAL, 1.0F, 1.0F);
                termitesToRemove.add(termite);
            }
        }
        for (Termite termite : termitesToRemove) {
            level.gameEvent(null, GameEvent.ENTITY_DIE, Vec3.atCenterOf(termite.pos));
            this.termites.remove(termite);
            level.gameEvent(null, GameEvent.BLOCK_CHANGE, Vec3.atCenterOf(pos));
        }
        if (this.termites.size() < maxTermites) {
            if (this.ticksToNextTermite > 0) {
                --this.ticksToNextTermite;
            } else {
                this.addTermite(pos);
                level.gameEvent(null, GameEvent.BLOCK_CHANGE, Vec3.atCenterOf(pos));
                level.playSound(null, this.worldPosition, WWSoundEvents.BLOCK_TERMITE_MOUND_EXIT.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                this.ticksToNextTermite = this.getBlockState().getValue(WWBlockStateProperties.NATURAL) ? 320 : 200;
            }
        }
        while (this.termites.size() > maxTermites) {
            Termite termite = this.termites.get(MathUtil.random().nextInt(this.termites.size()));
            level.playSound(null, termite.pos, WWSoundEvents.BLOCK_TERMITE_MOUND_ENTER.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
            level.gameEvent(null, GameEvent.TELEPORT, Vec3.atCenterOf(termite.pos));
            this.termites.remove(termite);
            level.gameEvent(null, GameEvent.BLOCK_CHANGE, Vec3.atCenterOf(pos));
        }
    }

    public static int maxTermites(Level level, int light, boolean natural) {
        if (level.isNight() && light < 7) {
            return natural ? 0 : 1;
        }
        return natural ? 3 : 5;
    }

    public static int getLightLevel(Level level, BlockPos blockPos) {
        int finalLight = 0;
        for (Direction direction : Direction.values()) {
            BlockPos pos = blockPos.relative(direction);
            int skyLight = 0;
            int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
            if (level.isDay() && !level.isRaining()) {
                skyLight = level.getBrightness(LightLayer.SKY, pos);
            }
            finalLight = Math.max(finalLight, Math.max(skyLight, blockLight));
        }
        return finalLight;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("ticksToNextTermite", this.ticksToNextTermite);
        tag.putInt("ticksToCheckLight", this.ticksToCheckLight);
        tag.putInt("lastLight", this.lastLight);
        Logger logger = WilderWild.LOGGER;
        Termite.CODEC.listOf()
                .encodeStart(NbtOps.INSTANCE, this.termites)
                .resultOrPartial(logger::error)
                .ifPresent((cursorsNbt) -> tag.put("termites", cursorsNbt));
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.ticksToNextTermite = tag.getInt("ticksToNextTermite");
        this.ticksToCheckLight = tag.getInt("ticksToCheckLight");
        this.lastLight = tag.getInt("lastLight");
        if (tag.contains("termites", 9)) {
            this.termites.clear();
            Logger logger = WilderWild.LOGGER;
            Termite.CODEC.listOf().parse(new Dynamic<>(NbtOps.INSTANCE, tag.getList("termites", 10)))
                    .resultOrPartial(logger::error)
                    .ifPresent(termitesAllAllAll -> {
                        int max = this.level != null ? maxTermites(this.level, this.lastLight, this.getBlockState().getValue(WWBlockStateProperties.NATURAL)) : 5;
                        int i = Math.min(termitesAllAllAll.size(), max);

                        for (int j = 0; j < i; ++j) {
                            this.termites.add(termitesAllAllAll.get(j));
                        }
                    });
        }
    }

    public static class Termite {
        public BlockPos mound;
        public BlockPos pos;
        public int blockDestroyPower;
        public int aliveTicks;
        public int update;
        public boolean natural;
        public boolean eating;

        public static final Codec<Termite> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                BlockPos.CODEC.fieldOf("mound").forGetter(Termite::getMoundPos),
                BlockPos.CODEC.fieldOf("pos").forGetter(Termite::getPos),
                Codec.intRange(0, 10000).fieldOf("blockDestroyPower").orElse(0).forGetter(Termite::getPower),
                Codec.intRange(0, 2002).fieldOf("aliveTicks").orElse(0).forGetter(Termite::getAliveTicks),
                Codec.intRange(0, 5).fieldOf("update").orElse(0).forGetter(Termite::getUpdateTicks),
                Codec.BOOL.fieldOf("natural").orElse(true).forGetter(Termite::getNatural)
        ).apply(instance, Termite::new));

        private static final Map<Block, Block> NON_NATURALS = Util.make(Maps.newHashMap(), map -> {
            map.put(Blocks.ACACIA_LOG, WWBlocks.HOLLOWED_ACACIA_LOG.get());
            map.put(Blocks.OAK_LOG, WWBlocks.HOLLOWED_OAK_LOG.get());
            map.put(Blocks.BIRCH_LOG, WWBlocks.HOLLOWED_BIRCH_LOG.get());
            map.put(Blocks.DARK_OAK_LOG, WWBlocks.HOLLOWED_DARK_OAK_LOG.get());
            map.put(Blocks.JUNGLE_LOG, WWBlocks.HOLLOWED_JUNGLE_LOG.get());
            map.put(Blocks.MANGROVE_LOG, WWBlocks.HOLLOWED_MANGROVE_LOG.get());
            map.put(Blocks.SPRUCE_LOG, WWBlocks.HOLLOWED_SPRUCE_LOG.get());
            map.put(Blocks.STRIPPED_ACACIA_LOG, Blocks.AIR);
            map.put(Blocks.STRIPPED_OAK_LOG, Blocks.AIR);
            map.put(Blocks.STRIPPED_BIRCH_LOG, Blocks.AIR);
            map.put(Blocks.STRIPPED_DARK_OAK_LOG, Blocks.AIR);
            map.put(Blocks.STRIPPED_JUNGLE_LOG, Blocks.AIR);
            map.put(Blocks.STRIPPED_MANGROVE_LOG, Blocks.AIR);
            map.put(Blocks.STRIPPED_SPRUCE_LOG, Blocks.AIR);
            map.put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD);
            map.put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD);
            map.put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD);
            map.put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD);
            map.put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD);
            map.put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD);
            map.put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD);
            map.put(Blocks.STRIPPED_ACACIA_WOOD, Blocks.AIR);
            map.put(Blocks.STRIPPED_OAK_WOOD, Blocks.AIR);
            map.put(Blocks.STRIPPED_BIRCH_WOOD, Blocks.AIR);
            map.put(Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.AIR);
            map.put(Blocks.STRIPPED_JUNGLE_WOOD, Blocks.AIR);
            map.put(Blocks.STRIPPED_MANGROVE_WOOD, Blocks.AIR);
            map.put(Blocks.STRIPPED_SPRUCE_WOOD, Blocks.AIR);
            map.put(WWBlocks.BAOBAB_LOG.get(), WWBlocks.HOLLOWED_BAOBAB_LOG.get());
            map.put(WWBlocks.STRIPPED_BAOBAB_LOG.get(), Blocks.AIR);
            map.put(WWBlocks.BAOBAB_WOOD.get(), WWBlocks.STRIPPED_BAOBAB_WOOD.get());
            map.put(WWBlocks.STRIPPED_BAOBAB_WOOD.get(), Blocks.AIR);
            map.put(WWBlocks.CYPRESS_LOG.get(), WWBlocks.HOLLOWED_CYPRESS_LOG.get());
            map.put(WWBlocks.STRIPPED_CYPRESS_LOG.get(), Blocks.AIR);
            map.put(WWBlocks.CYPRESS_WOOD.get(), WWBlocks.STRIPPED_CYPRESS_WOOD.get());
            map.put(WWBlocks.STRIPPED_CYPRESS_WOOD.get(), Blocks.AIR);
        });

        private static final Map<Block, Block> NATURALS = Util.make(Maps.newHashMap(), map -> {
            map.put(WWBlocks.BAOBAB_LOG.get(), WWBlocks.STRIPPED_BAOBAB_LOG.get());
            map.put(WWBlocks.BAOBAB_WOOD.get(), WWBlocks.STRIPPED_BAOBAB_WOOD.get());
            map.put(WWBlocks.CYPRESS_LOG.get(), WWBlocks.STRIPPED_CYPRESS_LOG.get());
            map.put(WWBlocks.CYPRESS_WOOD.get(), WWBlocks.STRIPPED_CYPRESS_WOOD.get());
            map.put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG);
            map.put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG);
            map.put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG);
            map.put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG);
            map.put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG);
            map.put(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG);
            map.put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG);
            map.put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD);
            map.put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD);
            map.put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD);
            map.put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD);
            map.put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD);
            map.put(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD);
            map.put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD);
        });

        public Termite(BlockPos mound, BlockPos pos, int blockDestroyPower, int aliveTicks, int update, boolean natural) {
            this.mound = mound;
            this.pos = pos;
            this.blockDestroyPower = blockDestroyPower;
            this.aliveTicks = aliveTicks;
            this.update = update;
            this.natural = natural;
        }

        public boolean tick(Level level) {
            boolean exit = false;
            ++this.aliveTicks;
            if (this.aliveTicks > (this.natural ? 1200 : 2000) || isTooFar(this.natural, this.mound, this.pos)) {
                return false;
            }
            if (canMove(level, this.pos)) {
                BlockState blockState = level.getBlockState(this.pos);
                Block block = blockState.getBlock();
                boolean degradable = !this.natural ? NON_NATURALS.containsKey(block) : NATURALS.containsKey(block);
                boolean breakable = blockState.is(WWBlockTags.TERMITE_BREAKABLE);
                boolean leaves = blockState.is(BlockTags.LEAVES);
                if (degradable || breakable) {
                    this.eating = true;
                    exit = true;
                    int additionalPower = breakable ? leaves ? 4 : 2 : 1;
                    this.blockDestroyPower += additionalPower;
                    if (this.blockDestroyPower > 200) {
                        this.blockDestroyPower = 0;
                        this.aliveTicks = this.natural ? Math.max(0, this.aliveTicks - (200 / additionalPower)) : 0;
                        if (blockState.is(WWBlockTags.TERMITE_BREAKABLE)) {
                            level.destroyBlock(this.pos, true);
                        } else {
                            Direction.Axis axis = blockState.hasProperty(BlockStateProperties.AXIS) ? blockState.getValue(BlockStateProperties.AXIS) : Direction.Axis.X;
                            level.addDestroyBlockEffect(this.pos, blockState);
                            BlockState setState = !this.natural ? NON_NATURALS.get(block).defaultBlockState() : NATURALS.get(block).defaultBlockState();
                            if (setState.hasProperty(BlockStateProperties.AXIS)) {
                                setState = setState.setValue(BlockStateProperties.AXIS, axis);
                            }
                            level.setBlockAndUpdate(this.pos, setState);
                        }
                    }
                } else {
                    this.eating = false;
                    this.blockDestroyPower = 0;
                    Direction direction = Direction.getRandom(level.getRandom());
                    if (blockState.isAir()) {
                        direction = Direction.DOWN;
                    }
                    BlockPos offest = this.pos.relative(direction);
                    BlockState state = level.getBlockState(offest);
                    if (state.is(WWBlockTags.KILLS_TERMITE) || state.is(Blocks.WATER) || state.is(Blocks.LAVA)) {
                        return false;
                    }

                    if (this.update > 0 && !blockState.isAir()) {
                        --this.update;
                        return true;
                    } else {
                        this.update = 1;
                        BlockPos priority = degradableBreakablePos(level, this.pos, this.natural);
                        if (priority != null) {
                            this.pos = priority;
                            exit = true;
                        } else {
                            BlockPos ledge = ledgePos(level, offest, this.natural);
                            BlockPos posUp = this.pos.above();
                            BlockState stateUp = level.getBlockState(posUp);
                            if (exposedToAir(level, offest, this.natural) && isBlockMovable(state, direction) && !(direction != Direction.DOWN && state.isAir() && (!this.mound.closerThan(this.pos, 1.5)) && ledge == null)) {
                                this.pos = offest;
                                if (ledge != null) {
                                    this.pos = ledge;
                                }
                                exit = true;
                            } else if (ledge != null && exposedToAir(level, ledge, this.natural)) {
                                this.pos = ledge;
                                exit = true;
                            } else if (!stateUp.isAir() && isBlockMovable(stateUp, Direction.UP) && exposedToAir(level, posUp, this.natural)) {
                                this.pos = posUp;
                                exit = true;
                            }
                        }
                    }
                }
            }
            return exit || (exposedToAir(level, this.pos, this.natural));
        }

        @Nullable
        public static BlockPos ledgePos(Level level, BlockPos pos, boolean natural) {
            BlockState state = level.getBlockState(pos);
            if (NATURALS.containsKey(state.getBlock()) || state.is(WWBlockTags.TERMITE_BREAKABLE)) {
                return pos;
            }
            pos = pos.below();
            state = level.getBlockState(pos);
            if (!state.isAir() && isBlockMovable(state, Direction.DOWN) && exposedToAir(level, pos, natural)) {
                return pos;
            }
            pos = pos.above().above();
            state = level.getBlockState(pos);
            if (!state.isAir() && isBlockMovable(state, Direction.UP) && exposedToAir(level, pos, natural)) {
                return pos;
            }
            return null;
        }

        @Nullable
        public static BlockPos degradableBreakablePos(Level level, BlockPos pos, boolean natural) {
            List<Direction> directions = Util.shuffledCopy(Direction.values(), level.random);
            BlockState upState = level.getBlockState(pos.relative(Direction.UP));
            if ((!natural ? NON_NATURALS.containsKey(upState.getBlock()) : NATURALS.containsKey(upState.getBlock())) || upState.is(WWBlockTags.TERMITE_BREAKABLE)) {
                return pos.relative(Direction.UP);
            }
            for (Direction direction : directions) {
                BlockState state = level.getBlockState(pos.relative(direction));
                if ((!natural ? NON_NATURALS.containsKey(state.getBlock()) : NATURALS.containsKey(state.getBlock())) || state.is(WWBlockTags.TERMITE_BREAKABLE)) {
                    return pos.relative(direction);
                }
            }
            return null;
        }

        public static boolean exposedToAir(Level level, BlockPos pos, boolean natural) {
            for (Direction direction : Direction.values()) {
                BlockState state = level.getBlockState(pos.relative(direction));
                if (state.isAir() || (!state.isRedstoneConductor(level, pos.relative(direction)) && !state.is(WWBlockTags.BLOCKS_TERMITE)) || (!natural && NON_NATURALS.containsKey(state.getBlock())) || (natural && NATURALS.containsKey(state.getBlock())) || state.is(WWBlockTags.TERMITE_BREAKABLE)) {
                    return true;
                }
            }
            return false;
        }

        public static boolean canMove(LevelAccessor level, BlockPos pos) {
            if (level instanceof ServerLevel serverLevel) {
                return serverLevel.shouldTickBlocksAt(pos);
            }
            return false;
        }

        public static boolean isBlockMovable(BlockState state, Direction direction) {
            if (state.is(WWBlockTags.BLOCKS_TERMITE)) {
                return false;
            }
            boolean moveableUp = !(direction == Direction.UP && (state.is(BlockTags.INSIDE_STEP_SOUND_BLOCKS) || state.is(BlockTags.REPLACEABLE_PLANTS) || state.is(BlockTags.FLOWERS)));
            boolean moveableDown = !(direction == Direction.DOWN && (state.is(Blocks.WATER) || state.is(Blocks.LAVA)));
            return moveableUp && moveableDown;
        }

        public static boolean isTooFar(boolean natural, BlockPos mound, BlockPos pos) {
            return !mound.closerThan(pos, natural ? 10 : 32);
        }

        public BlockPos getMoundPos() {
            return this.mound;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public int getPower() {
            return this.blockDestroyPower;
        }

        public int getAliveTicks() {
            return this.aliveTicks;
        }

        public int getUpdateTicks() {
            return this.update;
        }

        public boolean getNatural() {
            return this.natural;
        }

    }
}
