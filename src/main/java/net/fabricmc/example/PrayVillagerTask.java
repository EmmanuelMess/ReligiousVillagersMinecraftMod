package net.fabricmc.example;

import com.google.common.collect.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.server.world.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.util.math.*;

import java.util.*;

public class PrayVillagerTask extends Task<LivingEntity> {
    public PrayVillagerTask() {
        super(ImmutableMap.of());
    }

    protected boolean shouldRun(ServerWorld world, LivingEntity entity) {
        if(!entity.getBrain().getOptionalMemory(ExampleMod.MOSQUE_POINT).isPresent()) {
            entity.getBrain().remember(ExampleMod.MOSQUE_POINT, GlobalPos.create(world.getRegistryKey(), new BlockPos(0, 4, 0)));
        }

        if (entity.hasVehicle()) {
            return false;
        } else {
            Brain<?> brain = entity.getBrain();
            GlobalPos globalPos = brain.getOptionalMemory(ExampleMod.MOSQUE_POINT).get();

            return world.getRegistryKey() == globalPos.getDimension();
        }
    }

    protected boolean shouldKeepRunning(ServerWorld world, LivingEntity entity, long time) {
        Optional<GlobalPos> optional = entity.getBrain().getOptionalMemory(ExampleMod.MOSQUE_POINT);
        if (!optional.isPresent()) {
            return false;
        } else {
            BlockPos blockPos = optional.get().getPos();
            return entity.getBrain().hasActivity(ExampleMod.PRAY)
                    && entity.getY() > (double)blockPos.getY() + 0.4D
                    && blockPos.isWithinDistance(entity.getPos(), 1.14D);
        }
    }

    protected void run(ServerWorld world, LivingEntity entity, long time) {
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
