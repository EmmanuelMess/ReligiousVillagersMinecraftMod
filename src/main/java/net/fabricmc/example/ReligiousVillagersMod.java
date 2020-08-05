package net.fabricmc.example;

import com.google.common.collect.*;
import com.mojang.datafixers.util.*;
import net.fabricmc.api.*;
import net.fabricmc.example.mixin.*;
import net.minecraft.block.*;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.world.poi.*;
import org.apache.logging.log4j.*;

import java.util.function.*;

public class ReligiousVillagersMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void onInitialize() {
	}

	public static ImmutableList<com.mojang.datafixers.util.Pair<Integer, ? extends Task<? super VillagerEntity>>> createPrayTasks(float speed) {
		return ImmutableList.of(
				Pair.of(1, new FindTempleTask()),
				Pair.of(2, new VillagerWalkTowardsTask(
						MOSQUE_POINT, speed, 1, 150, 1200
				)),
				Pair.of(3, new ForgetCompletedPointOfInterestTask(BELIEVER, MOSQUE_POINT)),
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
				VillagerTaskListProvider.createBusyFollowTask(),
				Pair.of(99, new ScheduleActivityTask())
		);
	}

	public static final Activity PRAY = Activity.register("pray");
	public static final MemoryModuleType<GlobalPos> MOSQUE_POINT = MemoryModuleType.register(
			"mosque_point",
			GlobalPos.CODEC
	);

	public static final PointOfInterestType BELIEVER = PointOfInterestType.register(
			"believer", PointOfInterestType.getAllStatesOf(Blocks.DIAMOND_BLOCK), 32, 100
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
							ReligiousVillagersMod.LOGGER.info("dfsdfsdfsdfsdfsdfsdfsdfsd");
							return pointOfInterestType == BELIEVER;
						})
						.build());
	}
}
