package net.fabricmc.example;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.*;
import net.fabricmc.example.mixin.*;
import net.minecraft.block.*;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.*;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.*;
import net.minecraft.util.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.world.poi.*;
import org.apache.logging.log4j.*;

import java.util.function.*;
import java.util.stream.*;

public class ReligiousVillagersMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "modid";

	@Override
	public void onInitialize() {
	}

	public static ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>> createPrayTasks(float speed) {
		return ImmutableList.of(
				Pair.of(1, new FindTempleTask()),
				Pair.of(2, new VillagerWalkTowardsTask(
						MOSQUE_POINT, speed, 1, 150, 1200
				)),
				Pair.of(3, new ForgetCompletedPointOfInterestTask(BELIEVER, MOSQUE_POINT)),
				Pair.of(3, new PrayVillagerTask()),
				Pair.of(99, new ScheduleActivityTask())
		);
	}

	public static final Activity PRAY = Activity.register("pray");
	public static final MemoryModuleType<GlobalPos> MOSQUE_POINT = MemoryModuleType.register(
			"mosque_point",
			GlobalPos.CODEC
	);

	public static final PointOfInterestType BELIEVER = PointOfInterestType.register(
			"believer", PointOfInterestType.getAllStatesOf(Blocks.EMERALD_BLOCK), 32, 100
	);

	static {
		addScheduled();
		addMemoryModules();
		addPointsOfInterest();
		VillageGenerator.init();
		addTemple(new Identifier("village/plains/houses"));
		addTemple(new Identifier("village/snowy/houses"));
		addTemple(new Identifier("village/savanna/houses"));
		addTemple(new Identifier("village/desert/houses"));
		addTemple(new Identifier("village/taiga/houses"));
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
						.put(MOSQUE_POINT, (villagerEntity, pointOfInterestType) -> pointOfInterestType == BELIEVER)
						.build());
	}

	private static void addTemple(Identifier id) {
		StructurePool pool = StructurePoolBasedGenerator.REGISTRY.get(id);
		LOGGER.info("Pool " + pool.getId().toString());
		((StructurePoolRegistryAccessor) StructurePoolBasedGenerator.REGISTRY).getPools().remove(id);
		LOGGER.info("List " + ((StructurePoolRegistryAccessor) StructurePoolBasedGenerator.REGISTRY)
				.getPools().keySet().stream().map(Identifier::toString)
				.collect(Collectors.joining(", ")));


		ImmutableList<Pair<StructurePoolElement, Integer>> newElements =
				new ImmutableList.Builder<Pair<StructurePoolElement, Integer>>()
						.addAll(pool.elementCounts)
						.add(new Pair(new LegacySinglePoolElement(MODID + ":village/temple_1", ImmutableList.of()), 30))
						.build();

		StructurePoolBasedGenerator.REGISTRY.add(new StructurePool(
				id,
				pool.getTerminatorsId(),
				newElements,
				pool.projection
		));
	}
}
