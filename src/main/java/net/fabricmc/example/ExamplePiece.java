package net.fabricmc.example;

import net.minecraft.nbt.*;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

import static net.fabricmc.example.ExampleMod.*;

public class ExamplePiece extends PoolStructurePiece {

    public ExamplePiece(StructureManager manager, StructurePoolElement poolElement, BlockPos pos,
                 int groundLevelDelta, BlockRotation rotation, BlockBox boundingBox) {
        super(EXAMPLE_PIECE, manager, poolElement, pos, groundLevelDelta, rotation, boundingBox);
    }


    public ExamplePiece(StructureManager structureManager, CompoundTag compoundTag) {
        super(structureManager, compoundTag, EXAMPLE_PIECE);
    }
}