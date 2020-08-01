package net.fabricmc.example;

import com.google.common.collect.*;
import com.mojang.datafixers.util.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.*;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.decorator.*;
import net.minecraft.world.gen.feature.*;

public class ExampleMod implements ModInitializer {
	public static final String MODID = "modid";

	@Override
	public void onInitialize() {
		AutoConfig.register(RSAllConfig.class, Toml4jConfigSerializer::new);
		RSAllConfig = AutoConfig.getConfigHolder(RSAllConfig.class).getConfig();

		RSPlacements.registerPlacements();
		RSFeatures.registerFeatures();
		ServerStartCallback.EVENT.register(minecraftServer -> VillagerTrades.addMapTrades());

		//LoadNbtBlock.instantiateNbtBlock();

		for (Biome biome : Registry.BIOME) {
			addFeaturesAndStructuresToBiomes(biome, Registry.BIOME.getId(biome));
		}
		RegistryEntryAddedCallback.event(Registry.BIOME).register((i, identifier, biome) -> addFeaturesAndStructuresToBiomes(biome, identifier));

		Registry.BIOME.forEach(biome -> {
			biome.addStructureFeature(EXAMPLE_STRUCTURE_FEATURE.configure(new DefaultFeatureConfig()));
		});
	}
}
