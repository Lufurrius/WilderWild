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

package net.frozenblock.wilderwild.worldgen.feature.placed;

import java.util.List;
import net.frozenblock.lib.worldgen.feature.api.FrozenLibPlacedFeature;
import net.frozenblock.wilderwild.WWConstants;
import net.frozenblock.wilderwild.registry.WWBlocks;
import net.frozenblock.wilderwild.tag.WWBlockTags;
import net.frozenblock.wilderwild.worldgen.feature.WWPlacementUtils;
import static net.frozenblock.wilderwild.worldgen.feature.WWPlacementUtils.register;
import net.frozenblock.wilderwild.worldgen.feature.configured.WWCaveConfigured;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public final class WWCavePlaced {
	// MESOGLEA CAVES
	public static final FrozenLibPlacedFeature ORE_CALCITE = WWPlacementUtils.register("ore_calcite");
	public static final BlockPredicate ONLY_IN_WATER_PREDICATE = BlockPredicate.matchesBlocks(Blocks.WATER);
	public static final FrozenLibPlacedFeature MESOGLEA_CAVES_STONE_POOL = WWPlacementUtils.register("mesoglea_caves_stone_pool");
	public static final FrozenLibPlacedFeature MESOGLEA_PILLAR = WWPlacementUtils.register("blue_mesoglea_pillar");
	public static final FrozenLibPlacedFeature PURPLE_MESOGLEA_PILLAR = WWPlacementUtils.register("purple_mesoglea_pillar");
	public static final FrozenLibPlacedFeature BLUE_MESOGLEA_PATH = WWPlacementUtils.register("blue_mesoglea_path");
	public static final FrozenLibPlacedFeature PURPLE_MESOGLEA_PATH = WWPlacementUtils.register("purple_mesoglea_path");
	public static final FrozenLibPlacedFeature BLUE_MESOGLEA = register("blue_mesoglea");
	public static final FrozenLibPlacedFeature UPSIDE_DOWN_BLUE_MESOGLEA = register("upside_down_blue_mesoglea");
	public static final FrozenLibPlacedFeature PURPLE_MESOGLEA = register("purple_mesoglea");
	public static final FrozenLibPlacedFeature UPSIDE_DOWN_PURPLE_MESOGLEA = register("upside_down_purple_mesoglea");
	public static final FrozenLibPlacedFeature NEMATOCYST_BLUE = register("nematocyst_blue");
	public static final FrozenLibPlacedFeature NEMATOCYST_PURPLE = register("nematocyst_purple");
	public static final FrozenLibPlacedFeature MESOGLEA_CLUSTER_PURPLE = register("mesoglea_cluster_purple");
	public static final FrozenLibPlacedFeature MESOGLEA_CLUSTER_BLUE = register("mesoglea_cluster_blue");
	public static final FrozenLibPlacedFeature LARGE_MESOGLEA_PURPLE = register("large_mesoglea_purple");
	public static final FrozenLibPlacedFeature LARGE_MESOGLEA_BLUE = register("large_mesoglea_blue");

	// MAGMATIC CAVES
	public static final FrozenLibPlacedFeature MAGMA_LAVA_POOL = WWPlacementUtils.register("magma_lava_pool");
	public static final FrozenLibPlacedFeature MAGMA_PATH = WWPlacementUtils.register("magma_path");
	public static final FrozenLibPlacedFeature MAGMA_DISK = WWPlacementUtils.register("magma_disk");
	public static final FrozenLibPlacedFeature MAGMA_PILE = WWPlacementUtils.register("magma_pile");
	public static final FrozenLibPlacedFeature OBSIDIAN_DISK = WWPlacementUtils.register("obsidian_disk");
	public static final FrozenLibPlacedFeature LAVA_SPRING_EXTRA = WWPlacementUtils.register("lava_spring_extra");
	public static final FrozenLibPlacedFeature FIRE_PATCH_MAGMA = WWPlacementUtils.register("fire_patch_magma");
	public static final FrozenLibPlacedFeature ORE_GABBRO = WWPlacementUtils.register("ore_gabbro");
	public static final FrozenLibPlacedFeature GABBRO_DISK = WWPlacementUtils.register("gabbro_disk");
	public static final FrozenLibPlacedFeature GABBRO_PILE = WWPlacementUtils.register("gabbro_pile");
	public static final FrozenLibPlacedFeature NETHER_GEYSER = WWPlacementUtils.register("nether_geyser");
	public static final FrozenLibPlacedFeature NETHER_LAVA_GEYSER = WWPlacementUtils.register("nether_lava_geyser");
	public static final FrozenLibPlacedFeature GEYSER_LAVA = WWPlacementUtils.register("geyser_lava");
	public static final FrozenLibPlacedFeature GEYSER_UP = WWPlacementUtils.register("geyser_up");
	public static final FrozenLibPlacedFeature GEYSER_DOWN = WWPlacementUtils.register("geyser_down");
	public static final FrozenLibPlacedFeature GEYSER_NORTH = WWPlacementUtils.register("geyser_north");
	public static final FrozenLibPlacedFeature GEYSER_EAST = WWPlacementUtils.register("geyser_east");
	public static final FrozenLibPlacedFeature GEYSER_SOUTH = WWPlacementUtils.register("geyser_south");
	public static final FrozenLibPlacedFeature GEYSER_WEST = WWPlacementUtils.register("geyser_west");
	public static final FrozenLibPlacedFeature DOWNWARDS_GEYSER_COLUMN = WWPlacementUtils.register("downwards_geyser_column");
	public static final FrozenLibPlacedFeature DOWNWARDS_GABBRO_COLUMN = WWPlacementUtils.register("downwards_gabbro_column");
	public static final FrozenLibPlacedFeature LAVA_LAKE_EXTRA = WWPlacementUtils.register("lava_lake_extra");
	public static final FrozenLibPlacedFeature FOSSIL_LAVA = WWPlacementUtils.register("fossil_lava");
	public static final FrozenLibPlacedFeature UPSIDE_DOWN_MAGMA = register("upside_down_magma");

	// FROZEN CAVES
	public static final FrozenLibPlacedFeature PACKED_ICE_PATH = WWPlacementUtils.register("packed_ice_path");
	public static final FrozenLibPlacedFeature PACKED_ICE_DISK = WWPlacementUtils.register("packed_ice_disk");
	public static final FrozenLibPlacedFeature ICE_DISK = WWPlacementUtils.register("ice_disk");
	public static final FrozenLibPlacedFeature ICE_PILE = WWPlacementUtils.register("ice_pile");
	public static final FrozenLibPlacedFeature SNOW_DISK_UPPER = WWPlacementUtils.register("snow_disk_upper");
	public static final FrozenLibPlacedFeature POWDER_SNOW_DISK_UPPER = WWPlacementUtils.register("powder_snow_disk_upper");
	public static final FrozenLibPlacedFeature SNOW_DISK_LOWER = WWPlacementUtils.register("snow_disk_lower");
	public static final FrozenLibPlacedFeature POWDER_SNOW_DISK_LOWER = WWPlacementUtils.register("powder_snow_disk_lower");
	public static final FrozenLibPlacedFeature ICICLE_PATCH = WWPlacementUtils.register("icicle_patch");
	public static final FrozenLibPlacedFeature ICE_PATCH_CEILING = WWPlacementUtils.register("ice_patch_ceiling");
	public static final FrozenLibPlacedFeature ICE_COLUMN_PATCH = WWPlacementUtils.register("ice_column_patch");
	public static final FrozenLibPlacedFeature ICE_PATCH = WWPlacementUtils.register("ice_patch");
	public static final FrozenLibPlacedFeature DIORITE_PATCH = WWPlacementUtils.register("diorite_patch");
	public static final FrozenLibPlacedFeature DIORITE_PATCH_CEILING = WWPlacementUtils.register("diorite_patch_ceiling");
	public static final FrozenLibPlacedFeature ORE_DIORITE_EXTRA = WWPlacementUtils.register("ore_diorite_extra");

	private WWCavePlaced() {
		throw new UnsupportedOperationException("WilderCavePlaced contains only static declarations.");
	}

	public static void registerCavePlaced(@NotNull BootstrapContext<PlacedFeature> entries) {
		var configuredFeatures = entries.lookup(Registries.CONFIGURED_FEATURE);
		var placedFeatures = entries.lookup(Registries.PLACED_FEATURE);

		WWConstants.log("Registering WWCavePlaced.", true);

		// MESOGLEA CAVES

		ORE_CALCITE.makeAndSetHolder(WWCaveConfigured.ORE_CALCITE.getHolder(),
			modifiersWithCount(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-54), VerticalAnchor.absolute(64)))
		);

		MESOGLEA_CAVES_STONE_POOL.makeAndSetHolder(WWCaveConfigured.STONE_POOL.getHolder(),
			CountPlacement.of(60),
			InSquarePlacement.spread(),
			HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()),
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		MESOGLEA_PILLAR.makeAndSetHolder(WWCaveConfigured.BLUE_MESOGLEA_COLUMN.getHolder(),
			CountPlacement.of(7),
			InSquarePlacement.spread(),
			HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()),
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), ONLY_IN_WATER_PREDICATE, 12),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		PURPLE_MESOGLEA_PILLAR.makeAndSetHolder(WWCaveConfigured.PURPLE_MESOGLEA_COLUMN.getHolder(),
			CountPlacement.of(7),
			InSquarePlacement.spread(),
			HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()),
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), ONLY_IN_WATER_PREDICATE, 12),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		BLUE_MESOGLEA_PATH.makeAndSetHolder(WWCaveConfigured.BLUE_MESOGLEA_PATH.getHolder(),
			CountPlacement.of(24),
			InSquarePlacement.spread(),
			HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()),
			BiomeFilter.biome()
		);

		PURPLE_MESOGLEA_PATH.makeAndSetHolder(WWCaveConfigured.PURPLE_MESOGLEA_PATH.getHolder(),
			CountPlacement.of(24),
			InSquarePlacement.spread(),
			HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()),
			BiomeFilter.biome()
		);

		BLUE_MESOGLEA.makeAndSetHolder(WWCaveConfigured.BLUE_MESOGLEA.getHolder(),
			CountPlacement.of(9),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 1),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		UPSIDE_DOWN_BLUE_MESOGLEA.makeAndSetHolder(WWCaveConfigured.UPSIDE_DOWN_BLUE_MESOGLEA.getHolder(),
			CountPlacement.of(12),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 1),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		PURPLE_MESOGLEA.makeAndSetHolder(WWCaveConfigured.PURPLE_MESOGLEA.getHolder(),
			CountPlacement.of(9),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 1),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		UPSIDE_DOWN_PURPLE_MESOGLEA.makeAndSetHolder(WWCaveConfigured.UPSIDE_DOWN_PURPLE_MESOGLEA.getHolder(),
			CountPlacement.of(12),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 1),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		NEMATOCYST_BLUE.makeAndSetHolder(WWCaveConfigured.NEMATOCYST_BLUE.getHolder(),
			CountPlacement.of(ConstantInt.of(99)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			BiomeFilter.biome()
		);

		NEMATOCYST_PURPLE.makeAndSetHolder(WWCaveConfigured.NEMATOCYST_PURPLE.getHolder(),
			CountPlacement.of(ConstantInt.of(99)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			BiomeFilter.biome()
		);

		MESOGLEA_CLUSTER_PURPLE.makeAndSetHolder(WWCaveConfigured.MESOGLEA_CLUSTER_PURPLE.getHolder(),
			CountPlacement.of(UniformInt.of(9, 15)), InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, BiomeFilter.biome()
		);

		MESOGLEA_CLUSTER_BLUE.makeAndSetHolder(WWCaveConfigured.MESOGLEA_CLUSTER_BLUE.getHolder(),
			CountPlacement.of(UniformInt.of(6, 13)), InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, BiomeFilter.biome()
		);

		LARGE_MESOGLEA_PURPLE.makeAndSetHolder(WWCaveConfigured.LARGE_MESOGLEA_PURPLE.getHolder(),
			CountPlacement.of(UniformInt.of(1, 5)), RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, BiomeFilter.biome()
		);

		LARGE_MESOGLEA_BLUE.makeAndSetHolder(WWCaveConfigured.LARGE_MESOGLEA_BLUE.getHolder(),
			CountPlacement.of(UniformInt.of(1, 5)), RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, BiomeFilter.biome()
		);

		// MAGMATIC CAVES

		MAGMA_LAVA_POOL.makeAndSetHolder(WWCaveConfigured.MAGMA_LAVA_POOL.getHolder(),
			CountPlacement.of(4),
			InSquarePlacement.spread(),
			HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(5), VerticalAnchor.aboveBottom(60)),
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		MAGMA_PATH.makeAndSetHolder(WWCaveConfigured.MAGMA_AND_GABBRO_PATH.getHolder(),
			modifiersWithCount(64, PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT)
		);

		MAGMA_DISK.makeAndSetHolder(WWCaveConfigured.MAGMA_DISK.getHolder(),
			modifiersWithCount(48, PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT)
		);

		OBSIDIAN_DISK.makeAndSetHolder(WWCaveConfigured.OBSIDIAN_DISK.getHolder(),
			modifiersWithCount(6, PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT)
		);

		LAVA_SPRING_EXTRA.makeAndSetHolder(configuredFeatures.getOrThrow(MiscOverworldFeatures.SPRING_LAVA_OVERWORLD),
			CountPlacement.of(UniformInt.of(144, 200)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			BiomeFilter.biome()
		);

		FIRE_PATCH_MAGMA.makeAndSetHolder(WWCaveConfigured.FIRE_PATCH_MAGMA.getHolder(),
			CountPlacement.of(UniformInt.of(80, 130)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			BiomeFilter.biome()
		);

		ORE_GABBRO.makeAndSetHolder(WWCaveConfigured.ORE_GABBRO.getHolder(),
			modifiersWithCount(4, HeightRangePlacement.uniform(VerticalAnchor.absolute(-54), VerticalAnchor.absolute(64)))
		);

		GABBRO_DISK.makeAndSetHolder(WWCaveConfigured.GABBRO_DISK.getHolder(),
			modifiersWithCount(32, PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT)
		);

		GABBRO_PILE.makeAndSetHolder(WWCaveConfigured.GABBRO_PILE.getHolder(),
			CountPlacement.of(UniformInt.of(24, 64)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.replaceable(), 12),
			BiomeFilter.biome()
		);

		MAGMA_PILE.makeAndSetHolder(WWCaveConfigured.MAGMA_PILE.getHolder(),
			CountPlacement.of(UniformInt.of(32, 72)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.replaceable(), 12),
			BlockPredicateFilter.forPredicate(
				BlockPredicate.anyOf(
					BlockPredicate.noFluid(Direction.UP.getUnitVec3i()),
					BlockPredicate.matchesFluids(Direction.UP.getUnitVec3i(), Fluids.LAVA)
				)
			),
			BiomeFilter.biome()
		);

		NETHER_GEYSER.makeAndSetHolder(WWCaveConfigured.GEYSER_UP.getHolder(),
			CountPlacement.of(UniformInt.of(24, 48)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(
				Direction.DOWN,
				BlockPredicate.matchesTag(WWBlockTags.NETHER_GEYSER_REPLACEABLE),
				BlockPredicate.replaceable(),
				12
			),
			BiomeFilter.biome()
		);

		NETHER_LAVA_GEYSER.makeAndSetHolder(WWCaveConfigured.UPWARDS_GEYSER_COLUMN.getHolder(),
			CountPlacement.of(UniformInt.of(8, 20)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(
				Direction.DOWN,
				BlockPredicate.allOf(
					BlockPredicate.matchesTag(WWBlockTags.NETHER_GEYSER_REPLACEABLE),
					BlockPredicate.matchesFluids(Direction.UP.getUnitVec3i(), Fluids.LAVA),
					BlockPredicate.matchesFluids(Direction.UP.getUnitVec3i().above(), Fluids.LAVA),
					BlockPredicate.matchesFluids(Direction.UP.getUnitVec3i().above().above(), Fluids.LAVA)
				),
				BlockPredicate.replaceable(),
				12
			),
			BiomeFilter.biome()
		);

		GEYSER_LAVA.makeAndSetHolder(WWCaveConfigured.GEYSER_UP.getHolder(),
			CountPlacement.of(UniformInt.of(64, 72)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(
				Direction.DOWN,
				BlockPredicate.allOf(
					BlockPredicate.solid(),
					BlockPredicate.matchesFluids(Direction.UP.getUnitVec3i(), Fluids.LAVA)
				),
				BlockPredicate.replaceable(),
				12
			),
			BiomeFilter.biome()
		);

		GEYSER_UP.makeAndSetHolder(WWCaveConfigured.GEYSER_UP.getHolder(),
			CountPlacement.of(UniformInt.of(64, 72)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(
				Direction.DOWN,
				BlockPredicate.anyOf(
					BlockPredicate.matchesBlocks(WWBlocks.GABBRO, Blocks.MAGMA_BLOCK),
					BlockPredicate.allOf(
						BlockPredicate.solid(),
						BlockPredicate.matchesFluids(Direction.UP.getUnitVec3i(), Fluids.LAVA)
					)
				),
				BlockPredicate.replaceable(),
				12
			),
			BiomeFilter.biome()
		);

		GEYSER_DOWN.makeAndSetHolder(WWCaveConfigured.GEYSER_DOWN.getHolder(),
			CountPlacement.of(UniformInt.of(48, 64)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(
				Direction.UP,
				BlockPredicate.matchesBlocks(WWBlocks.GABBRO, Blocks.MAGMA_BLOCK),
				BlockPredicate.replaceable(),
				12
			),
			BiomeFilter.biome()
		);

		GEYSER_NORTH.makeAndSetHolder(WWCaveConfigured.GEYSER_NORTH.getHolder(),
			CountPlacement.of(UniformInt.of(96, 128)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(
				Direction.DOWN,
				BlockPredicate.allOf(
					BlockPredicate.solid(),
					BlockPredicate.replaceable(Direction.NORTH.getUnitVec3i()),
					BlockPredicate.matchesBlocks(Direction.SOUTH.getUnitVec3i(), WWBlocks.GABBRO, Blocks.MAGMA_BLOCK)
				),
				BlockPredicate.alwaysTrue(),
				12
			),
			BiomeFilter.biome()
		);

		GEYSER_EAST.makeAndSetHolder(WWCaveConfigured.GEYSER_EAST.getHolder(),
			CountPlacement.of(UniformInt.of(96, 128)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(
				Direction.DOWN,
				BlockPredicate.allOf(
					BlockPredicate.solid(),
					BlockPredicate.replaceable(Direction.EAST.getUnitVec3i()),
					BlockPredicate.matchesBlocks(Direction.WEST.getUnitVec3i(), WWBlocks.GABBRO, Blocks.MAGMA_BLOCK)
				),
				BlockPredicate.alwaysTrue(),
				12
			),
			BiomeFilter.biome()
		);

		GEYSER_SOUTH.makeAndSetHolder(WWCaveConfigured.GEYSER_SOUTH.getHolder(),
			CountPlacement.of(UniformInt.of(96, 128)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(
				Direction.DOWN,
				BlockPredicate.allOf(
					BlockPredicate.solid(),
					BlockPredicate.replaceable(Direction.SOUTH.getUnitVec3i()),
					BlockPredicate.matchesBlocks(Direction.NORTH.getUnitVec3i(), WWBlocks.GABBRO, Blocks.MAGMA_BLOCK)
				),
				BlockPredicate.alwaysTrue(),
				12
			),
			BiomeFilter.biome()
		);

		GEYSER_WEST.makeAndSetHolder(WWCaveConfigured.GEYSER_WEST.getHolder(),
			CountPlacement.of(UniformInt.of(96, 128)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(
				Direction.DOWN,
				BlockPredicate.allOf(
					BlockPredicate.solid(),
					BlockPredicate.replaceable(Direction.WEST.getUnitVec3i()),
					BlockPredicate.matchesBlocks(Direction.EAST.getUnitVec3i(), WWBlocks.GABBRO, Blocks.MAGMA_BLOCK)
				),
				BlockPredicate.alwaysTrue(),
				12
			),
			BiomeFilter.biome()
		);

		DOWNWARDS_GEYSER_COLUMN.makeAndSetHolder(WWCaveConfigured.DOWNWARDS_GEYSER_COLUMN.getHolder(),
			CountPlacement.of(UniformInt.of(8, 24)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(
				Direction.UP,
				BlockPredicate.matchesBlocks(WWBlocks.GABBRO, Blocks.MAGMA_BLOCK),
				BlockPredicate.replaceable(),
				12
			),
			BiomeFilter.biome()
		);

		DOWNWARDS_GABBRO_COLUMN.makeAndSetHolder(WWCaveConfigured.DOWNWARDS_GABBRO_COLUMN.getHolder(),
			CountPlacement.of(UniformInt.of(72, 120)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.replaceable(), 12),
			BiomeFilter.biome()
		);

		LAVA_LAKE_EXTRA.makeAndSetHolder(configuredFeatures.getOrThrow(MiscOverworldFeatures.LAKE_LAVA),
			CountPlacement.of(UniformInt.of(0, 8)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			BiomeFilter.biome()
		);

		FOSSIL_LAVA.makeAndSetHolder(configuredFeatures.getOrThrow(CaveFeatures.FOSSIL_DIAMONDS),
			RarityFilter.onAverageOnceEvery(20),
			InSquarePlacement.spread(),
			HeightRangePlacement.uniform(VerticalAnchor.absolute(-54), VerticalAnchor.absolute(-24)),
			BiomeFilter.biome()
		);

		UPSIDE_DOWN_MAGMA.makeAndSetHolder(WWCaveConfigured.UPSIDE_DOWN_MAGMA.getHolder(),
			CountPlacement.of(72),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 4),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		// FROZEN CAVES

		PACKED_ICE_PATH.makeAndSetHolder(WWCaveConfigured.PACKED_ICE_PATH.getHolder(),
			modifiersWithCount(92, PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT)
		);

		PACKED_ICE_DISK.makeAndSetHolder(WWCaveConfigured.PACKED_ICE_DISK.getHolder(),
			modifiersWithCount(32, PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT)
		);

		ICE_DISK.makeAndSetHolder(WWCaveConfigured.ICE_DISK.getHolder(),
			modifiersWithCount(24, PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT)
		);

		ICE_PILE.makeAndSetHolder(WWCaveConfigured.ICE_PILE.getHolder(),
			CountPlacement.of(UniformInt.of(60, 80)),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
			BiomeFilter.biome()
		);

		POWDER_SNOW_DISK_LOWER.makeAndSetHolder(WWCaveConfigured.POWDER_SNOW_DISK.getHolder(),
			InSquarePlacement.spread(),
			HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(48)),
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
			BiomeFilter.biome()
		);

		SNOW_DISK_LOWER.makeAndSetHolder(WWCaveConfigured.SNOW_DISK.getHolder(),
			CountPlacement.of(2),
			InSquarePlacement.spread(),
			HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(48)),
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
			BiomeFilter.biome()
		);

		POWDER_SNOW_DISK_UPPER.makeAndSetHolder(WWCaveConfigured.POWDER_SNOW_DISK.getHolder(),
			CountPlacement.of(5),
			InSquarePlacement.spread(),
			HeightRangePlacement.uniform(VerticalAnchor.absolute(48), VerticalAnchor.absolute(256)),
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
			BiomeFilter.biome()
		);

		SNOW_DISK_UPPER.makeAndSetHolder(WWCaveConfigured.SNOW_DISK.getHolder(),
			CountPlacement.of(10),
			InSquarePlacement.spread(),
			HeightRangePlacement.uniform(VerticalAnchor.absolute(48), VerticalAnchor.absolute(256)),
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
			BiomeFilter.biome()
		);

		ICICLE_PATCH.makeAndSetHolder(WWCaveConfigured.ICICLE_PATCH.getHolder(),
			CountPlacement.of(16),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		ICE_PATCH_CEILING.makeAndSetHolder(WWCaveConfigured.ICE_PATCH_CEILING.getHolder(),
			CountPlacement.of(24),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		ICE_COLUMN_PATCH.makeAndSetHolder(WWCaveConfigured.ICE_COLUMN_PATCH.getHolder(),
			CountPlacement.of(12),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		ICE_PATCH.makeAndSetHolder(WWCaveConfigured.ICE_PATCH.getHolder(),
			CountPlacement.of(48),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		DIORITE_PATCH.makeAndSetHolder(WWCaveConfigured.DIORITE_PATCH.getHolder(),
			CountPlacement.of(16),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		DIORITE_PATCH_CEILING.makeAndSetHolder(WWCaveConfigured.DIORITE_PATCH_CEILING.getHolder(),
			CountPlacement.of(16),
			InSquarePlacement.spread(),
			PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
			EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12),
			RandomOffsetPlacement.vertical(ConstantInt.of(1)),
			BiomeFilter.biome()
		);

		ORE_DIORITE_EXTRA.makeAndSetHolder(configuredFeatures.getOrThrow(OreFeatures.ORE_DIORITE),
			modifiersWithCount(1, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(256)))
		);
	}

	@Contract("_, _ -> new")
	private static @Unmodifiable List<PlacementModifier> modifiers(PlacementModifier countModifier, PlacementModifier heightModifier) {
		return List.of(countModifier, InSquarePlacement.spread(), heightModifier, BiomeFilter.biome());
	}

	@Contract("_, _ -> new")
	private static @Unmodifiable List<PlacementModifier> modifiersWithCount(int count, PlacementModifier heightModifier) {
		return modifiers(CountPlacement.of(count), heightModifier);
	}
}
