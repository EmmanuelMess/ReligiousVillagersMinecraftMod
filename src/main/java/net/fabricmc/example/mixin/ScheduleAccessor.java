package net.fabricmc.example.mixin;

import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.world.poi.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;
import java.util.function.*;

@Mixin(Schedule.class)
public interface ScheduleAccessor {
    @Accessor("VILLAGER_DEFAULT")
    static void setVillagerDefault(Schedule _) {
        throw new UnsupportedOperationException();
    }
}
