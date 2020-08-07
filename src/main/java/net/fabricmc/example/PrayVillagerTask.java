package net.fabricmc.example;

import com.google.common.collect.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.*;
import net.minecraft.server.world.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.util.math.*;

import java.util.*;

public class PrayVillagerTask extends Task<VillagerEntity> {
    public PrayVillagerTask() {
        super(ImmutableMap.of(ReligiousVillagersMod.MOSQUE_POINT, MemoryModuleState.VALUE_PRESENT));
    }

    protected boolean shouldRun(ServerWorld world, VillagerEntity entity) {
        if (entity.hasVehicle()) {
            return false;
        } else {
            Brain<?> brain = entity.getBrain();
            GlobalPos globalPos = brain.getOptionalMemory(ReligiousVillagersMod.MOSQUE_POINT).get();

            return globalPos.getPos().isWithinDistance(entity.getPos(), 2.0);
        }
    }

    protected boolean shouldKeepRunning(ServerWorld world, VillagerEntity entity, long time) {
        Optional<GlobalPos> optional = entity.getBrain().getOptionalMemory(ReligiousVillagersMod.MOSQUE_POINT);
        BlockPos blockPos = optional.get().getPos();
        return entity.getBrain().hasActivity(ReligiousVillagersMod.PRAY)
                && entity.getY() > (double)blockPos.getY() + 0.4D
                && blockPos.isWithinDistance(entity.getPos(), 1.14D);
    }

    protected void run(ServerWorld world, VillagerEntity entity, long time) {
        ReligiousVillagersMod.LOGGER.info("Pray:run!");

        entity.getBrain().getOptionalMemory(MemoryModuleType.OPENED_DOORS).ifPresent((set) -> {
            OpenDoorsTask.closeOpenedDoors(world, ImmutableList.of(), 0, entity, entity.getBrain());
        });
        pray(entity);
    }

    protected boolean isTimeLimitExceeded(long time) {
        return false;
    }

    public void pray(LivingEntity entity) {
        if (entity.hasVehicle()) {
            entity.stopRiding();
        }

        entity.setPose(EntityPose.STANDING);
        entity.setVelocity(Vec3d.ZERO);
        entity.velocityDirty = true;
    }
}
