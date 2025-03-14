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

import net.frozenblock.lib.math.api.EasyNoiseSampler;
import net.frozenblock.lib.worldgen.feature.api.FrozenLibConfiguredFeature;
import net.frozenblock.lib.worldgen.feature.api.FrozenLibFeatures;
import net.frozenblock.lib.worldgen.feature.api.block_predicate.SearchInDirectionBlockPredicate;
import net.frozenblock.lib.worldgen.feature.api.feature.config.ComboFeatureConfig;
import net.frozenblock.lib.worldgen.feature.api.feature.noise_path.config.NoiseBandBlockPlacement;
import net.frozenblock.lib.worldgen.feature.api.feature.noise_path.config.NoiseBandPlacement;
import net.frozenblock.lib.worldgen.feature.api.feature.noise_path.config.NoisePathFeatureConfig;
import net.frozenblock.wilderwild.WWConstants;
import net.frozenblock.wilderwild.registry.WWBlocks;
import net.frozenblock.wilderwild.registry.WWFeatures;
import net.frozenblock.wilderwild.tag.WWBlockTags;
import net.frozenblock.wilderwild.worldgen.features.WWFeatureUtils;
import static net.frozenblock.wilderwild.worldgen.features.WWFeatureUtils.register;
import net.frozenblock.wilderwild.worldgen.impl.feature.config.AlgaeFeatureConfig;
import net.frozenblock.wilderwild.worldgen.impl.feature.config.CattailFeatureConfig;
import net.frozenblock.wilderwild.worldgen.impl.feature.config.SpongeBudFeatureConfig;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import java.util.List;

public final class WWAquaticConfigured {
	public static final FrozenLibConfiguredFeature<CattailFeatureConfig, ConfiguredFeature<CattailFeatureConfig, ?>> CATTAIL = WWFeatureUtils.register("cattail");
	public static final FrozenLibConfiguredFeature<CattailFeatureConfig, ConfiguredFeature<CattailFeatureConfig, ?>> CATTAIL_SMALL = WWFeatureUtils.register("cattail_small");
	public static final FrozenLibConfiguredFeature<CattailFeatureConfig, ConfiguredFeature<CattailFeatureConfig, ?>> CATTAIL_MUD = WWFeatureUtils.register("cattail_mud");
	public static final FrozenLibConfiguredFeature<RandomPatchConfiguration, ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_FLOWERING_WATERLILY = WWFeatureUtils.register("patch_flowering_waterlily");
	public static final FrozenLibConfiguredFeature<AlgaeFeatureConfig, ConfiguredFeature<AlgaeFeatureConfig, ?>> PATCH_ALGAE = WWFeatureUtils.register("patch_algae");
	public static final FrozenLibConfiguredFeature<AlgaeFeatureConfig, ConfiguredFeature<AlgaeFeatureConfig, ?>> PATCH_ALGAE_SMALL = WWFeatureUtils.register("patch_algae_small");
	public static final FrozenLibConfiguredFeature<ProbabilityFeatureConfiguration, ConfiguredFeature<ProbabilityFeatureConfiguration, ?>> SEAGRASS_MEADOW = WWFeatureUtils.register("seagrass_meadow");
	public static final FrozenLibConfiguredFeature<RandomPatchConfiguration, ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_BARNACLES_DENSE = WWFeatureUtils.register("patch_barnacles_dense");
	public static final FrozenLibConfiguredFeature<RandomPatchConfiguration, ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_BARNACLES_STRUCTURE = WWFeatureUtils.register("patch_barnacles_structure");
	public static final FrozenLibConfiguredFeature<RandomPatchConfiguration, ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_BARNACLES = WWFeatureUtils.register("patch_barnacles");
	public static final FrozenLibConfiguredFeature<SpongeBudFeatureConfig, ConfiguredFeature<SpongeBudFeatureConfig, ?>> SPONGE_BUD = WWFeatureUtils.register("sponge_bud");
	public static final FrozenLibConfiguredFeature<RandomPatchConfiguration, ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_SEA_ANEMONE = WWFeatureUtils.register("patch_sea_anemone");
	public static final FrozenLibConfiguredFeature<RandomPatchConfiguration, ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_TUBE_WORMS = WWFeatureUtils.register("patch_tube_worms");

	public static final FrozenLibConfiguredFeature<VegetationPatchConfiguration, ConfiguredFeature<VegetationPatchConfiguration, ?>> HYDROTHERMAL_VENT = WWFeatureUtils.register("hydrothermal_vent");
	public static final FrozenLibConfiguredFeature<ComboFeatureConfig, ConfiguredFeature<ComboFeatureConfig, ?>> HYDROTHERMAL_VENT_TUBE_WORMS = WWFeatureUtils.register("hydrothermal_vent_tube_worms");
	public static final FrozenLibConfiguredFeature<NoisePathFeatureConfig, ConfiguredFeature<NoisePathFeatureConfig, ?>> OCEAN_MOSS = register("ocean_moss");

	private WWAquaticConfigured() {
		throw new UnsupportedOperationException("WWAquaticConfigured contains only static declarations.");
	}

	public static void registerAquaticConfigured() {
		WWConstants.logWithModId("Registering WWAquaticConfigured for", true);

		CATTAIL.makeAndSetHolder(WWFeatures.CATTAIL_FEATURE,
			new CattailFeatureConfig(
				UniformInt.of(-7, 7),
				UniformInt.of(12, 18),
				true,
				WWBlockTags.CATTAIL_FEATURE_PLACEABLE
			)
		);

		CATTAIL_SMALL.makeAndSetHolder(WWFeatures.CATTAIL_FEATURE,
			new CattailFeatureConfig(
				UniformInt.of(-5, 5),
				UniformInt.of(6, 12),
				true,
				WWBlockTags.CATTAIL_FEATURE_PLACEABLE
			)
		);

		CATTAIL_MUD.makeAndSetHolder(WWFeatures.CATTAIL_FEATURE,
			new CattailFeatureConfig(
				UniformInt.of(-7, 7),
				UniformInt.of(12, 18),
				false,
				WWBlockTags.CATTAIL_FEATURE_MUD_PLACEABLE
			)
		);

		PATCH_FLOWERING_WATERLILY.makeAndSetHolder(Feature.RANDOM_PATCH,
			new RandomPatchConfiguration(
				10,
				7,
				3,
				PlacementUtils.onlyWhenEmpty(
					Feature.SIMPLE_BLOCK,
					new SimpleBlockConfiguration(BlockStateProvider.simple(WWBlocks.FLOWERING_LILY_PAD))
				)
			)
		);

		PATCH_ALGAE.makeAndSetHolder(WWFeatures.ALGAE_FEATURE,
			new AlgaeFeatureConfig(UniformInt.of(4, 10))
		);

		PATCH_ALGAE_SMALL.makeAndSetHolder(WWFeatures.ALGAE_FEATURE,
			new AlgaeFeatureConfig(UniformInt.of(2, 6))
		);

		SEAGRASS_MEADOW.makeAndSetHolder(Feature.SEAGRASS,
			new ProbabilityFeatureConfiguration(0.025F)
		);

		PATCH_BARNACLES_DENSE.makeAndSetHolder(Feature.RANDOM_PATCH,
			new RandomPatchConfiguration(
				30,
				6,
				3,
				PlacementUtils.inlinePlaced(
					Feature.MULTIFACE_GROWTH,
					new MultifaceGrowthConfiguration(
						WWBlocks.BARNACLES,
						10,
						true,
						false,
						true,
						0.7F,
						new HolderSet.Named<>(
							BuiltInRegistries.BLOCK.holderOwner(),
							WWBlockTags.BARNACLES_FEATURE_PLACEABLE
						)
					),
					BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.WATER))
				)
			)
		);

		PATCH_BARNACLES_STRUCTURE.makeAndSetHolder(Feature.RANDOM_PATCH,
			new RandomPatchConfiguration(
				42,
				8,
				8,
				PlacementUtils.inlinePlaced(
					Feature.MULTIFACE_GROWTH,
					new MultifaceGrowthConfiguration(
						WWBlocks.BARNACLES,
						6,
						true,
						true,
						true,
						0.7F,
						new HolderSet.Named<>(
							BuiltInRegistries.BLOCK.holderOwner(),
							WWBlockTags.BARNACLES_FEATURE_PLACEABLE_STRUCTURE
						)
					),
					BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.WATER))
				)
			)
		);

		PATCH_BARNACLES.makeAndSetHolder(Feature.RANDOM_PATCH,
			new RandomPatchConfiguration(
				18,
				6,
				3,
				PlacementUtils.inlinePlaced(
					Feature.MULTIFACE_GROWTH,
					new MultifaceGrowthConfiguration(
						WWBlocks.BARNACLES,
						10,
						true,
						false,
						true,
						0.7F,
						new HolderSet.Named<>(
							BuiltInRegistries.BLOCK.holderOwner(),
							WWBlockTags.BARNACLES_FEATURE_PLACEABLE
						)
					),
					BlockPredicateFilter.forPredicate(BlockPredicate.matchesBlocks(Blocks.WATER))
				)
			)
		);

		SPONGE_BUD.makeAndSetHolder(WWFeatures.SPONGE_BUD_FEATURE,
			new SpongeBudFeatureConfig(
				20,
				true,
				true,
				true,
				WWBlockTags.SMALL_SPONGE_GROWS_ON
			)
		);

		PATCH_SEA_ANEMONE.makeAndSetHolder(Feature.RANDOM_PATCH,
			new RandomPatchConfiguration(
				12,
				6,
				3,
				PlacementUtils.inlinePlaced(
					WWFeatures.SEA_ANEMONE_FEATURE,
					new BlockStateConfiguration(WWBlocks.SEA_ANEMONE.defaultBlockState())
				)
			)
		);

		PATCH_TUBE_WORMS.makeAndSetHolder(Feature.RANDOM_PATCH,
			new RandomPatchConfiguration(
				12,
				3,
				4,
				PlacementUtils.inlinePlaced(
					WWFeatures.TUBE_WORMS_FEATURE,
					NoneFeatureConfiguration.INSTANCE
				)
			)
		);

		HYDROTHERMAL_VENT.makeAndSetHolder(FrozenLibFeatures.UNDERWATER_VEGETATION_PATCH,
			new VegetationPatchConfiguration(
				WWBlockTags.HYDROTHERMAL_VENT_REPLACEABLE,
				BlockStateProvider.simple(WWBlocks.GABBRO),
				PlacementUtils.inlinePlaced(
					WWFeatures.HYDROTHERMAL_VENT_FEATURE,
					NoneFeatureConfiguration.INSTANCE
				),
				CaveSurface.FLOOR,
				ConstantInt.of(2),
				0.375F,
				6,
				0.25F,
				UniformInt.of(1, 2),
				0.5F
			)
		);

		HYDROTHERMAL_VENT_TUBE_WORMS.makeAndSetHolder(FrozenLibFeatures.COMBO_FEATURE,
			new ComboFeatureConfig(
				List.of(
					PlacementUtils.inlinePlaced(HYDROTHERMAL_VENT.getHolder()),
					PlacementUtils.inlinePlaced(
						Feature.RANDOM_PATCH,
						new RandomPatchConfiguration(
							33,
							5,
							4,
							PlacementUtils.inlinePlaced(
								WWFeatures.TUBE_WORMS_FEATURE,
								NoneFeatureConfiguration.INSTANCE
							)
						)
					)
				)
			)
		);

		OCEAN_MOSS.makeAndSetHolder(FrozenLibFeatures.NOISE_PATH_FEATURE,
			new NoisePathFeatureConfig(
				new NoiseBandPlacement.Builder(EasyNoiseSampler.NoiseType.CHECKED)
					.noiseScale(0.1D)
					.calculateNoiseWithY()
					.scaleYNoise()
					.heightmapType(Heightmap.Types.OCEAN_FLOOR_WG)
					.noiseBandBlockPlacements(
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.MOSS_BLOCK))
							.within(0.4D, 0.9D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.OCEAN_MOSS_REPLACEABLE))
							.searchingBlockPredicate(SearchInDirectionBlockPredicate.hasWaterAbove(1))
							.placementChance(0.915F)
							.build(),
						new NoiseBandBlockPlacement.Builder(BlockStateProvider.simple(Blocks.MOSS_BLOCK))
							.within(-0.9D, -0.4D)
							.replacementBlockPredicate(BlockPredicate.matchesTag(WWBlockTags.OCEAN_MOSS_REPLACEABLE))
							.searchingBlockPredicate(SearchInDirectionBlockPredicate.hasWaterAbove(1))
							.placementChance(0.915F)
							.build()
					).build(),
				12
			)
		);
	}
}
