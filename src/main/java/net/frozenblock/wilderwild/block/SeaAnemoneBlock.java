/*
 * Copyright 2023-2025 FrozenBlock
 * This file is part of Wilder Wild.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.wilderwild.block;

import com.mojang.serialization.MapCodec;
import net.frozenblock.wilderwild.registry.WWBlockStateProperties;
import net.frozenblock.wilderwild.registry.WWBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SeaAnemoneBlock extends BushBlock implements BonemealableBlock, LiquidBlockContainer {
	private static final VoxelShape SHAPE = Block.box(4D, 0D, 4D, 12D, 8D, 12D);
	private static final BooleanProperty GLOWING = WWBlockStateProperties.GLOWING;
	public static final int LIGHT_LEVEL = 4;
	public static final MapCodec<SeaAnemoneBlock> CODEC = simpleCodec(SeaAnemoneBlock::new);

	public SeaAnemoneBlock(@NotNull Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(GLOWING, false));
	}

	@Override
	public @NotNull MapCodec<? extends SeaAnemoneBlock> codec() {
		return CODEC;
	}

	@Override
	protected @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return SHAPE;
	}

	@Override
	protected boolean mayPlaceOn(@NotNull BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return blockState.isFaceSturdy(blockGetter, blockPos, Direction.UP) && !blockState.is(Blocks.MAGMA_BLOCK) && !blockState.is(WWBlocks.GEYSER);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
		return this.isValidWaterToReplace(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos()) ? super.getStateForPlacement(blockPlaceContext) : null;
	}

	@Override
	protected @NotNull BlockState updateShape(
		BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2
	) {
		BlockState updatedShape = super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
		if (!updatedShape.isAir()) levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
		return updatedShape;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(GLOWING);
	}

	@Override
	protected void randomTick(@NotNull BlockState blockState, @NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
		if (this.tryChangingState(blockState, serverLevel, blockPos, randomSource)) {
			serverLevel.sendParticles(
				ParticleTypes.BUBBLE,
				blockPos.getX() + 0.5D,
				blockPos.getY() + 0.5125D,
				blockPos.getZ() + 0.5D,
				randomSource.nextInt(1, 3),
				0D,
				0D,
				0D,
				0.05D
			);
			// TODO: Particles
		}

		super.randomTick(blockState, serverLevel, blockPos, randomSource);
	}

	@Override
	protected void tick(@NotNull BlockState blockState, @NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
		if (this.tryChangingState(blockState, serverLevel, blockPos, randomSource)) {
			serverLevel.sendParticles(
				ParticleTypes.BUBBLE,
				blockPos.getX() + 0.5D,
				blockPos.getY() + 0.5125D,
				blockPos.getZ() + 0.5D,
				randomSource.nextInt(1, 3),
				0D,
				0D,
				0D,
				0.05D
			);
			// TODO: Particles
		}

		super.tick(blockState, serverLevel, blockPos, randomSource);
	}

	public static boolean isGlowing(@NotNull BlockState blockState) {
		return blockState.getValue(GLOWING);
	}

	private boolean tryChangingState(BlockState blockState, @NotNull ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
		if (!serverLevel.dimensionType().natural()) return false;
		if (serverLevel.isDay() == isGlowing(blockState)) return false;
		boolean setGlowing = !isGlowing(blockState);
		serverLevel.setBlock(blockPos, blockState.setValue(GLOWING, setGlowing), UPDATE_ALL);
		serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(blockState));
		BlockPos.betweenClosed(blockPos.offset(-3, -2, -3), blockPos.offset(3, 2, 3)).forEach((blockPos2) -> {
			BlockState foundState = serverLevel.getBlockState(blockPos2);
			if (foundState == blockState) {
				double distance = Math.sqrt(blockPos.distSqr(blockPos2));
				int delay = randomSource.nextIntBetweenInclusive((int) (distance * 40D), (int) (distance * 60D));
				serverLevel.scheduleTick(blockPos2, blockState.getBlock(), delay);
			}
		});
		return true;
	}

	@Override
	public boolean isValidBonemealTarget(@NotNull LevelReader levelReader, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			mutableBlockPos.setWithOffset(blockPos, direction);
			if (this.isValidWaterToReplace(levelReader, mutableBlockPos) && this.canSurvive(blockState, levelReader, mutableBlockPos)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
		return true;
	}

	@Override
	public void performBonemeal(@NotNull ServerLevel serverLevel, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
		BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
		for (Direction direction : Direction.Plane.HORIZONTAL.shuffledCopy(randomSource)) {
			mutableBlockPos.setWithOffset(blockPos, direction);
			if (this.isValidWaterToReplace(serverLevel, mutableBlockPos) && this.canSurvive(blockState, serverLevel, mutableBlockPos)) {
				serverLevel.setBlock(mutableBlockPos, blockState, UPDATE_CLIENTS);
				return;
			}
		}
	}

	@Override
	public boolean canPlaceLiquid(Player player, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, Fluid fluid) {
		return false;
	}

	@Override
	public boolean placeLiquid(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
		return false;
	}

	@Override
	protected @NotNull FluidState getFluidState(@NotNull BlockState blockState) {
		return Fluids.WATER.getSource(false);
	}

	private boolean isValidWaterToReplace(@NotNull LevelReader levelReader, BlockPos blockPos) {
		BlockState blockState = levelReader.getBlockState(blockPos);
		FluidState fluidState = blockState.getFluidState();
		return (blockState.is(Blocks.WATER) || (blockState.canBeReplaced() && fluidState.is(FluidTags.WATER))) && fluidState.getAmount() == 8;
	}
}
