package net.frozenblock.wilderwild.world.feature;

import net.frozenblock.wilderwild.WilderWild;
import net.frozenblock.wilderwild.registry.RegisterBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.List;


public class WildConfiguredFeatures {
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> NEW_FALLEN_BIRCH_AND_OAK =
            ConfiguredFeatures.register("new_fallen_birch_and_oak", Feature.RANDOM_SELECTOR,
                    new RandomFeatureConfig(List.of(new RandomFeatureEntry(WildTreePlaced.NEW_FALLEN_BIRCH_CHECKED, 0.35F)), WildTreePlaced.NEW_FALLEN_OAK_CHECKED));
    public static final RegistryEntry<ConfiguredFeature<RandomFeatureConfig, ?>> NEW_TREES_BIRCH_AND_OAK =
            ConfiguredFeatures.register("new_trees_birch_and_oak", Feature.RANDOM_SELECTOR,
                    new RandomFeatureConfig(List.of(new RandomFeatureEntry(WildTreePlaced.NEW_BIRCH_BEES_0002, 0.2F),
                            new RandomFeatureEntry(WildTreePlaced.NEW_FANCY_OAK_BEES_0002, 0.27F)), WildTreePlaced.NEW_OAK_BEES_0002));

    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> NEW_GRASS_FOREST =
            ConfiguredFeatures.register("new_grass_forest", Feature.RANDOM_PATCH,
                    ConfiguredFeatures.createRandomPatchFeatureConfig(64, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                            new SimpleBlockFeatureConfig(BlockStateProvider.of(Blocks.GRASS)))));

    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> DATURA =
            ConfiguredFeatures.register("datura", Feature.FLOWER,
                    ConfiguredFeatures.createRandomPatchFeatureConfig(64, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                            new SimpleBlockFeatureConfig(BlockStateProvider.of(RegisterBlocks.DATURA)))));

    public static final RegistryEntry<ConfiguredFeature<RandomPatchFeatureConfig, ?>> CARNATION =
            ConfiguredFeatures.register("carnation", Feature.FLOWER,
                    ConfiguredFeatures.createRandomPatchFeatureConfig(64, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                            new SimpleBlockFeatureConfig(BlockStateProvider.of(RegisterBlocks.CARNATION)))));

    public static final RegistryEntry<ConfiguredFeature<MultifaceGrowthFeatureConfig, ?>> POLLEN_CONFIGURED =
            ConfiguredFeatures.register("pollen", Feature.MULTIFACE_GROWTH, new MultifaceGrowthFeatureConfig(RegisterBlocks.POLLEN_BLOCK, 20, true, true, true, 0.5F, RegistryEntryList.of(Block::getRegistryEntry, Blocks.GRASS_BLOCK, Blocks.BIRCH_LEAVES, Blocks.OAK_LEAVES, Blocks.OAK_LOG)));
    public static void registerConfiguredFeatures() {
        System.out.println("Registering WildConfiguredFeatures for " + WilderWild.MOD_ID);
    }
}

