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

package net.frozenblock.wilderwild.worldgen.modification;

import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.frozenblock.wilderwild.WWConstants;
import net.frozenblock.wilderwild.config.WWWorldgenConfig;
import net.frozenblock.wilderwild.tag.WWBiomeTags;
import net.frozenblock.wilderwild.worldgen.features.placed.WWAquaticPlaced;
import net.frozenblock.wilderwild.worldgen.features.placed.WWPlacedFeatures;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.GenerationStep;

public final class WWVegetationGeneration {

	public static void generateFlower() {
		BiomeModifications.create(WWConstants.id("flower_generation"))
			.add(ModificationPhase.ADDITIONS,
				BiomeSelectors.all(),
				(biomeSelectionContext, context) -> {
					if (WWWorldgenConfig.get().vegetation.flowerGeneration) {
						BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_CARNATION)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.CARNATION.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_MARIGOLD)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.MARIGOLD.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_MARIGOLD_SPARSE)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.MARIGOLD_SPARSE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_EYEBLOSSOM)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.EYEBLOSSOM.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_PINK_TULIP_UNCOMMON)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PINK_TULIP_UNCOMMON.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_ALLIUM_UNCOMMON)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.ALLIUM_UNCOMMON.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_DATURA)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.DATURA.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_ROSE_BUSH)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.ROSE_BUSH.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_PEONY)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PEONY.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_LILAC)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.LILAC.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_CHERRY_FLOWERS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.FLOWER_CHERRY.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_HIBISCUS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.HIBISCUS.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_SPARSE_JUNGLE_FLOWERS)) {
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.FLOWER_SPARSE_JUNGLE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_JUNGLE_FLOWERS)) {
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.FLOWER_JUNGLE.getKey());
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.TALL_FLOWER_JUNGLE.getKey());
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.HIBISCUS_JUNGLE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_SPARSE_JUNGLE_FLOWERS)) {
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.HIBISCUS_SPARSE_JUNGLE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FLOWERING_WATER_LILY)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWAquaticPlaced.PATCH_FLOWERING_WATERLILY.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_SEEDING_DANDELION)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.SEEDING_DANDELION.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_COMMON_SEEDING_DANDELION)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.COMMON_SEEDING_DANDELION.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_RARE_SEEDING_DANDELION)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.RARE_SEEDING_DANDELION.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_VERY_RARE_SEEDING_DANDELION)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.VERY_RARE_SEEDING_DANDELION.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_MILKWEED)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.MILKWEED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_BERRY_PATCH)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_BERRY_FOREST.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FIELD_FLOWERS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.FLOWER_FLOWER_FIELD.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_CYPRESS_FLOWERS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.CYPRESS_WETLANDS_FLOWERS.getKey());
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.CYPRESS_WETLANDS_FLOWERS_TALL.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_RARE_MILKWEED)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.MILKWEED_RARE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.MEADOW)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.FLOWER_MEADOW.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_BIRCH_CLEARING_FLOWERS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.FLOWER_BIRCH_CLEARING.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FOREST_CLEARING_FLOWERS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.FLOWER_FOREST_CLEARING.getKey());
						}
					}
				});

		BiomeModifications.create(WWConstants.id("flower_replacement_generation"))
			.add(ModificationPhase.REPLACEMENTS,
				BiomeSelectors.all(),
				(biomeSelectionContext, context) -> {
					if (WWWorldgenConfig.get().flowerGeneration) {
						BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_WILDFLOWERS)) {
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_BIRCH_FOREST);
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_MEADOW);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.WILDFLOWERS.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_PHLOX)) {
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_BIRCH_FOREST);
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_MEADOW);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PHLOX.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_PHLOX_SPARSE)) {
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_BIRCH_FOREST);
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_MEADOW);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PHLOX_SPARSE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_LANTANAS)) {
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_BIRCH_FOREST);
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_MEADOW);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.LANTANAS.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_LANTANAS_SPARSE)) {
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_BIRCH_FOREST);
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_MEADOW);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.LANTANAS_SPARSE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_WILDFLOWERS_SPARSE)) {
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_BIRCH_FOREST);
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_MEADOW);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.WILDFLOWERS_SPARSE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_WILDFLOWERS_AND_PHLOX)) {
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_BIRCH_FOREST);
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_MEADOW);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.WILDFLOWERS_AND_PHLOX.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_WILDFLOWERS_AND_PHLOX_SPARSE)) {
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_BIRCH_FOREST);
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_MEADOW);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.WILDFLOWERS_AND_PHLOX_SPARSE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_WILDFLOWERS_AND_LANTANAS)) {
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_BIRCH_FOREST);
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_MEADOW);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.WILDFLOWERS_AND_LANTANAS.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_LANTANAS_AND_PHLOX)) {
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_BIRCH_FOREST);
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_MEADOW);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.LANTANAS_AND_PHLOX.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_LANTANAS_AND_PHLOX_SPARSE)) {
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_BIRCH_FOREST);
							generationSettings.removeFeature(VegetationPlacements.WILDFLOWERS_MEADOW);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.LANTANAS_AND_PHLOX_SPARSE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_PLAINS_FLOWERS)) {
							generationSettings.removeFeature(VegetationPlacements.FLOWER_PLAINS);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.FLOWER_PLAINS.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_TUNDRA_FLOWERS)) {
							generationSettings.removeFeature(VegetationPlacements.FOREST_FLOWERS);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.FLOWER_TUNDRA.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_GENERIC_FLOWERS)) {
							generationSettings.removeFeature(VegetationPlacements.FLOWER_DEFAULT);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.FLOWER_GENERIC.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_GENERIC_FLOWERS_NO_CARNATIONS)) {
							generationSettings.removeFeature(VegetationPlacements.FLOWER_DEFAULT);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.FLOWER_GENERIC_NO_CARNATION.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_BIRCH_FLOWERS)) {
							generationSettings.removeFeature(VegetationPlacements.FLOWER_DEFAULT);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.FLOWER_BIRCH.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_SUNFLOWER_PLAINS_FLOWERS)) {
							generationSettings.removeFeature(VegetationPlacements.FLOWER_PLAINS);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.FLOWER_SUNFLOWER_PLAINS.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_SNOWY_PLAINS_FLOWERS)) {
							generationSettings.removeFeature(VegetationPlacements.FLOWER_PLAINS);
							generationSettings.removeFeature(VegetationPlacements.FLOWER_DEFAULT);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.FLOWER_SNOWY_PLAINS.getKey());
						}
					}
				});
	}

	public static void generateBush() {
		BiomeModifications.create(WWConstants.id("bush_generation"))
			.add(ModificationPhase.ADDITIONS,
				BiomeSelectors.all(),
				(biomeSelectionContext, context) -> {
					if (WWWorldgenConfig.get().vegetation.shrubGeneration) {
						BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_SHRUB)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.GENERIC_SHRUB_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_JUNGLE_SHRUB)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.JUNGLE_SHRUB_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_SPARSE_SHRUB)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.SPARSE_SHRUB_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_BADLANDS_SAND_SHRUB)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.BADLANDS_SHRUB_SAND_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_BADLANDS_TERRACOTTA_SHRUB)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.BADLANDS_SHRUB_TERRACOTTA_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_WOODED_BADLANDS_SHRUB)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.WOODED_BADLANDS_SHRUB_TERRACOTTA_PLACED.getKey());
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.WOODED_BADLANDS_SHRUB_DIRT_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_BADLANDS_RARE_SAND_SHRUB)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.BADLANDS_SHRUB_RARE_SAND_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_DESERT_SHRUB)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.DESERT_SHRUB_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_OASIS_SHRUB)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.OASIS_SHRUB_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_ARID_SHRUB)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.ARID_BUSH_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FLOWER_FIELD_SHRUB)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.FLOWER_FIELD_SHRUB_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_RAINFOREST_SHRUB)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.GENERIC_SHRUB_PLACED.getKey());
						}
					}
			})
			.add(ModificationPhase.REPLACEMENTS,
				BiomeSelectors.all(),
				(biomeSelectionContext, context) -> {
					if (WWWorldgenConfig.get().shrubGeneration) {
						BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FROZEN_BUSH)) {
							generationSettings.removeFeature(VegetationPlacements.PATCH_BUSH);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_FROZEN_BUSH.getKey());
						}
					}
				});

		BiomeModifications.create(WWConstants.id("firefly_bush_generation"))
			.add(ModificationPhase.REPLACEMENTS,
				BiomeSelectors.all(),
				(biomeSelectionContext, context) -> {
					if (WWWorldgenConfig.get().fireflyBushGen) {
						BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FIREFLY_BUSH)) {
							generationSettings.removeFeature(VegetationPlacements.PATCH_FIREFLY_BUSH_NEAR_WATER);
							generationSettings.removeFeature(VegetationPlacements.PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP);
							generationSettings.removeFeature(VegetationPlacements.PATCH_FIREFLY_BUSH_SWAMP);

							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_FIREFLY_BUSH_NEAR_WATER.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FIREFLY_BUSH_SWAMP)) {
							generationSettings.removeFeature(VegetationPlacements.PATCH_FIREFLY_BUSH_NEAR_WATER);
							generationSettings.removeFeature(VegetationPlacements.PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP);
							generationSettings.removeFeature(VegetationPlacements.PATCH_FIREFLY_BUSH_SWAMP);

							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP.getKey());
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_FIREFLY_BUSH_SWAMP.getKey());
						}
					}
				});
	}

	public static void generateCacti() {
		BiomeModifications.create(WWConstants.id("cactus_generation"))
			.add(ModificationPhase.ADDITIONS,
				BiomeSelectors.all(),
				(biomeSelectionContext, context) -> {
					if (WWWorldgenConfig.get().vegetation.cactusGeneration) {
						BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_TALL_CACTUS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.TALL_CACTUS_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_PRICKLY_PEAR)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PRICKLY_PEAR.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_RARE_PRICKLY_PEAR)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PRICKLY_PEAR_RARE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_TALL_BADLANDS_CACTUS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.BADLANDS_TALL_CACTUS_PLACED.getKey());
						}
					}
				});
	}

	public static void generateGrass() {
		BiomeModifications.create(WWConstants.id("tumbleweed_generation"))
			.add(ModificationPhase.ADDITIONS,
				BiomeSelectors.all(),
				(biomeSelectionContext, context) -> {
					if (WWWorldgenConfig.get().vegetation.tumbleweed) {
						BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_TUMBLEWEED_PLANT)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.TUMBLEWEED.getKey());
						}
					}
				});

		BiomeModifications.create(WWConstants.id("grass_generation"))
			.add(ModificationPhase.ADDITIONS,
				BiomeSelectors.all(),
				(biomeSelectionContext, context) -> {
					if (WWWorldgenConfig.get().vegetation.grassGeneration) {
						BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_CLOVERS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.CLOVERS.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_CLOVERS_SPARSE)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.CLOVERS_SPARSE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_NEW_RARE_GRASS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.RARE_GRASS_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_LARGE_FERN_AND_GRASS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.LARGE_FERN_AND_GRASS.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_LARGE_FERN_AND_GRASS_RARE)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.LARGE_FERN_AND_GRASS_RARE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FLOWER_FIELD_TALL_GRASS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_TALL_GRASS_FLOWER_FIELD.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_SWAMP_FERN)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.SWAMP_FERN.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_DENSE_FERN)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.DENSE_FERN_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_DENSE_TALL_GRASS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.DENSE_TALL_GRASS_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_SWAMP_TALL_GRASS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.SWAMP_TALL_GRASS_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_WATER_GRASS)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.TALL_GRASS_AND_GRASS_WATER.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_MYCELIUM_GROWTH)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.MYCELIUM_GROWTH_PLACED.getKey());
						}
					}
				})
			.add(ModificationPhase.REPLACEMENTS,
				BiomeSelectors.all(),
				(biomeSelectionContext, context) -> {
					if (WWWorldgenConfig.get().vegetation.grassGeneration) {
						BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FROZEN_FOREST_GRASS)) {
							generationSettings.removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_GRASS_FROZEN_FOREST.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FROZEN_NORMAL_GRASS)) {
							generationSettings.removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_NORMAL);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_GRASS_FROZEN_NORMAL.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FROZEN_PLAIN_GRASS)) {
							generationSettings.removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_BADLANDS);
							generationSettings.removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_GRASS_FROZEN_PLAIN.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FROZEN_TAIGA_GRASS)) {
							generationSettings.removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_TAIGA);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_GRASS_FROZEN_TAIGA.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FROZEN_TAIGA_2_GRASS)) {
							generationSettings.removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_TAIGA_2);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_GRASS_FROZEN_TAIGA_2.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FROZEN_PLAIN_TALL_GRASS)) {
							generationSettings.removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS_2);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_FROZEN_TALL_GRASS_2.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_FROZEN_LARGE_FERNS)) {
							generationSettings.removeFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_LARGE_FERN);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_FROZEN_LARGE_FERN.getKey());
						}
					}
				});

		BiomeModifications.create(WWConstants.id("dry_grass_generation"))
			.add(ModificationPhase.REPLACEMENTS,
				BiomeSelectors.all(),
				(biomeSelectionContext, context) -> {
					if (WWWorldgenConfig.get().grassGeneration) {
						BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_DRY_GRASS_BADLANDS)) {
							generationSettings.removeFeature(VegetationPlacements.PATCH_DRY_GRASS_BADLANDS);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_DRY_GRASS_BADLANDS.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_DRY_GRASS_DESERT)) {
							generationSettings.removeFeature(VegetationPlacements.PATCH_DRY_GRASS_DESERT);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_DRY_GRASS_DESERT.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_DRY_GRASS_BEACH)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_DRY_GRASS_BEACH.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_DRY_GRASS_BETA_BEACH)) {
							generationSettings.removeFeature(VegetationPlacements.PATCH_DRY_GRASS_DESERT);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_DRY_GRASS_BETA_BEACH.getKey());
						}
					}
				});

		BiomeModifications.create(WWConstants.id("leaf_litter_generation"))
			.add(ModificationPhase.REPLACEMENTS,
				BiomeSelectors.all(),
				(biomeSelectionContext, context) -> {
					if (WWWorldgenConfig.get().leafLitterGen) {
						BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_LEAF_LITTER)) {
							generationSettings.removeFeature(VegetationPlacements.PATCH_LEAF_LITTER);
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_LEAF_LITTER.getKey());
						}
					}
				});
	}

	public static void generateMushroom() {
		BiomeModifications.create(WWConstants.id("mushroom_generation"))
			.add(ModificationPhase.ADDITIONS,
				BiomeSelectors.all(),
				(biomeSelectionContext, context) -> {
					if (WWWorldgenConfig.get().vegetation.mushroomGeneration) {
						BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_HUGE_RED_MUSHROOM)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.HUGE_RED_MUSHROOM_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_HUGE_BROWN_MUSHROOM)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.HUGE_BROWN_MUSHROOM_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_HUGE_PALE_MUSHROOM)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.HUGE_PALE_MUSHROOM_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_BIG_MUSHROOMS)) {
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.MUSHROOM_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_COMMON_BROWN_MUSHROOM)) {
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.BROWN_MUSHROOM_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_COMMON_RED_MUSHROOM)) {
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.RED_MUSHROOM_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_PALE_MUSHROOM)) {
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.PALE_MUSHROOM_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_SWAMP_MUSHROOM)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.HUGE_MUSHROOMS_SWAMP.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_BIG_MUSHROOM_PATCH)) {
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.DARK_FOREST_MUSHROOM_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_CRIMSON_SHELF_FUNGI)) {
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.CRIMSON_SHELF_FUNGI.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_WARPED_SHELF_FUNGI)) {
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.WARPED_SHELF_FUNGI.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_CRIMSON_SHELF_FUNGI_RARE)) {
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.CRIMSON_SHELF_FUNGI_RARE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_WARPED_SHELF_FUNGI_RARE)) {
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.WARPED_SHELF_FUNGI_RARE.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_RAINFOREST_MUSHROOM)) {
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.RAINFOREST_MUSHROOMS_PLACED.getKey());
						}

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_MIXED_MUSHROOM)) {
							generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, WWPlacedFeatures.MIXED_MUSHROOMS_PLACED.getKey());
						}
					}
				});
	}

	public static void generatePumpkin() {
		BiomeModifications.create(WWConstants.id("pumpkin_generation"))
			.add(ModificationPhase.ADDITIONS,
				BiomeSelectors.all(),
				(biomeSelectionContext, context) -> {
					if (WWWorldgenConfig.get().vegetation.pumpkin) {
						BiomeModificationContext.GenerationSettingsContext generationSettings = context.getGenerationSettings();

						if (biomeSelectionContext.hasTag(WWBiomeTags.HAS_COMMON_PUMPKIN)) {
							generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WWPlacedFeatures.PATCH_PUMPKIN_COMMON.getKey());
						}
					}
				});
	}

}
