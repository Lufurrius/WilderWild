/*
 * Copyright 2023-2024 FrozenBlock
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

package net.frozenblock.wilderwild.mixin.block.leaves;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.frozenblock.wilderwild.block.PalmFrondsBlock;
import net.frozenblock.wilderwild.block.impl.FallingLeafUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {

	@ModifyExpressionValue(
		method = "isRandomlyTicking",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;",
			ordinal = 0
		)
	)
	public Comparable<?> wilderWild$isRandomlyTicking(Comparable<?> original) {
		if (original instanceof Integer) {
			if (FallingLeafUtil.getFallingLeafData(LeavesBlock.class.cast(this)).isPresent()) {
				return 7;
			}
		}
		return original;
	}

	@ModifyExpressionValue(
		method = "randomTick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/LeavesBlock;decaying(Lnet/minecraft/world/level/block/state/BlockState;)Z",
			ordinal = 0
		)
	)
	public boolean wilderWild$fixPalmFrondDecay(
		boolean original,
		@Local(argsOnly = true) ServerLevel world, @Local(argsOnly = true) BlockPos pos
	) {
		if (LeavesBlock.class.cast(this) instanceof PalmFrondsBlock palmFrondsBlock) {
			if (!palmFrondsBlock.canPalmFrondDecay(world, pos)) return false;
		}
		return original;
	}

	@Inject(method = "animateTick", at = @At("HEAD"))
	public void wilderWild$fallingLeafParticles(BlockState state, Level world, BlockPos pos, RandomSource random, CallbackInfo info) {
		FallingLeafUtil.addFallingLeafParticles(state, world, pos, random);
	}

}
