package net.fabricmc.example.mixin;

import com.google.common.collect.*;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.world.poi.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;
import java.util.function.*;

@Mixin(VillagerEntity.class)
public interface VillagerEntityAccessor {
    @Accessor("POINTS_OF_INTEREST")
    static void setPointsOfInterest(Map<MemoryModuleType<GlobalPos>, BiPredicate<VillagerEntity, PointOfInterestType>> _) {
        throw new UnsupportedOperationException();
    }

    @Accessor("MEMORY_MODULES")
    static void setMemoryModules(ImmutableList<MemoryModuleType<?>> _) {
        throw new UnsupportedOperationException();
    }

    @Accessor("MEMORY_MODULES")
    static ImmutableList<MemoryModuleType<?>> getMemoryModules() {
        throw new UnsupportedOperationException();
    }
}