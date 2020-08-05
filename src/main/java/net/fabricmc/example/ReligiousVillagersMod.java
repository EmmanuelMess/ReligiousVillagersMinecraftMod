package net.fabricmc.example;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.*;
import net.fabricmc.example.mixin.*;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.poi.*;

import java.util.*;
import java.util.function.*;

public class ReligiousVillagersMod implements ModInitializer {
	@Override
	public void onInitialize() {
	}

	public static ImmutableList<com.mojang.datafixers.util.Pair<Integer, ? extends Task<? super VillagerEntity>>> createPrayTasks(float speed) {
		return ImmutableList.of(
				Pair.of(2, new VillagerWalkTowardsTask(ReligiousVillagersMod.MOSQUE_POINT, speed, 1, 1500000, 1200)),
				//Pair.of(3, new ForgetCompletedPointOfInterestTask(PointOfInterestType.HOME, MemoryModuleType.HOME)),
				Pair.of(3, new PrayVillagerTask()),
				Pair.of(5, new RandomTask(
						ImmutableMap.of(),
						ImmutableList.of(
								Pair.of(new WalkHomeTask(speed), 1),
								Pair.of(new WanderIndoorsTask(speed), 4),
								Pair.of(new GoToPointOfInterestTask(speed, 4), 2),
								Pair.of(new WaitTask(20, 40), 2)
						)
				)),
				//VillagerTaskListProvider.createBusyFollowTask(),
				Pair.of(99, new ScheduleActivityTask())
		);
	}

	public static final Activity PRAY = Registry.register(Registry.ACTIVITY, "pray", new Activity("pray"));
	public static final MemoryModuleType<GlobalPos> MOSQUE_POINT = Registry.register(
			Registry.MEMORY_MODULE_TYPE,
			new Identifier("mosque_point"),
			new MemoryModuleType<>(Optional.of(GlobalPos.CODEC))
	);

	static {
		addScheduled();
		addMemoryModules();
		addPointsOfInterest();
	}

	private static void addScheduled() {
		ScheduleAccessor.setVillagerDefault(
				new ScheduleBuilder(Schedule.VILLAGER_DEFAULT).withActivity(10, ReligiousVillagersMod.PRAY).build());
	}

	private static void addMemoryModules() {
		VillagerEntityAccessor.setMemoryModules(new ImmutableList.Builder<MemoryModuleType<?>>()
				.addAll(VillagerEntityAccessor.getMemoryModules()).add(MOSQUE_POINT).build());
	}

	private static void addPointsOfInterest() {
		VillagerEntityAccessor.setPointsOfInterest(
				new ImmutableMap.Builder<MemoryModuleType<GlobalPos>, BiPredicate<VillagerEntity, PointOfInterestType>>()
						.putAll(VillagerEntity.POINTS_OF_INTEREST)
						.put(MOSQUE_POINT, (villagerEntity, pointOfInterestType) -> {
							return pointOfInterestType == PointOfInterestType.MEETING;
						})
						.build());
	}
}
