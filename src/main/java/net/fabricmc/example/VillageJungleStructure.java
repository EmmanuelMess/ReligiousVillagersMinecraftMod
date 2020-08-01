package net.fabricmc.example;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.biome.source.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.feature.*;

import static net.fabricmc.example.RegistryStartFeatures.*;

public class VillageJungleStructure extends AbstractVillageStructure {
    public VillageJungleStructure(Codec<DefaultFeatureConfig> config) {
        super(config);
    }


    @Override
    public StructureFeature<DefaultFeatureConfig> getVillageInstance() {
        return JUNGLE_VILLAGE;
    }

    public StructureFeature.StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
        return VillageJungleStructure.Start::new;
    }

    public static class Start extends AbstractStart {
        public Start(StructureFeature<DefaultFeatureConfig> structureIn, int chunkX, int chunkZ, BlockBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        public static Identifier VILLAGE_IDENTIFIER = new Identifier(ExampleMod.MODID + ":village/jungle/town_centers");

        @Override
        public Identifier getIdentifier() {
            return VILLAGE_IDENTIFIER;
        }

        @Override
        public int getSize() {
            return 8;
        }
    }
}