package net.fabricmc.example.mixin;

import com.google.common.collect.*;
import com.mojang.datafixers.util.*;
import net.fabricmc.example.*;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.passive.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static net.fabricmc.example.ExampleMod.*;

@Mixin(VillagerEntity.class)
public class ExampleMixin {
	@Inject(at = @At("HEAD"), method = "initBrain(Lnet/minecraft/entity/ai/brain/Brain;)V", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void initBrain(Brain<VillagerEntity> brain, CallbackInfo info) {
		VillagerEntity $this = (VillagerEntity) (Object) this;

		System.out.println("initBrain: " + $this.toString());

		if (!$this.isBaby()) {
			brain.setTaskList(
					ExampleMod.PRAY,
					createPrayTasks(0.5F),
					ImmutableSet.of()
			);
		}
	}

	@Inject(at = @At("RETURN"), method = "initBrain(Lnet/minecraft/entity/ai/brain/Brain;)V", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void initBrainReturn(Brain<VillagerEntity> brain, CallbackInfo info) {
		if(brain.getSchedule() != null) {
			System.out.println("schedule: " + brain.getSchedule().toString());
			System.out.println("Schedule.SIMPLE: " + Schedule.SIMPLE);
			System.out.println("Schedule.VILLAGER: " + Schedule.VILLAGER_DEFAULT);
			System.out.println("initBrain: " + brain.getSchedule().getActivityForTime(21));
		}
	}
}
