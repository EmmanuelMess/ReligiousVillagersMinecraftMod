package net.fabricmc.example;

import com.google.common.collect.*;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.passive.*;
import net.minecraft.server.world.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.world.poi.*;

public class FindTempleTask extends Task<VillagerEntity> {
    public FindTempleTask() {
        super(ImmutableMap.of(ReligiousVillagersMod.MOSQUE_POINT, MemoryModuleState.VALUE_ABSENT));
    }

    protected boolean shouldRun(ServerWorld world, VillagerEntity entity) {
        ReligiousVillagersMod.LOGGER.info("shouldRun!");

        return world.getPointOfInterestStorage()
                .getNearestPosition(
                        ReligiousVillagersMod.BELIEVER.getCompletionCondition(),
                        entity.getBlockPos(),
                        48,
                        PointOfInterestStorage.OccupationStatus.ANY
                ).isPresent();
    }

    protected void run(ServerWorld world, VillagerEntity entity, long time) {
        ReligiousVillagersMod.LOGGER.info("run!");

        world.getPointOfInterestStorage()
                .getPositions(
                        ReligiousVillagersMod.BELIEVER.getCompletionCondition(),
                        (blockPos) -> {
                            Path path = entity
                                    .getNavigation()
                                    .findPathTo(blockPos, ReligiousVillagersMod.BELIEVER.getSearchDistance());
                            return (path != null && path.reachesTarget());
                        },
                        entity.getBlockPos(),
                        48,
                        PointOfInterestStorage.OccupationStatus.ANY
                )
                .findAny()
                .ifPresent(blockPos -> {
                    GlobalPos globalPos = GlobalPos.create(world.getRegistryKey(), blockPos);
                    entity.getBrain().remember(ReligiousVillagersMod.MOSQUE_POINT, globalPos);
                });
    }
}
