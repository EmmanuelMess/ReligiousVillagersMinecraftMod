package net.fabricmc.example;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.feature.*;

public class ExampleStructureStart extends StructureStart<DefaultFeatureConfig> {

    private static final Identifier BASE_POOL = new Identifier("tutorial:base_pool");
    private static final Identifier COLOR_POOL = new Identifier("tutorial:color_pool");

    static {
        StructurePoolBasedGenerator.REGISTRY.add(
                new StructurePool(
                        BASE_POOL,
                        new Identifier("empty"),
                        ImmutableList.of(
                                com.mojang.datafixers.util.Pair.of(new SinglePoolElement("tutorial:black_square"), 1),
                                com.mojang.datafixers.util.Pair.of(new SinglePoolElement("tutorial:white_square"), 1)
                        ),
                        StructurePool.Projection.RIGID
                )
        );

        StructurePoolBasedGenerator.REGISTRY.add(
                new StructurePool(
                        COLOR_POOL,
                        new Identifier("empty"),
                        ImmutableList.of(
                                com.mojang.datafixers.util.Pair.of(new SinglePoolElement("tutorial:lime_square"), 1),
                                com.mojang.datafixers.util.Pair.of(new SinglePoolElement("tutorial:magenta_square"), 1),
                                com.mojang.datafixers.util.Pair.of(new SinglePoolElement("tutorial:orange_square"), 1),
                                Pair.of(new SinglePoolElement("tutorial:light_blue_square"), 1)
                        ),
                        StructurePool.Projection.RIGID
                )
        );
    }

    ExampleStructureStart(StructureFeature<DefaultFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box,
                          int references, long seed) {
        super(feature, chunkX, chunkZ, box, references, seed);
    }

    public void init(ChunkGenerator chunkGenerator, StructureManager structureManager, int x, int z,
                     Biome biome, DefaultFeatureConfig featureConfig) {
        StructurePoolBasedGenerator.addPieces(BASE_POOL, 7, ExamplePiece::new, chunkGenerator, structureManager,
                new BlockPos(x * 16, 150, z * 16), children, random, true, true);
        setBoundingBoxFromChildren();
    }
}