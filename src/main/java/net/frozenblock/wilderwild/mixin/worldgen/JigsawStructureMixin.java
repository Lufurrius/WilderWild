package net.frozenblock.wilderwild.mixin.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Function;

@Mixin(JigsawStructure.class)
public class JigsawStructureMixin {
    @Shadow @Final @Mutable
    public static int MAX_TOTAL_STRUCTURE_RANGE = 256;

    @Shadow @Final @Mutable
    public static Codec<JigsawStructure> CODEC = RecordCodecBuilder.<JigsawStructure>mapCodec((instance) -> {
        return instance.group(Structure.StructureSettings.CODEC.forGetter(structure -> structure.settings), StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter((jigsawStructure) -> {
            return jigsawStructure.startPool;
        }), ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter((jigsawStructure) -> {
            return jigsawStructure.startJigsawName;
        }), Codec.intRange(0, 15).fieldOf("size").forGetter((jigsawStructure) -> {
            return jigsawStructure.maxDepth;
        }), HeightProvider.CODEC.fieldOf("start_height").forGetter((jigsawStructure) -> {
            return jigsawStructure.startHeight;
        }), Codec.BOOL.fieldOf("use_expansion_hack").forGetter((jigsawStructure) -> {
            return jigsawStructure.useExpansionHack;
        }), Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter((jigsawStructure) -> {
            return jigsawStructure.projectStartToHeightmap;
        }), Codec.intRange(1, MAX_TOTAL_STRUCTURE_RANGE).fieldOf("max_distance_from_center").forGetter((jigsawStructure) -> {
            return jigsawStructure.maxDistanceFromCenter;
        })).apply(instance, JigsawStructure::new);
    }).flatXmap(verifyRange(), verifyRange()).codec();


    private static Function<JigsawStructure, DataResult<JigsawStructure>> verifyRange() {
        return (jigsawStructure) -> {

            int i = switch (jigsawStructure.terrainAdaptation()) {
                case NONE -> 0;
                case BURY, BEARD_THIN, BEARD_BOX -> 12;
            };
            return jigsawStructure.maxDistanceFromCenter + i > MAX_TOTAL_STRUCTURE_RANGE ? DataResult.error("Structure size including terrain adaptation must not exceed " + MAX_TOTAL_STRUCTURE_RANGE) : DataResult.success(jigsawStructure);
        };
    }
}