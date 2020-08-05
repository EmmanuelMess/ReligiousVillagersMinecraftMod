package net.fabricmc.example.mixin;

import net.minecraft.entity.ai.brain.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(Schedule.class)
public interface ScheduleAccessor {
    @Accessor("VILLAGER_DEFAULT")
    static void setVillagerDefault(Schedule _) {
        throw new UnsupportedOperationException();
    }
}
