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

package net.frozenblock.wilderwild.entity.variant.jellyfish;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.frozenblock.wilderwild.registry.WilderWildRegistries;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public final class JellyfishVariant implements PriorityProvider<SpawnContext, SpawnCondition> {
	public static final Codec<JellyfishVariant> DIRECT_CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			ClientAsset.DEFAULT_FIELD_CODEC.forGetter(JellyfishVariant::assetInfo),
			Codec.BOOL.fieldOf("pearlescent").forGetter(JellyfishVariant::isPearlescent),
			SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(JellyfishVariant::spawnConditions),
			RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("reproduction_food").forGetter(JellyfishVariant::getReproductionFood)
		).apply(instance, JellyfishVariant::new)
	);
	public static final Codec<JellyfishVariant> NETWORK_CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			ClientAsset.DEFAULT_FIELD_CODEC.forGetter(JellyfishVariant::assetInfo),
			Codec.BOOL.fieldOf("pearlescent").forGetter(JellyfishVariant::isPearlescent),
			RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("reproduction_food").forGetter(JellyfishVariant::getReproductionFood)
		).apply(instance, JellyfishVariant::new)
	);
	public static final Codec<Holder<JellyfishVariant>> CODEC = RegistryFixedCodec.create(WilderWildRegistries.JELLYFISH_VARIANT);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<JellyfishVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(WilderWildRegistries.JELLYFISH_VARIANT);

	private final ClientAsset clientAsset;
	private final boolean pearlescent;
	private final SpawnPrioritySelectors spawnConditions;
	private final HolderSet<Item> reproductionFood;

	public JellyfishVariant(ClientAsset clientAsset, boolean pearlescent, SpawnPrioritySelectors spawnConditions, HolderSet<Item> reproductionFood) {
		this.clientAsset = clientAsset;
		this.pearlescent = pearlescent;
		this.spawnConditions = spawnConditions;
		this.reproductionFood = reproductionFood;
	}

	private JellyfishVariant(ClientAsset clientAsset, boolean pearlescent, HolderSet<Item> reproductionFood) {
		this(clientAsset, pearlescent, SpawnPrioritySelectors.EMPTY, reproductionFood);
	}

	@NotNull
	public ClientAsset assetInfo() {
		return this.clientAsset;
	}

	public SpawnPrioritySelectors spawnConditions() {
		return this.spawnConditions;
	}

	public boolean isPearlescent() {
		return this.pearlescent;
	}

	public HolderSet<Item> getReproductionFood() {
		return this.reproductionFood;
	}

	@Override
	public @NotNull List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
		return this.spawnConditions.selectors();
	}
}
