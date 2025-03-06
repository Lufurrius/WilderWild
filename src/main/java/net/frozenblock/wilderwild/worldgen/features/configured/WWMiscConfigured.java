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

package net.frozenblock.wilderwild.worldgen.features.configured;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import net.frozenblock.lib.math.api.EasyNoiseSampler;
import net.frozenblock.lib.worldgen.feature.api.FrozenLibConfiguredFeature;
import net.frozenblock.lib.worldgen.feature.api.FrozenLibFeatureUtils;
import net.frozenblock.lib.worldgen.feature.api.FrozenLibFeatures;
import net.frozenblock.lib.worldgen.feature.api.block_predicate.SearchInAreaBlockPredicate;
import net.frozenblock.lib.worldgen.feature.api.block_predicate.SearchInDirectionBlockPredicate;
import net.frozenblock.lib.worldgen.feature.api.block_predicate.TouchingBlockPredicate;
import net.frozenblock.lib.worldgen.feature.api.feature.config.ComboFeatureConfig;
import net.frozenblock.lib.worldgen.feature.api.feature.disk.config.BallBlockPlacement;
import net.frozenblock.lib.worldgen.feature.api.feature.disk.config.BallFeatureConfig;
import net.frozenblock.lib.worldgen.feature.api.feature.disk.config.BallOuterRingBlockPlacement;
import net.frozenblock.lib.worldgen.feature.api.feature.noise_path.config.NoiseBandBlockPlacement;
import net.frozenblock.lib.worldgen.feature.api.feature.noise_path.config.NoiseBandPlacement;
import net.frozenblock.lib.worldgen.feature.api.feature.noise_path.config.NoisePathFeatureConfig;
import net.frozenblock.wilderwild.WWConstants;
import net.frozenblock.wilderwild.registry.WWBlockStateProperties;
import net.frozenblock.wilderwild.registry.WWBlocks;
import net.frozenblock.wilderwild.registry.WWFeatures;
import net.frozenblock.wilderwild.tag.WWBiomeTags;
import net.frozenblock.wilderwild.tag.WWBlockTags;
import static net.frozenblock.wilderwild.worldgen.features.WWFeatureUtils.register;
import net.frozenblock.wilderwild.worldgen.impl.feature.config.SnowAndIceDiskFeatureConfig;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeafLitterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.material.Fluids;

public final class WWMiscConfigured {

	public static final FrozenLibConfiguredFeature<NoneFeatureConfiguration, ConfiguredFeature<NoneFeatureConfiguration, ?>> EMPTY = register("empty");

	public static final FrozenLibConfiguredFeature<SimpleBlockConfiguration, ConfiguredFeature<SimpleBlockConfiguration, ?>> SINGLE_MYCELIUM_GROWTH = register("single_mycelium_growth");

	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> COARSE_DIRT_PATH_RARE = register("coarse_dirt_path_rare");
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> GRAVEL_PATH_RARE = register("gravel_path_rare");
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> STONE_PATH_RARE = register("stone_path_rare");
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> COARSE_DIRT_PATH_CLEARING = register("coarse_dirt_path_clearing");
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> GRAVEL_PATH_CLEARING = register("gravel_path_clearing");
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> ROOTED_DIRT_PATH_CLEARING = register("rooted_dirt_path_clearing");
	public static final FrozenLibConfiguredFeature<ComboFeatureConfig, ConfiguredFeature<ComboFeatureConfig, ?>> STONE_DISK_AND_PILE = register("stone_disk_and_pile");

	// SWAMP
	public static final FrozenLibConfiguredFeature<DiskConfiguration, ConfiguredFeature<DiskConfiguration, ?>> DISK_MUD = register("disk_mud");
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> MUD_PATH = register("mud_path");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> MUD_TRANSITION_DISK = register("mud_transition_disk");

	// TAIGA
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> COARSE_PATH = register("coarse_dirt_path");

	// CYPRESS WETLANDS
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> UNDER_WATER_SAND_PATH = register("under_water_sand_path");
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> UNDER_WATER_GRAVEL_PATH = register("under_water_gravel_path");
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> UNDER_WATER_CLAY_PATH = register("under_water_clay_path");
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> UNDER_WATER_CLAY_PATH_BEACH = register("under_water_clay_path_beach");
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> UNDER_WATER_GRAVEL_PATH_RIVER = register("under_water_gravel_path_river");

	// BEACH AND RIVER
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> STONE_TRANSITION_DISK = register("stone_transition_disk");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> SMALL_SAND_TRANSITION_DISK = register("small_sand_transition_disk");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> BETA_BEACH_SAND_TRANSITION_DISK = register("beta_beach_sand_transition_disk");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> SMALL_GRAVEL_TRANSITION_DISK = register("small_gravel_transition_disk");
	public static final FrozenLibConfiguredFeature<VegetationPatchConfiguration, ConfiguredFeature<VegetationPatchConfiguration, ?>> RIVER_POOL = register("river_pool");
	public static final FrozenLibConfiguredFeature<VegetationPatchConfiguration, ConfiguredFeature<VegetationPatchConfiguration, ?>> SMALL_RIVER_POOL = register("small_river_pool");

	// SAVANNA
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> PACKED_MUD_PATH = register("packed_mud_path");

	// JUNGLE
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> MOSS_PATH = register("moss_path");

	// DESERT
	public static final RuleTest PACKED_MUD_REPLACEABLE = new TagMatchTest(WWBlockTags.PACKED_MUD_REPLACEABLE);
	public static final FrozenLibConfiguredFeature<OreConfiguration, ConfiguredFeature<OreConfiguration, ?>> ORE_PACKED_MUD = register("ore_packed_mud");
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> SANDSTONE_PATH = register("sandstone_path");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> SCORCHED_SAND_DISK = register("scorched_sand");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> SCORCHED_SAND_DISK_HUGE = register("scorched_sand_huge");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> SCORCHED_SAND_DISK_LIGHTNING = register("scorched_sand_lightning");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> SAND_TRANSITION_DISK = register("sand_transition");

	// BADLANDS
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> COARSE_DIRT_PATH_SMALL = register("coarse_dirt_path_small");
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> PACKED_MUD_PATH_BADLANDS = register("packed_mud_path_badlands");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> SCORCHED_RED_SAND_DISK = register("scorched_red_sand");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> SCORCHED_RED_SAND_DISK_HUGE = register("scorched_red_sand_huge");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> SCORCHED_RED_SAND_DISK_LIGHTNING = register("scorched_red_sand_lightning");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> RED_SAND_TRANSITION_DISK = register("red_sand_transition");

	// OASIS
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> GRASS_PATH = register("grass_path");
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> MOSS_PATH_OASIS = register("moss_path_oasis");

	// ARID SAVANNA
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> ARID_COARSE_PATH = register("arid_coarse_dirt_path");

	// OLD GROWTH SNOWY TAIGA
	public static final FrozenLibConfiguredFeature<BlockStateConfiguration, ConfiguredFeature<BlockStateConfiguration, ?>> SNOW = register("snow");

	// TEMPERATE RAINFOREST & RAINFOREST
	public static final FrozenLibConfiguredFeature<BlockPileConfiguration, ConfiguredFeature<BlockPileConfiguration, ?>> MOSS_PILE = register("moss_pile");
	public static final FrozenLibConfiguredFeature<VegetationPatchConfiguration, ConfiguredFeature<VegetationPatchConfiguration, ?>> BASIN_PODZOL = register("basin_podzol");
	public static final FrozenLibConfiguredFeature<VegetationPatchConfiguration, ConfiguredFeature<VegetationPatchConfiguration, ?>> BASIN_MOSS = register("basin_moss");
	public static final FrozenLibConfiguredFeature<LakeFeature.Configuration, ConfiguredFeature<LakeFeature.Configuration, ?>> MOSS_LAKE = register("moss_lake");

	// PALE GARDEN
	public static final FrozenLibConfiguredFeature<BlockPileConfiguration, ConfiguredFeature<BlockPileConfiguration, ?>> PALE_MOSS_PILE = register("pale_moss_pile");
	public static final FrozenLibConfiguredFeature<ComboFeatureConfig, ConfiguredFeature<ComboFeatureConfig, ?>> GRAVEL_AND_PALE_MOSS_PATH = register("gravel_and_pale_moss_path");

	// MANGROVE SWAMP
	public static final FrozenLibConfiguredFeature<BlockPileConfiguration, ConfiguredFeature<BlockPileConfiguration, ?>> MUD_PILE = register("mud_pile");
	public static final FrozenLibConfiguredFeature<VegetationPatchConfiguration, ConfiguredFeature<VegetationPatchConfiguration, ?>> BASIN_MUD = register("basin_mud");
	public static final FrozenLibConfiguredFeature<LakeFeature.Configuration, ConfiguredFeature<LakeFeature.Configuration, ?>> MUD_LAKE = register("mud_lake");

	// DYING FOREST
	public static final FrozenLibConfiguredFeature<ComboFeatureConfig, ConfiguredFeature<ComboFeatureConfig, ?>> COARSE_DIRT_DISK_AND_PILE = register("coarse_dirt_disk_and_pile");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> COARSE_TRANSITION_DISK = register("coarse_dirt_transition_disk");

	// MAPLE GROVE
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> YELLOW_MAPLE_LEAF_LITTER = register("yellow_maple_leaf_litter");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> ORANGE_MAPLE_LEAF_LITTER = register("orange_maple_leaf_litter");
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> RED_MAPLE_LEAF_LITTER = register("red_maple_leaf_litter");

	// SNOW
	public static final FrozenLibConfiguredFeature<NoneFeatureConfiguration, ConfiguredFeature<NoneFeatureConfiguration, ?>> SNOW_BLANKET = register("snow_blanket");
	public static final FrozenLibConfiguredFeature<SnowAndIceDiskFeatureConfig, ConfiguredFeature<SnowAndIceDiskFeatureConfig, ?>> SNOW_AND_ICE_TRANSITION_DISK = register("snow_and_freeze_transition_disk");
	public static final FrozenLibConfiguredFeature<RandomPatchConfiguration, ConfiguredFeature<RandomPatchConfiguration, ?>> SNOW_CARPET_RANDOM = register("snow_carpet_random");

	// ICE
	public static final FrozenLibConfiguredFeature<BallFeatureConfig, ConfiguredFeature<BallFeatureConfig, ?>> FRAGILE_ICE_DISK_SURFACE = register("fragile_ice_disk_surface");

	private WWMiscConfigured() {
		throw new UnsupportedOperationException("WilderMiscConfigured contains only static declarations.");
	}

	public static void registerMiscConfigured() {
		WWConstants.logWithModId("Registering WilderMiscConfigured for", true);

		EMPTY.makeAndSetHolder(Feature.NO_OP,
			NoneFeatureConfiguration.INSTANCE
		);

		SINGLE_MYCELIUM_GROWTH.makeAndSetHolder(Feature.SIMPLE_BLOCK,
			new SimpleBlockConfiguration(BlockStateProvider.simple(WWBlocks.MYCELIUM_GROWTH))
		);

		COARSE_DIRT_PATH_RARE.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.LEGACY_THREAD_SAFE)
					.noiseScale(0.12D)
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.COARSE_DIRT))
							.within(-0.2D, 0.3D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.COARSE_PATH_REPLACEABLE))
							.searchingBlockPredicate(BlockPredicate.not(SearchInDirectionBlockPredicate.hasWaterAbove(1)))
							.placementChance(0.25F)
							.build()
					).build(),
				6
			)
		);

		GRAVEL_PATH_RARE.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.XORO)
					.noiseScale(0.12D)
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.GRAVEL))
							.within(-0.2D, 0.3D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.GRAVEL_PATH_REPLACEABLE))
							.searchingBlockPredicate(TouchingBlockPredicate.exposed())
							.placementChance(0.21F)
							.build()
					).build(),
				6
			)
		);

		STONE_PATH_RARE.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.CHECKED)
					.noiseScale(0.12D)
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.STONE))
							.within(-0.2D, 0.3D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.STONE_PATH_REPLACEABLE))
							.searchingBlockPredicate(TouchingBlockPredicate.exposed())
							.placementChance(0.215F)
							.build()
					).build(),
				6
			)
		);

		COARSE_DIRT_PATH_CLEARING.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.LEGACY_THREAD_SAFE)
					.noiseScale(0.07D)
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.COARSE_DIRT))
							.within(-0.075D, 0.175D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.COARSE_CLEARING_REPLACEABLE))
							.searchingBlockPredicate(BlockPredicate.not(SearchInDirectionBlockPredicate.hasWaterAbove(1)))
							.placementChance(0.7F)
							.build()
					).build(),
				3
			)
		);

		GRAVEL_PATH_CLEARING.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.LEGACY_THREAD_SAFE)
					.noiseScale(0.07D)
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.GRAVEL))
							.within(-0.075D, 0.175D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.GRAVEL_CLEARING_REPLACEABLE))
							.searchingBlockPredicate(SearchInAreaBlockPredicate.hasAirWithin(2))
							.placementChance(0.7F)
							.build()
					).build(),
				3
			)
		);

		ROOTED_DIRT_PATH_CLEARING.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.LEGACY_THREAD_SAFE)
					.noiseScale(0.07D)
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.ROOTED_DIRT))
							.within(-0.035D, 0.135D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.ROOTED_DIRT_PATH_REPLACEABLE))
							.searchingBlockPredicate(TouchingBlockPredicate.exposed())
							.placementChance(0.5F)
							.build()
					).build(),
				3
			)
		);

		STONE_DISK_AND_PILE.makeAndSetHolder(FrozenLibFeatures.COMBO_FEATURE,
			new ComboFeatureConfig(
				ImmutableList.of(
					PlacementUtils.inlinePlaced(
						Feature.BLOCK_PILE,
						new BlockPileConfiguration(
							BlockStateProvider.simple(Blocks.STONE.defaultBlockState())
						)
					),
					PlacementUtils.inlinePlaced(
						FrozenLibFeatures.BALL_FEATURE,
						new BallFeatureConfig(
							new BallBlockPlacement.Builder(BlockStateProvider.simple(Blocks.STONE))
								.placementChance(0.95F)
								.fadeStartPercentage(0.8F)
								.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.STONE_TRANSITION_REPLACEABLE))
								.outerRingBlockPlacement(
									new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(Blocks.STONE))
										.placementChance(0.7F)
										.outerRingStartPercentage(0.7F)
										.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.STONE_TRANSITION_REPLACEABLE))
										.build()
								).build(),
							Optional.of(Heightmap.Types.OCEAN_FLOOR),
							UniformInt.of(2, 4)
						)
					)
				)
			)
		);

		DISK_MUD.makeAndSetHolder(Feature.DISK,
			new DiskConfiguration(
				new RuleBasedBlockStateProvider(
					BlockStateProvider.simple(Blocks.MUD),
					List.of(
						new RuleBasedBlockStateProvider.Rule(
							BlockPredicate.not(
								BlockPredicate.anyOf(
									BlockPredicate.solid(Direction.UP.getUnitVec3i()),
									BlockPredicate.matchesFluids(Direction.UP.getUnitVec3i(), Fluids.WATER)
								)
							),
							BlockStateProvider.simple(Blocks.MUD)
						)
					)
				),
				BlockPredicate.matchesBlocks(
					Blocks.DIRT,
					Blocks.GRASS_BLOCK
				),
				UniformInt.of(2, 6),
				2
			)
		);

		MUD_PATH.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.XORO)
					.noiseScale(0.1D)
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.MUD))
							.within(0.23D, 1D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.MUD_PATH_REPLACEABLE))
							.searchingBlockPredicate(SearchInAreaBlockPredicate.hasAirOrWaterWithin(2))
							.placementChance(0.75F)
							.build()
					).build(),
				11
			)
		);

		MUD_TRANSITION_DISK.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(Blocks.MUD))
					.placementChance(0.65F)
					.fadeStartPercentage(0.75F)
					.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.MUD_TRANSITION_REPLACEABLE))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(Blocks.MUD))
							.placementChance(0.5F)
							.outerRingStartPercentage(0.5F)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.MUD_TRANSITION_REPLACEABLE))
							.build()
					).build(),
				Optional.of(Heightmap.Types.OCEAN_FLOOR),
				UniformInt.of(3, 5)
			)
		);

		COARSE_PATH.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.LEGACY_THREAD_SAFE)
					.noiseScale(0.12D)
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.COARSE_DIRT))
							.within(-0.2D, 0.3D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.COARSE_PATH_REPLACEABLE))
							.searchingBlockPredicate(BlockPredicate.not(SearchInDirectionBlockPredicate.hasWaterAbove(1)))
							.placementChance(0.65F)
							.build()
					).build(),
				11
			)
		);

		UNDER_WATER_SAND_PATH.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.XORO)
					.noiseScale(0.05D)
					.calculateNoiseWithY()
					.scaleYNoise()
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.SAND))
							.within(0.2D, 0.54D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.UNDER_WATER_SAND_PATH_REPLACEABLE))
							.searchingBlockPredicate(SearchInAreaBlockPredicate.hasWaterWithin(2))
							.placementChance(0.925F)
							.build()
					).build(),
				12
			)
		);

		UNDER_WATER_GRAVEL_PATH.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.LOCAL)
					.noiseScale(0.07D)
					.calculateNoiseWithY()
					.scaleYNoise()
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.GRAVEL))
							.within(-0.7D, -0.3D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.UNDER_WATER_GRAVEL_PATH_REPLACEABLE))
							.searchingBlockPredicate(SearchInAreaBlockPredicate.hasWaterWithin(2))
							.placementChance(0.9F)
							.build()
					).build(),
				12
			)
		);

		UNDER_WATER_CLAY_PATH.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.LEGACY_THREAD_SAFE)
					.noiseScale(0.07D)
					.calculateNoiseWithY()
					.scaleYNoise()
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.CLAY))
							.within(0.5D, 0.85D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.UNDER_WATER_CLAY_PATH_REPLACEABLE))
							.searchingBlockPredicate(SearchInAreaBlockPredicate.hasWaterWithin(2))
							.placementChance(0.9F)
							.build()
					).build(),
				12
			)
		);

		UNDER_WATER_CLAY_PATH_BEACH.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.CHECKED)
					.noiseScale(0.1D)
					.calculateNoiseWithY()
					.scaleYNoise()
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.CLAY))
							.within(0.5D, 0.85D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.BEACH_CLAY_PATH_REPLACEABLE))
							.searchingBlockPredicate(SearchInAreaBlockPredicate.hasWaterWithin(2))
							.placementChance(0.915F)
							.build()
					).build(),
				12
			)
		);

		UNDER_WATER_GRAVEL_PATH_RIVER.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.CHECKED)
					.noiseScale(0.1D)
					.calculateNoiseWithY()
					.scaleYNoise()
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.GRAVEL))
							.within(0.5D, 0.85D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.RIVER_GRAVEL_PATH_REPLACEABLE))
							.searchingBlockPredicate(SearchInAreaBlockPredicate.hasWaterWithin(2))
							.placementChance(0.915F)
							.build()
					).build(),
				12
			)
		);

		STONE_TRANSITION_DISK.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(Blocks.STONE))
					.placementChance(0.65F)
					.fadeStartPercentage(0.75F)
					.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.STONE_TRANSITION_REPLACEABLE))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(Blocks.STONE))
							.placementChance(0.5F)
							.outerRingStartPercentage(0.5F)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.STONE_TRANSITION_REPLACEABLE))
							.build()
					).build(),
				Optional.of(Heightmap.Types.OCEAN_FLOOR),
				UniformInt.of(6, 7)
			)
		);

		SMALL_SAND_TRANSITION_DISK.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(Blocks.SAND))
					.placementChance(0.65F)
					.fadeStartPercentage(0.75F)
					.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.SMALL_SAND_TRANSITION_REPLACEABLE))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(Blocks.SAND))
							.placementChance(0.75F)
							.outerRingStartPercentage(0.5F)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.SMALL_SAND_TRANSITION_REPLACEABLE))
							.build()
					).excludedBiomes(
						FrozenLibFeatureUtils.BOOTSTRAP_CONTEXT.lookup(Registries.BIOME).getOrThrow(WWBiomeTags.HAS_SMALL_SAND_TRANSITION)
					)
					.build(),
				Optional.of(Heightmap.Types.OCEAN_FLOOR),
				UniformInt.of(6, 7)
			)
		);

		BETA_BEACH_SAND_TRANSITION_DISK.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(Blocks.SAND))
					.placementChance(0.65F)
					.fadeStartPercentage(0.75F)
					.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.SMALL_SAND_TRANSITION_REPLACEABLE))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(Blocks.SAND))
							.placementChance(0.5F)
							.outerRingStartPercentage(0.5F)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.SMALL_SAND_TRANSITION_REPLACEABLE))
							.build()
					).build(),
				Optional.of(Heightmap.Types.OCEAN_FLOOR),
				UniformInt.of(4, 6)
			)
		);

		SMALL_GRAVEL_TRANSITION_DISK.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(Blocks.GRAVEL))
					.placementChance(0.65F)
					.fadeStartPercentage(0.75F)
					.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.GRAVEL_TRANSITION_REPLACEABLE))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(Blocks.GRAVEL))
							.placementChance(0.5F)
							.outerRingStartPercentage(0.5F)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.GRAVEL_TRANSITION_REPLACEABLE))
							.build()
					).build(),
				Optional.of(Heightmap.Types.OCEAN_FLOOR),
				UniformInt.of(4, 5)
			)
		);

		RIVER_POOL.makeAndSetHolder(FrozenLibFeatures.CIRCULAR_WATERLOGGED_VEGETATION_PATCH_LESS_BORDERS,
			new VegetationPatchConfiguration(
				WWBlockTags.RIVER_POOL_REPLACEABLE,
				BlockStateProvider.simple(Blocks.GRASS_BLOCK),
				PlacementUtils.inlinePlaced(EMPTY.getHolder()),
				CaveSurface.FLOOR,
				ConstantInt.of(1),
				0.8F,
				1,
				0.000F,
				UniformInt.of(4, 8),
				0.7F
			)
		);
		SMALL_RIVER_POOL.makeAndSetHolder(FrozenLibFeatures.CIRCULAR_WATERLOGGED_VEGETATION_PATCH_LESS_BORDERS,
			new VegetationPatchConfiguration(
				WWBlockTags.RIVER_POOL_REPLACEABLE,
				BlockStateProvider.simple(Blocks.GRASS_BLOCK),
				PlacementUtils.inlinePlaced(EMPTY.getHolder()),
				CaveSurface.FLOOR,
				ConstantInt.of(1),
				0.8F,
				1,
				0.000F,
				UniformInt.of(1, 2),
				0.7F
			)
		);

		PACKED_MUD_PATH.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.LOCAL)
					.noiseScale(0.12D)
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.PACKED_MUD))
							.within(0.2D, 1D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.PACKED_MUD_PATH_REPLACEABLE))
							.searchingBlockPredicate(TouchingBlockPredicate.exposed())
							.placementChance(0.675F)
							.build()
					).build(),
				9
			)
		);

		MOSS_PATH.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.LOCAL)
					.noiseScale(0.15D)
					.calculateNoiseWithY()
					.scaleYNoise()
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.MOSS_BLOCK))
							.within(0.18D, 1D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.MOSS_PATH_REPLACEABLE))
							.searchingBlockPredicate(TouchingBlockPredicate.exposed())
							.placementChance(0.7F)
							.build()
					).build(),
				9
			)
		);

		ORE_PACKED_MUD.makeAndSetHolder(Feature.ORE,
			new OreConfiguration(
				PACKED_MUD_REPLACEABLE,
				Blocks.PACKED_MUD.defaultBlockState(),
				40
			)
		);

		SANDSTONE_PATH.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.CHECKED)
					.noiseScale(0.2D)
					.calculateNoiseWithY()
					.scaleYNoise()
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.SANDSTONE))
							.within(0.4D, 1D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.SANDSTONE_PATH_REPLACEABLE))
							.searchingBlockPredicate(SearchInAreaBlockPredicate.hasAirOrWaterOrLavaWithin(2))
							.placementChance(0.65F)
							.build()
					).build(),
				10
			)
		);

		SCORCHED_SAND_DISK.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(WWBlocks.SCORCHED_SAND.defaultBlockState().setValue(WWBlockStateProperties.CRACKED, true)))
					.placementChance(0.95F)
					.fadeStartPercentage(0.8F)
					.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.SCORCHED_SAND_FEATURE_INNER_REPLACEABLE))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(WWBlocks.SCORCHED_SAND.defaultBlockState()))
							.placementChance(0.895F)
							.outerRingStartPercentage(0.7F)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.SCORCHED_SAND_FEATURE_REPLACEABLE))
							.build()
					).build(),
				Optional.of(Heightmap.Types.OCEAN_FLOOR),
				UniformInt.of(2, 8)
			)
		);

		SCORCHED_SAND_DISK_HUGE.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(WWBlocks.SCORCHED_SAND.defaultBlockState().setValue(WWBlockStateProperties.CRACKED, true)))
					.placementChance(0.95F)
					.fadeStartPercentage(0.8F)
					.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.SCORCHED_SAND_FEATURE_INNER_REPLACEABLE))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(WWBlocks.SCORCHED_SAND.defaultBlockState()))
							.placementChance(0.895F)
							.outerRingStartPercentage(0.7F)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.SCORCHED_SAND_FEATURE_REPLACEABLE))
							.build()
					).build(),
				Optional.of(Heightmap.Types.OCEAN_FLOOR),
				UniformInt.of(11, 15)
			)
		);

		SCORCHED_SAND_DISK_LIGHTNING.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(WWBlocks.SCORCHED_SAND.defaultBlockState().setValue(WWBlockStateProperties.CRACKED, true)))
					.placementChance(0.95F)
					.fadeStartPercentage(0.8F)
					.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.SCORCHED_SAND_FEATURE_INNER_REPLACEABLE))
					.searchingBlockPredicate(TouchingBlockPredicate.exposed())
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(WWBlocks.SCORCHED_SAND))
							.placementChance(0.895F)
							.outerRingStartPercentage(0.6F)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.SCORCHED_SAND_FEATURE_REPLACEABLE))
							.searchingBlockPredicate(TouchingBlockPredicate.exposed())
							.build()
					).build(),
				Optional.empty(),
				UniformInt.of(1, 3)
			)
		);

		SAND_TRANSITION_DISK.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(Blocks.SAND))
					.placementChance(0.65F)
					.fadeStartPercentage(0.5F)
					.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.SAND_TRANSITION_REPLACEABLE))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(Blocks.SAND))
							.placementChance(0.6F)
							.outerRingStartPercentage(0.4F)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.SAND_TRANSITION_REPLACEABLE))
							.build()
					).excludedBiomes(
						FrozenLibFeatureUtils.BOOTSTRAP_CONTEXT.lookup(Registries.BIOME).getOrThrow(WWBiomeTags.HAS_SAND_TRANSITION)
					)
					.build(),
				Optional.of(Heightmap.Types.OCEAN_FLOOR),
				UniformInt.of(7, 12)
			)
		);

		COARSE_DIRT_PATH_SMALL.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.CHECKED)
					.noiseScale(0.15D)
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.COARSE_DIRT))
							.within(0.2D, 1D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.SMALL_COARSE_DIRT_PATH_REPLACEABLE))
							.searchingBlockPredicate(BlockPredicate.not(SearchInDirectionBlockPredicate.hasWaterAbove(1)))
							.placementChance(0.715F)
							.build()
					).build(),
				8
			)
		);

		PACKED_MUD_PATH_BADLANDS.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.LEGACY_THREAD_SAFE)
					.noiseScale(0.7D)
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.PACKED_MUD))
							.within(0.2D, 1D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.PACKED_MUD_PATH_BADLANDS_REPLACEABLE))
							.searchingBlockPredicate(TouchingBlockPredicate.exposed())
							.placementChance(0.9F)
							.build()
					).build(),
				4
			)
		);

		SCORCHED_RED_SAND_DISK.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(WWBlocks.SCORCHED_RED_SAND.defaultBlockState().setValue(WWBlockStateProperties.CRACKED, true)))
					.placementChance(0.95F)
					.fadeStartPercentage(0.8F)
					.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.RED_SCORCHED_SAND_FEATURE_INNER_REPLACEABLE))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(WWBlocks.SCORCHED_RED_SAND.defaultBlockState()))
							.placementChance(0.895F)
							.outerRingStartPercentage(0.7F)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.RED_SCORCHED_SAND_FEATURE_REPLACEABLE))
							.build()
					).build(),
				Optional.of(Heightmap.Types.OCEAN_FLOOR),
				UniformInt.of(2, 8)
			)
		);

		SCORCHED_RED_SAND_DISK_HUGE.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(WWBlocks.SCORCHED_RED_SAND.defaultBlockState().setValue(WWBlockStateProperties.CRACKED, true)))
					.placementChance(0.95F)
					.fadeStartPercentage(0.8F)
					.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.RED_SCORCHED_SAND_FEATURE_INNER_REPLACEABLE))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(WWBlocks.SCORCHED_RED_SAND.defaultBlockState()))
							.placementChance(0.895F)
							.outerRingStartPercentage(0.7F)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.RED_SCORCHED_SAND_FEATURE_REPLACEABLE))
							.build()
					).build(),
				Optional.of(Heightmap.Types.OCEAN_FLOOR),
				UniformInt.of(11, 15)
			)
		);

		SCORCHED_RED_SAND_DISK_LIGHTNING.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(WWBlocks.SCORCHED_RED_SAND.defaultBlockState().setValue(WWBlockStateProperties.CRACKED, true)))
					.placementChance(0.95F)
					.fadeStartPercentage(0.8F)
					.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.RED_SCORCHED_SAND_FEATURE_INNER_REPLACEABLE))
					.searchingBlockPredicate(TouchingBlockPredicate.exposed())
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(WWBlocks.SCORCHED_RED_SAND.defaultBlockState()))
							.placementChance(0.895F)
							.outerRingStartPercentage(0.6F)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.RED_SCORCHED_SAND_FEATURE_REPLACEABLE))
							.searchingBlockPredicate(TouchingBlockPredicate.exposed())
							.build()
					).build(),
				Optional.empty(),
				UniformInt.of(1, 3)
			)
		);

		RED_SAND_TRANSITION_DISK.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(Blocks.RED_SAND))
					.placementChance(0.65F)
					.fadeStartPercentage(0.5F)
					.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.RED_SAND_TRANSITION_REPLACEABLE))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(Blocks.RED_SAND))
							.placementChance(0.6F)
							.outerRingStartPercentage(0.4F)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.RED_SAND_TRANSITION_REPLACEABLE))
							.build()
					).excludedBiomes(
						FrozenLibFeatureUtils.BOOTSTRAP_CONTEXT.lookup(Registries.BIOME).getOrThrow(WWBiomeTags.HAS_RED_SAND_TRANSITION)
					)
					.build(),
				Optional.of(Heightmap.Types.OCEAN_FLOOR),
				UniformInt.of(7, 12)
			)
		);

		// OASIS

		GRASS_PATH.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.XORO)
					.noiseScale(0.15D)
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacements(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.GRASS_BLOCK))
							.within(0.4D, 1D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.OASIS_PATH_REPLACEABLE))
							.searchingBlockPredicate(
								BlockPredicate.not(
									SearchInDirectionBlockPredicate.anyAboveMatch(
										BlockPredicate.matchesFluids(Fluids.WATER, Fluids.FLOWING_WATER, Fluids.LAVA, Fluids.FLOWING_LAVA),
										1
									)
								)
							)
							.placementChance(0.8F)
							.build()
					).build(),
				11
			)
		);

		MOSS_PATH_OASIS.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.CHECKED)
					.noiseScale(0.1D)
					.calculateNoiseWithY()
					.scaleYNoise()
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.MOSS_BLOCK))
							.within(0.12D, 1D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.OASIS_PATH_REPLACEABLE))
							.searchingBlockPredicate(TouchingBlockPredicate.exposed())
							.placementChance(0.725F)
							.build()
					).build(),
				9
			)
		);

		// ARID SAVANNA

		ARID_COARSE_PATH.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.LEGACY_THREAD_SAFE)
					.noiseScale(0.15D)
					.heightmapType(Heightmap.Types.OCEAN_FLOOR)
					.noiseBandBlockPlacement(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.COARSE_DIRT))
							.within(-0.15D, 0.55D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(BlockTags.DIRT))
							.searchingBlockPredicate(
								BlockPredicate.allOf(BlockPredicate.not(SearchInDirectionBlockPredicate.hasWaterAbove(1)))
							)
							.placementChance(0.825F)
							.build()
					).build(),
				12
			)
		);

		SNOW.makeAndSetHolder(Feature.FOREST_ROCK,
			new BlockStateConfiguration(
				Blocks.SNOW_BLOCK.defaultBlockState()
			)
		);

		MOSS_PILE.makeAndSetHolder(Feature.BLOCK_PILE,
			new BlockPileConfiguration(
				BlockStateProvider.simple(Blocks.MOSS_BLOCK)
			)
		);

		BASIN_PODZOL.makeAndSetHolder(FrozenLibFeatures.CIRCULAR_WATERLOGGED_VEGETATION_PATCH,
			new VegetationPatchConfiguration(
				WWBlockTags.BASIN_REPLACEABLE,
				BlockStateProvider.simple(Blocks.PODZOL),
				PlacementUtils.inlinePlaced(EMPTY.getHolder()),
				CaveSurface.FLOOR,
				ConstantInt.of(2),
				0.8F,
				1,
				0.000F,
				UniformInt.of(1, 3),
				0.7F
			)
		);

		BASIN_MOSS.makeAndSetHolder(FrozenLibFeatures.CIRCULAR_WATERLOGGED_VEGETATION_PATCH,
			new VegetationPatchConfiguration(
				WWBlockTags.BASIN_REPLACEABLE,
				BlockStateProvider.simple(Blocks.MOSS_BLOCK),
				PlacementUtils.inlinePlaced(EMPTY.getHolder()),
				CaveSurface.FLOOR,
				ConstantInt.of(2),
				0.8F,
				1,
				0.000F,
				UniformInt.of(1, 3),
				0.7F
			)
		);

		MOSS_LAKE.makeAndSetHolder(Feature.LAKE,
			new LakeFeature.Configuration(
				BlockStateProvider.simple(Blocks.WATER.defaultBlockState()),
				BlockStateProvider.simple(Blocks.MOSS_BLOCK.defaultBlockState())
			)
		);

		MUD_PILE.makeAndSetHolder(Feature.BLOCK_PILE,
			new BlockPileConfiguration(
				BlockStateProvider.simple(Blocks.MUD)
			)
		);

		BASIN_MUD.makeAndSetHolder(FrozenLibFeatures.CIRCULAR_WATERLOGGED_VEGETATION_PATCH,
			new VegetationPatchConfiguration(
				WWBlockTags.BASIN_REPLACEABLE,
				BlockStateProvider.simple(Blocks.MUD),
				PlacementUtils.inlinePlaced(EMPTY.getHolder()),
				CaveSurface.FLOOR,
				ConstantInt.of(2),
				0.8F,
				1,
				0.000F,
				UniformInt.of(1, 3),
				0.7F
			)
		);

		MUD_LAKE.makeAndSetHolder(Feature.LAKE,
			new LakeFeature.Configuration(
				BlockStateProvider.simple(Blocks.WATER.defaultBlockState()),
				BlockStateProvider.simple(Blocks.MUD.defaultBlockState())
			)
		);

		PALE_MOSS_PILE.makeAndSetHolder(Feature.BLOCK_PILE,
			new BlockPileConfiguration(
				BlockStateProvider.simple(Blocks.PALE_MOSS_BLOCK)
			)
		);

		GRAVEL_AND_PALE_MOSS_PATH.makeAndSetHolder(FrozenLibFeatures.COMBO_FEATURE,
			new ComboFeatureConfig(
				List.of(
					PlacementUtils.inlinePlaced(
						FrozenLibFeatures.NOISE_PATH_FEATURE,
						new NoisePathFeatureConfig(
							new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.XORO)
								.noiseScale(0.1D)
								.heightmapType(Heightmap.Types.OCEAN_FLOOR)
								.noiseBandBlockPlacement(
									new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.GRAVEL))
										.within(-0.2D, 0.3D)
										.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.GRAVEL_AND_PALE_MOSS_PATH_REPLACEABLE))
										.searchingBlockPredicate(BlockPredicate.not(SearchInDirectionBlockPredicate.hasWaterAbove(1)))
										.placementChance(0.35F)
										.build()
								)
								.build(),
							9
						)
					),
					PlacementUtils.inlinePlaced(
						FrozenLibFeatures.NOISE_PATH_FEATURE,
						new NoisePathFeatureConfig(
							new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.XORO)
								.noiseScale(0.1D)
								.heightmapType(Heightmap.Types.OCEAN_FLOOR)
								.noiseBandBlockPlacement(
									new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.PALE_MOSS_BLOCK))
										.within(-0.21D, 0.31D)
										.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.GRAVEL_AND_PALE_MOSS_PATH_REPLACEABLE))
										.searchingBlockPredicate(BlockPredicate.not(SearchInDirectionBlockPredicate.hasWaterAbove(1)))
										.placementChance(0.25F)
										.build()
								)
								.build(),
							9
						)
					)
				)
			)
		);

		COARSE_DIRT_DISK_AND_PILE.makeAndSetHolder(FrozenLibFeatures.COMBO_FEATURE,
			new ComboFeatureConfig(
				ImmutableList.of(
					PlacementUtils.inlinePlaced(
						Feature.BLOCK_PILE,
						new BlockPileConfiguration(
							BlockStateProvider.simple(Blocks.COARSE_DIRT.defaultBlockState())
						)
					),
					PlacementUtils.inlinePlaced(
						FrozenLibFeatures.BALL_FEATURE,
						new BallFeatureConfig(
							new BallBlockPlacement.Builder(BlockStateProvider.simple(Blocks.COARSE_DIRT))
								.placementChance(0.95F)
								.fadeStartPercentage(0.5F)
								.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.COARSE_DIRT_DISK_REPLACEABLE))
								.outerRingBlockPlacement(
									new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(Blocks.COARSE_DIRT))
										.placementChance(0.875F)
										.outerRingStartPercentage(0.7F)
										.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.COARSE_DIRT_DISK_REPLACEABLE))
										.build()
								).build(),
							Optional.of(Heightmap.Types.OCEAN_FLOOR),
							UniformInt.of(2, 4)
						)
					)
				)
			)
		);

		COARSE_TRANSITION_DISK.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(Blocks.COARSE_DIRT))
					.placementChance(0.95F)
					.fadeStartPercentage(0.5F)
					.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.COARSE_DIRT_DISK_REPLACEABLE))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(Blocks.COARSE_DIRT))
							.placementChance(0.875F)
							.outerRingStartPercentage(0.7F)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.COARSE_DIRT_DISK_REPLACEABLE))
							.build()
					).build(),
				Optional.of(Heightmap.Types.OCEAN_FLOOR),
				UniformInt.of(2, 4)
			)
		);

		WeightedList.Builder<BlockState> yellowLitterStates = WeightedList.builder();
		for (int i = 1; i <= 4; i++) {
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				yellowLitterStates.add(
					WWBlocks.YELLOW_MAPLE_LEAF_LITTER.defaultBlockState()
						.setValue(WWBlocks.YELLOW_MAPLE_LEAF_LITTER.getSegmentAmountProperty(), i)
						.setValue(LeafLitterBlock.FACING, direction),
					1
				);
			}
		}
		YELLOW_MAPLE_LEAF_LITTER.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(new WeightedStateProvider(yellowLitterStates.build()))
					.placementChance(0.75F)
					.fadeStartPercentage(0.5F)
					.searchingBlockPredicate(BlockPredicate.wouldSurvive(WWBlocks.YELLOW_MAPLE_LEAF_LITTER.defaultBlockState(), Vec3i.ZERO))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(new WeightedStateProvider(yellowLitterStates.build()))
							.placementChance(0.65F)
							.outerRingStartPercentage(0.7F)
							.searchingBlockPredicate(BlockPredicate.wouldSurvive(WWBlocks.YELLOW_MAPLE_LEAF_LITTER.defaultBlockState(), Vec3i.ZERO))
							.build()
					).build(),
				Optional.of(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
				UniformInt.of(2, 4)
			)
		);

		WeightedList.Builder<BlockState> orangeLitterStates = WeightedList.builder();
		for (int i = 1; i <= 4; i++) {
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				orangeLitterStates.add(
					WWBlocks.ORANGE_MAPLE_LEAF_LITTER.defaultBlockState()
						.setValue(WWBlocks.ORANGE_MAPLE_LEAF_LITTER.getSegmentAmountProperty(), i)
						.setValue(LeafLitterBlock.FACING, direction),
					1
				);
			}
		}
		ORANGE_MAPLE_LEAF_LITTER.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(new WeightedStateProvider(orangeLitterStates.build()))
					.placementChance(0.75F)
					.fadeStartPercentage(0.5F)
					.searchingBlockPredicate(BlockPredicate.wouldSurvive(WWBlocks.ORANGE_MAPLE_LEAF_LITTER.defaultBlockState(), Vec3i.ZERO))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(new WeightedStateProvider(orangeLitterStates.build()))
							.placementChance(0.65F)
							.outerRingStartPercentage(0.7F)
							.searchingBlockPredicate(BlockPredicate.wouldSurvive(WWBlocks.ORANGE_MAPLE_LEAF_LITTER.defaultBlockState(), Vec3i.ZERO))
							.build()
					).build(),
				Optional.of(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
				UniformInt.of(2, 4)
			)
		);

		WeightedList.Builder<BlockState> redLitterStates = WeightedList.builder();
		for (int i = 1; i <= 4; i++) {
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				redLitterStates.add(
					WWBlocks.RED_MAPLE_LEAF_LITTER.defaultBlockState()
						.setValue(WWBlocks.RED_MAPLE_LEAF_LITTER.getSegmentAmountProperty(), i)
						.setValue(LeafLitterBlock.FACING, direction),
					1
				);
			}
		}
		RED_MAPLE_LEAF_LITTER.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(new WeightedStateProvider(redLitterStates.build()))
					.placementChance(0.75F)
					.fadeStartPercentage(0.5F)
					.searchingBlockPredicate(BlockPredicate.wouldSurvive(WWBlocks.RED_MAPLE_LEAF_LITTER.defaultBlockState(), Vec3i.ZERO))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(new WeightedStateProvider(redLitterStates.build()))
							.placementChance(0.65F)
							.outerRingStartPercentage(0.7F)
							.searchingBlockPredicate(BlockPredicate.wouldSurvive(WWBlocks.RED_MAPLE_LEAF_LITTER.defaultBlockState(), Vec3i.ZERO))
							.build()
					).build(),
				Optional.of(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
				UniformInt.of(2, 4)
			)
		);

		SNOW_BLANKET.makeAndSetHolder(WWFeatures.SNOW_BLANKET_FEATURE, NoneFeatureConfiguration.INSTANCE);

		SNOW_AND_ICE_TRANSITION_DISK.makeAndSetHolder(WWFeatures.SNOW_AND_FREEZE_DISK_FEATURE,
			new SnowAndIceDiskFeatureConfig(
				UniformInt.of(6, 7),
				UniformInt.of(2, 4),
				0.65F,
				0.5F
			)
		);

		SNOW_CARPET_RANDOM.makeAndSetHolder(Feature.RANDOM_PATCH,
			FeatureUtils.simpleRandomPatchConfiguration(
				64,
				PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
					new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SNOW))
				)
			)
		);

		FRAGILE_ICE_DISK_SURFACE.makeAndSetHolder(FrozenLibFeatures.BALL_FEATURE,
			new BallFeatureConfig(
				new BallBlockPlacement.Builder(BlockStateProvider.simple(WWBlocks.FRAGILE_ICE))
					.placementChance(0.85F)
					.fadeStartPercentage(0.75F)
					.replacementBlockPredicate(
						BlockPredicate.anyOf(
							BlockPredicate.replaceable(),
							BlockPredicate.matchesBlocks(Blocks.ICE)
						)
					)
					.searchingBlockPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), Blocks.WATER))
					.outerRingBlockPlacement(
						new BallOuterRingBlockPlacement.Builder(BlockStateProvider.simple(WWBlocks.FRAGILE_ICE))
							.placementChance(0.75F)
							.outerRingStartPercentage(0.675F)
							.replacementBlockPredicate(
								BlockPredicate.anyOf(
									BlockPredicate.replaceable(),
									BlockPredicate.matchesBlocks(Blocks.ICE)
								)
							)
							.searchingBlockPredicate(BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), Blocks.WATER))
							.build()
					).build(),
				Optional.of(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
				UniformInt.of(4, 5)
			)
		);
	}
}
