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

package net.frozenblock.wilderwild.entity.variant.moobloom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.frozenblock.wilderwild.registry.WilderWildRegistries;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public final class MoobloomVariant implements PriorityProvider<SpawnContext, SpawnCondition> {
	public static final Codec<MoobloomVariant> DIRECT_CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			ClientAsset.DEFAULT_FIELD_CODEC.forGetter(MoobloomVariant::assetInfo),
			BlockState.CODEC.fieldOf("flower_block_state").forGetter(MoobloomVariant::getFlowerBlockState),
			SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(MoobloomVariant::spawnConditions)
		).apply(instance, MoobloomVariant::new)
	);
	public static final Codec<MoobloomVariant> NETWORK_CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			ClientAsset.DEFAULT_FIELD_CODEC.forGetter(MoobloomVariant::assetInfo),
			BlockState.CODEC.fieldOf("flower_block_state").forGetter(MoobloomVariant::getFlowerBlockState)
		).apply(instance, MoobloomVariant::new)
	);
	public static final Codec<Holder<MoobloomVariant>> CODEC = RegistryFixedCodec.create(WilderWildRegistries.MOOBLOOM_VARIANT);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<MoobloomVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(WilderWildRegistries.MOOBLOOM_VARIANT);


	private final ClientAsset clientAsset;
	private final BlockState flowerBlockState;
	private final SpawnPrioritySelectors spawnConditions;


	public MoobloomVariant(ClientAsset clientAsset, BlockState flowerBlockState, SpawnPrioritySelectors spawnConditions) {
		this.clientAsset = clientAsset;
		this.flowerBlockState = flowerBlockState;
		this.spawnConditions = spawnConditions;
	}

	private MoobloomVariant(ClientAsset clientAsset, BlockState flowerBlockState) {
		this(clientAsset, flowerBlockState, SpawnPrioritySelectors.EMPTY);
	}

	@NotNull
	public ClientAsset assetInfo() {
		return this.clientAsset;
	}

	public BlockState getFlowerBlockState() {
		return this.flowerBlockState;
	}

	public SpawnPrioritySelectors spawnConditions() {
		return this.spawnConditions;
	}

	@Override
	public @NotNull List<Selector<SpawnContext, SpawnCondition>> selectors() {
		return this.spawnConditions.selectors();
	}

}
