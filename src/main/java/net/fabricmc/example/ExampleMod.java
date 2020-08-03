package net.fabricmc.example;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.util.registry.*;
import net.minecraft.village.*;
import net.minecraft.world.poi.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

public class ExampleMod implements ModInitializer {
	@Override
	public void onInitialize() {
	}

	public static ImmutableList<com.mojang.datafixers.util.Pair<Integer, ? extends Task<? super VillagerEntity>>> createPrayTasks(float speed) {
		return ImmutableList.of(
				Pair.of(2, new VillagerWalkTowardsTask(ExampleMod.MOSQUE_POINT, speed, 1, 1500000, 1200)),
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

	public static final Activity PRAY = register("pray");
	public static final MemoryModuleType<GlobalPos> MOSQUE_POINT = register("mosque_point", GlobalPos.CODEC);

	static {
		addScheduled();
		addMemoryModules();
		addPointsOfInterest();
	}

	private static <U> MemoryModuleType<U> register(String id, Codec<GlobalPos> codec) {
		try {
			return (MemoryModuleType) Registry.register(
					Registry.MEMORY_MODULE_TYPE,
					new Identifier(id),
					createMemoryModuleType(Optional.of(codec))
			);
		} catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException
				| InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static Activity register(String id) {
		try {
			return Registry.register(Registry.ACTIVITY, id, createActivity(id));
		} catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException
				| InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static Activity createActivity(String id)
			throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Class<Activity> activityClass = (Class<Activity>) Class.forName("net.minecraft.entity.ai.brain.Activity");
		Constructor<Activity> constructor = activityClass.getDeclaredConstructor(String.class);
		constructor.setAccessible(true);
		Activity activity = constructor.newInstance(id);
		constructor.setAccessible(false);
		return activity;
	}

	private static MemoryModuleType<GlobalPos> createMemoryModuleType(Optional<Codec<GlobalPos>> factory)
			throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		Class<MemoryModuleType<GlobalPos>> memoryModuleTypeClass =
				(Class<MemoryModuleType<GlobalPos>>) Class.forName("net.minecraft.entity.ai.brain.MemoryModuleType");
		Constructor<MemoryModuleType<GlobalPos>> constructor = memoryModuleTypeClass.getDeclaredConstructor(Optional.class);
		constructor.setAccessible(true);
		MemoryModuleType<GlobalPos> memoryModuleType = constructor.newInstance(factory);
		constructor.setAccessible(false);
		return memoryModuleType;
	}

	private static void addScheduled() {
		try {
			Field field = Schedule.class.getDeclaredField("VILLAGER_DEFAULT");
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

			Schedule newSchedule = new ScheduleBuilder(Schedule.VILLAGER_DEFAULT).withActivity(10, ExampleMod.PRAY).build();

			field.set(null, newSchedule);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static void addMemoryModules() {
		try {
			Field field = VillagerEntity.class.getDeclaredField("MEMORY_MODULES");
			field.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

			ImmutableList<MemoryModuleType<?>> list = (ImmutableList<MemoryModuleType<?>>) field.get(null);
			ImmutableList<MemoryModuleType<?>> newList = new ImmutableList.Builder<MemoryModuleType<?>>()
					.addAll(list).add(MOSQUE_POINT).build();

			field.set(null, newList);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static void addPointsOfInterest() {
		try {
			Field field = VillagerEntity.class.getDeclaredField("POINTS_OF_INTEREST");
			field.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

			Map<MemoryModuleType<GlobalPos>, BiPredicate<VillagerEntity, PointOfInterestType>> map =
					(Map<MemoryModuleType<GlobalPos>, BiPredicate<VillagerEntity, PointOfInterestType>>) field.get(null);
			Map<MemoryModuleType<GlobalPos>, BiPredicate<VillagerEntity, PointOfInterestType>> newList =
					new ImmutableMap.Builder<MemoryModuleType<GlobalPos>, BiPredicate<VillagerEntity, PointOfInterestType>>()
							.putAll(map).put(MOSQUE_POINT, (villagerEntity, pointOfInterestType) -> {
						return pointOfInterestType == PointOfInterestType.MEETING;
					}).build();

			field.set(null, newList);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
