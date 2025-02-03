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

package net.frozenblock.wilderwild.entity;

import net.frozenblock.lib.entity.api.SilentTicker;
import net.frozenblock.wilderwild.WWConstants;
import net.frozenblock.wilderwild.block.impl.FallingLeafUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeafLitterBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;

public class FallingLeafTicker extends SilentTicker {
	private double yd = -0.05D;
	private Block leafLitter;

	public FallingLeafTicker(@NotNull EntityType<?> entityType, @NotNull Level level) {
		super(entityType, level);
	}

	public FallingLeafTicker(@NotNull EntityType<?> entityType, @NotNull Level level, @NotNull BlockPos pos) {
		super(entityType, level);
		this.snapTo(Vec3.atBottomCenterOf(pos));
	}

	public static void createAndSpawn(@NotNull EntityType<?> entityType, @NotNull Level level, @NotNull BlockPos pos, Block leafLitter) {
		FallingLeafTicker fallingLeafTicker = new FallingLeafTicker(entityType, level, pos);
		fallingLeafTicker.leafLitter = leafLitter;
		level.addFreshEntity(fallingLeafTicker);
	}

	@Override
	public void tick(@NotNull Level level, @NotNull Vec3 vec3, @NotNull BlockPos pos, int ticks) {
		if (vec3.y > level.getMinY() && this.leafLitter != null) {
			if (level instanceof ServerLevel serverLevel) {
				this.yd -= 0.04D;
				this.setPos(vec3.add(0D, this.yd, 0D));
				BlockHitResult hitResult = this.createHitResultFromMovement(level, vec3);
				if (hitResult.getType() != HitResult.Type.MISS) {
					BlockPos hitPos = hitResult.getBlockPos();
					BlockPos placePos = hitPos.above();
					BlockState stateToReplace = level.getBlockState(placePos);
					if (FallingLeafUtil.isSafePosToPlaceLitter(level, placePos, stateToReplace, this.leafLitter)) {
						BlockState litterState = getPlacementStateForLitter(this.leafLitter, stateToReplace, random);
						level.setBlockAndUpdate(placePos, litterState);
						serverLevel.sendParticles(
							new BlockParticleOption(ParticleTypes.BLOCK, litterState),
							placePos.getX() + 0.5D,
							placePos.getY() + 0.1D,
							placePos.getZ() + 0.5D,
							random.nextInt(8, 18),
							0.3D, 0D, 0.3D,
							0.05D
						);
						SoundType soundType = litterState.getSoundType();
						level.playSound(
							null,
							placePos,
							soundType.getPlaceSound(),
							SoundSource.BLOCKS,
							0.3F,
							soundType.getPitch() * 0.8F
						);
					}
					this.discard();
				}
				return;
			}
		}
		this.discard();
	}

	public @NotNull BlockHitResult createHitResultFromMovement(@NotNull Level world, Vec3 vec3) {
		return world.clip(
			new ClipContext(
				vec3,
				vec3.add(0D, this.yd, 0D),
				ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY,
				CollisionContext.empty()
			)
		);
	}

	private static BlockState getPlacementStateForLitter(Block leafLitter, BlockState replacingState, RandomSource random) {
		if (!(leafLitter instanceof LeafLitterBlock leafLitterBlock)) throw new IllegalStateException("Block is not a LeafLitterBlock!");

		if (replacingState.is(leafLitter) && replacingState.getBlock() instanceof LeafLitterBlock replacingLeafLitterBlock) {
			IntegerProperty segmentProperty = replacingLeafLitterBlock.getSegmentAmountProperty();
			if (replacingState.getValue(segmentProperty) < LeafLitterBlock.MAX_SEGMENT) {
				replacingState = replacingState.cycle(segmentProperty);
			}
			return replacingState;
		}

		return leafLitterBlock.defaultBlockState()
			.setValue(LeafLitterBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random))
			.setValue(leafLitterBlock.getSegmentAmountProperty(), random.nextInt(LeafLitterBlock.MIN_SEGMENT, LeafLitterBlock.MAX_SEGMENT));
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.put("LeafLitterBlock", BuiltInRegistries.BLOCK.byNameCodec().encodeStart(NbtOps.INSTANCE, this.leafLitter).getOrThrow());
		this.yd = compound.getDouble("LeafFallVelocity");
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		compound.putDouble("LeafFallVelocity", this.yd);
		if (compound.contains("LeafLitterBlock")) {
			BuiltInRegistries.BLOCK.byNameCodec()
				.parse(NbtOps.INSTANCE, compound.get("LeafLitterBlock"))
				.resultOrPartial(WWConstants.LOGGER::error)
				.ifPresent(block -> this.leafLitter = block);
		}
	}

}
