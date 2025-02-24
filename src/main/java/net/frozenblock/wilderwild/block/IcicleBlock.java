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

import com.google.common.annotations.VisibleForTesting;
import com.mojang.serialization.MapCodec;
import net.frozenblock.wilderwild.block.entity.IcicleBlockEntity;
import net.frozenblock.wilderwild.block.impl.SnowloggingUtils;
import net.frozenblock.wilderwild.registry.WWBlockEntityTypes;
import net.frozenblock.wilderwild.registry.WWBlocks;
import net.frozenblock.wilderwild.tag.WWBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class IcicleBlock extends BaseEntityBlock implements Fallable, SimpleWaterloggedBlock {
	public static final MapCodec<IcicleBlock> CODEC = simpleCodec(IcicleBlock::new);
	public static final EnumProperty<Direction> TIP_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;
	public static final EnumProperty<DripstoneThickness> THICKNESS = BlockStateProperties.DRIPSTONE_THICKNESS;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	private static final int DELAY_BEFORE_FALLING = 2;
	private static final VoxelShape TIP_MERGE_SHAPE = Block.box(6D, 0D, 6D, 10D, 16D, 10D);
	private static final VoxelShape TIP_SHAPE_UP = Block.box(6D, 0D, 6D, 10D, 12D, 10D);
	private static final VoxelShape TIP_SHAPE_DOWN = Block.box(6D, 4D, 6D, 10D, 16D, 10D);
	private static final VoxelShape FRUSTUM_SHAPE = Block.box(5D, 0D, 5D, 11D, 16D, 11D);
	private static final VoxelShape MIDDLE_SHAPE = Block.box(5D, 0D, 5D, 11D, 16D, 11D);
	private static final VoxelShape BASE_SHAPE = Block.box(3D, 0D, 3D, 13D, 16D, 13D);

	public IcicleBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(
			this.stateDefinition.any().setValue(TIP_DIRECTION, Direction.UP).setValue(THICKNESS, DripstoneThickness.TIP).setValue(WATERLOGGED, false)
		);
	}

	@Override
	protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(TIP_DIRECTION, THICKNESS, WATERLOGGED);
		SnowloggingUtils.appendSnowlogProperties(builder);
	}

	@Override
	public boolean canSurvive(@NotNull BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
		return isValidIciclePlacement(levelReader, blockPos, blockState.getValue(TIP_DIRECTION));
	}

	@Override
	protected @NotNull BlockState updateShape(
		@NotNull BlockState blockState,
		Direction direction,
		BlockState blockState2,
		LevelAccessor levelAccessor,
		BlockPos blockPos,
		BlockPos blockPos2
	) {
		if (blockState.getValue(WATERLOGGED)) {
			levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelAccessor));
		}

		if (direction != Direction.UP && direction != Direction.DOWN) {
			return blockState;
		} else {
			Direction direction2 = blockState.getValue(TIP_DIRECTION);
			if (direction2 == Direction.DOWN && levelAccessor.getBlockTicks().hasScheduledTick(blockPos, this)) {
				return blockState;
			} else if (direction == direction2.getOpposite() && !this.canSurvive(blockState, levelAccessor, blockPos)) {
				if (direction2 == Direction.DOWN) {
					levelAccessor.scheduleTick(blockPos, this, DELAY_BEFORE_FALLING);
				} else {
					levelAccessor.scheduleTick(blockPos, this, 1);
				}

				return blockState;
			} else {
				boolean bl = blockState.getValue(THICKNESS) == DripstoneThickness.TIP_MERGE;
				DripstoneThickness dripstoneThickness = calculateIcicleThickness(levelAccessor, blockPos, direction2, bl);
				return blockState.setValue(THICKNESS, dripstoneThickness);
			}
		}
	}

	@Override
	protected void onProjectileHit(@NotNull Level level, BlockState blockState, BlockHitResult blockHitResult, Projectile projectile) {
		if (!level.isClientSide) {
			BlockPos blockPos = blockHitResult.getBlockPos();
			if (level instanceof ServerLevel serverLevel
				&& projectile.mayInteract(serverLevel, blockPos)
				&& projectile.mayBreak(serverLevel)
				&& projectile.getDeltaMovement().length() > 0.4D) {
				level.destroyBlock(blockPos, true);
			}
		}
	}

	public void triggerFall(@NotNull Level level, @NotNull BlockPos blockPos) {
		level.scheduleTick(blockPos, this, DELAY_BEFORE_FALLING);
	}

	@Override
	protected void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
		if (isIceSpike(blockState) && !this.canSurvive(blockState, serverLevel, blockPos)) {
			serverLevel.destroyBlock(blockPos, true);
		} else {
			spawnFallingIcicle(blockState, serverLevel, blockPos);
		}
	}

	@Override
	protected void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, @NotNull RandomSource randomSource) {
		if (randomSource.nextFloat() < 0.011377778F && isHangingIcicleStartPos(blockState, serverLevel, blockPos)) {
			growIcicleIfPossible(blockState, serverLevel, blockPos, randomSource);
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
		LevelAccessor levelAccessor = blockPlaceContext.getLevel();
		BlockPos blockPos = blockPlaceContext.getClickedPos();
		Direction direction = blockPlaceContext.getNearestLookingVerticalDirection().getOpposite();
		Direction direction2 = calculateTipDirection(levelAccessor, blockPos, direction);
		if (direction2 == null) {
			return null;
		} else {
			DripstoneThickness icicleThickness = calculateIcicleThickness(levelAccessor, blockPos, direction2, !blockPlaceContext.isSecondaryUseActive());
			return icicleThickness == null
				? null
				: SnowloggingUtils.getSnowPlacementState(
					this.defaultBlockState()
						.setValue(TIP_DIRECTION, direction2)
						.setValue(THICKNESS, icicleThickness)
						.setValue(WATERLOGGED, levelAccessor.getFluidState(blockPos).getType() == Fluids.WATER),
				blockPlaceContext
			);
		}
	}

	@Override
	protected @NotNull FluidState getFluidState(@NotNull BlockState blockState) {
		return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
	}

	@Override
	protected @NotNull VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return Shapes.empty();
	}

	@Override
	protected @NotNull VoxelShape getShape(@NotNull BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		DripstoneThickness icicleThickness = blockState.getValue(THICKNESS);
		VoxelShape voxelShape;
		if (icicleThickness == DripstoneThickness.TIP_MERGE) {
			voxelShape = TIP_MERGE_SHAPE;
		} else if (icicleThickness == DripstoneThickness.TIP) {
			if (blockState.getValue(TIP_DIRECTION) == Direction.DOWN) {
				voxelShape = TIP_SHAPE_DOWN;
			} else {
				voxelShape = TIP_SHAPE_UP;
			}
		} else if (icicleThickness == DripstoneThickness.FRUSTUM) {
			voxelShape = FRUSTUM_SHAPE;
		} else if (icicleThickness == DripstoneThickness.MIDDLE) {
			voxelShape = MIDDLE_SHAPE;
		} else {
			voxelShape = BASE_SHAPE;
		}

		Vec3 vec3 = blockState.getOffset(blockGetter, blockPos);
		return voxelShape.move(vec3.x, 0D, vec3.z);
	}

	@Override
	protected boolean isCollisionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return false;
	}

	@Override
	protected float getMaxHorizontalOffset() {
		return 0.175F;
	}

	@Override
	public void onBrokenAfterFall(Level level, BlockPos blockPos, @NotNull FallingBlockEntity fallingBlockEntity) {
		if (!fallingBlockEntity.isSilent()) {
			level.levelEvent(LevelEvent.SOUND_POINTED_DRIPSTONE_LAND, blockPos, 0);
		}
	}

	@Override
	public @NotNull DamageSource getFallDamageSource(@NotNull Entity entity) {
		// TODO: Icicle damage source
		return entity.damageSources().fallingStalactite(entity);
	}

	private static void spawnFallingIcicle(BlockState blockState, ServerLevel serverLevel, @NotNull BlockPos blockPos) {
		BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
		BlockState blockState2 = blockState;

		while (isHangingIcicle(blockState2)) {
			FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(serverLevel, mutableBlockPos, blockState2);
			fallingBlockEntity.disableDrop();
			if (isTip(blockState2, true)) {
				int i = Math.max(1 + blockPos.getY() - mutableBlockPos.getY(), 6);
				fallingBlockEntity.setHurtsEntities(i, 10);
				break;
			}

			mutableBlockPos.move(Direction.DOWN);
			blockState2 = serverLevel.getBlockState(mutableBlockPos);
		}
	}

	@VisibleForTesting
	public static void growIcicleIfPossible(BlockState blockState, @NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, RandomSource randomSource) {
		BlockState blockState2 = serverLevel.getBlockState(blockPos.above(1));
		BlockState blockState3 = serverLevel.getBlockState(blockPos.above(2));
		if (canGrow(blockState2, blockState3)) {
			BlockPos blockPos2 = findTip(blockState, serverLevel, blockPos, 7, false);
			if (blockPos2 != null) {
				BlockState blockState4 = serverLevel.getBlockState(blockPos2);
				if (canDrip(blockState4) && canTipGrow(blockState4, serverLevel, blockPos2)) {
					if (randomSource.nextBoolean()) {
						grow(serverLevel, blockPos2, Direction.DOWN);
					}
				}
			}
		}
	}

	private static void grow(@NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, Direction direction) {
		BlockPos blockPos2 = blockPos.relative(direction);
		BlockState blockState = serverLevel.getBlockState(blockPos2);
		if (isUnmergedTipWithDirection(blockState, direction.getOpposite())) {
			createMergedTips(blockState, serverLevel, blockPos2);
		} else if (blockState.isAir() || blockState.is(Blocks.WATER)) {
			createIcicle(serverLevel, blockPos2, direction, DripstoneThickness.TIP);
		}
	}

	private static void createIcicle(@NotNull LevelAccessor levelAccessor, BlockPos blockPos, Direction direction, DripstoneThickness icicleThickness) {
		BlockState blockState = WWBlocks.ICICLE
			.defaultBlockState()
			.setValue(TIP_DIRECTION, direction)
			.setValue(THICKNESS, icicleThickness)
			.setValue(WATERLOGGED, levelAccessor.getFluidState(blockPos).getType() == Fluids.WATER);
		levelAccessor.setBlock(blockPos, blockState, UPDATE_ALL);
	}

	private static void createMergedTips(@NotNull BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
		BlockPos blockPos3;
		BlockPos blockPos2;
		if (blockState.getValue(TIP_DIRECTION) == Direction.UP) {
			blockPos2 = blockPos;
			blockPos3 = blockPos.above();
		} else {
			blockPos3 = blockPos;
			blockPos2 = blockPos.below();
		}

		createIcicle(levelAccessor, blockPos3, Direction.DOWN, DripstoneThickness.TIP_MERGE);
		createIcicle(levelAccessor, blockPos2, Direction.UP, DripstoneThickness.TIP_MERGE);
	}

	@Nullable
	private static BlockPos findTip(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, int i, boolean bl) {
		if (isTip(blockState, bl)) return blockPos;
		Direction direction = blockState.getValue(TIP_DIRECTION);
		BiPredicate<BlockPos, BlockState> biPredicate = (blockPosx, blockStatex) -> blockStatex.is(WWBlocks.ICICLE)
			&& blockStatex.getValue(TIP_DIRECTION) == direction;

		return findBlockVertical(levelAccessor, blockPos, direction.getAxisDirection(), biPredicate, blockStatex -> isTip(blockStatex, bl), i).orElse(null);
	}

	@Nullable
	private static Direction calculateTipDirection(LevelReader levelReader, BlockPos blockPos, Direction direction) {
		if (isValidIciclePlacement(levelReader, blockPos, direction)) return direction;
		if (!isValidIciclePlacement(levelReader, blockPos, direction.getOpposite())) return null;
		return direction.getOpposite();
	}

	private static DripstoneThickness calculateIcicleThickness(@NotNull LevelReader levelReader, @NotNull BlockPos blockPos, @NotNull Direction direction, boolean bl) {
		Direction direction2 = direction.getOpposite();
		BlockState blockState = levelReader.getBlockState(blockPos.relative(direction));
		if (isIcicleWithDirection(blockState, direction2)) {
			return !bl && blockState.getValue(THICKNESS) != DripstoneThickness.TIP_MERGE ? DripstoneThickness.TIP : DripstoneThickness.TIP_MERGE;
		} else if (!isIcicleWithDirection(blockState, direction)) {
			return DripstoneThickness.TIP;
		} else {
			DripstoneThickness dripstoneThickness = blockState.getValue(THICKNESS);
			if (dripstoneThickness != DripstoneThickness.TIP && dripstoneThickness != DripstoneThickness.TIP_MERGE) {
				BlockState blockState2 = levelReader.getBlockState(blockPos.relative(direction2));
				return !isIcicleWithDirection(blockState2, direction) ? DripstoneThickness.BASE : DripstoneThickness.MIDDLE;
			} else {
				return DripstoneThickness.FRUSTUM;
			}
		}
	}

	public static boolean canDrip(BlockState blockState) {
		return isHangingIcicle(blockState) && blockState.getValue(THICKNESS) == DripstoneThickness.TIP && !blockState.getValue(WATERLOGGED);
	}

	private static boolean canTipGrow(@NotNull BlockState blockState, @NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos) {
		Direction direction = blockState.getValue(TIP_DIRECTION);
		BlockPos blockPos2 = blockPos.relative(direction);
		BlockState blockState2 = serverLevel.getBlockState(blockPos2);
		if (!blockState2.getFluidState().isEmpty()) {
			return false;
		} else {
			return blockState2.isAir() || isUnmergedTipWithDirection(blockState2, direction.getOpposite());
		}
	}

	private static boolean isValidIciclePlacement(@NotNull LevelReader levelReader, @NotNull BlockPos blockPos, @NotNull Direction direction) {
		BlockPos blockPos2 = blockPos.relative(direction.getOpposite());
		BlockState blockState = levelReader.getBlockState(blockPos2);
		return blockState.isFaceSturdy(levelReader, blockPos2, direction) || isIcicleWithDirection(blockState, direction);
	}

	private static boolean isTip(@NotNull BlockState blockState, boolean bl) {
		if (!blockState.is(WWBlocks.ICICLE)) {
			return false;
		} else {
			DripstoneThickness dripstoneThickness = blockState.getValue(THICKNESS);
			return dripstoneThickness == DripstoneThickness.TIP || bl && dripstoneThickness == DripstoneThickness.TIP_MERGE;
		}
	}

	private static boolean isUnmergedTipWithDirection(BlockState blockState, Direction direction) {
		return isTip(blockState, false) && blockState.getValue(TIP_DIRECTION) == direction;
	}

	private static boolean isHangingIcicle(BlockState blockState) {
		return isIcicleWithDirection(blockState, Direction.DOWN);
	}

	private static boolean isIceSpike(BlockState blockState) {
		return isIcicleWithDirection(blockState, Direction.UP);
	}

	private static boolean isHangingIcicleStartPos(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
		return isHangingIcicle(blockState) && !levelReader.getBlockState(blockPos.above()).is(WWBlocks.ICICLE);
	}

	@Override
	protected boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
		return false;
	}

	private static boolean isIcicleWithDirection(@NotNull BlockState blockState, Direction direction) {
		return blockState.is(WWBlocks.ICICLE) && blockState.getValue(TIP_DIRECTION) == direction;
	}

	private static boolean canGrow(@NotNull BlockState blockState, BlockState aboveState) {
		return blockState.is(WWBlocks.FRAGILE_ICE) || (blockState.is(WWBlockTags.ICICLE_GROWS_WHEN_UNDER) && isValidWaterForGrowing(aboveState));
	}

	public static boolean isValidWaterForGrowing(@NotNull BlockState blockState) {
		return blockState.is(Blocks.WATER) && blockState.getFluidState().isSource();
	}

	private static Optional<BlockPos> findBlockVertical(
		LevelAccessor levelAccessor,
		@NotNull BlockPos blockPos,
		Direction.AxisDirection axisDirection,
		BiPredicate<BlockPos, BlockState> biPredicate,
		Predicate<BlockState> predicate,
		int i
	) {
		Direction direction = Direction.get(axisDirection, Direction.Axis.Y);
		BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();

		for(int j = 1; j < i; ++j) {
			mutableBlockPos.move(direction);
			BlockState blockState = levelAccessor.getBlockState(mutableBlockPos);
			if (predicate.test(blockState)) {
				return Optional.of(mutableBlockPos.immutable());
			}

			if (levelAccessor.isOutsideBuildHeight(mutableBlockPos.getY()) || !biPredicate.test(mutableBlockPos, blockState)) {
				return Optional.empty();
			}
		}

		return Optional.empty();
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new IcicleBlockEntity(blockPos, blockState);
	}

	@Override
	protected @NotNull RenderShape getRenderShape(BlockState blockState) {
		return RenderShape.MODEL;
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, BlockState blockState, BlockEntityType<T> type) {
		return !level.isClientSide ? createTickerHelper(type, WWBlockEntityTypes.ICICLE, (worldx, pos, statex, blockEntity) ->
			blockEntity.serverTick(worldx, pos, statex)
		) : null;
	}
}
