package net.fabricmc.example.mixin;

import com.google.common.collect.*;
import com.mojang.datafixers.util.*;
import net.fabricmc.example.*;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.*;
import net.minecraft.server.world.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static net.fabricmc.example.ExampleMod.*;

@Mixin(VillagerBreedTask.class)
public class VillagerBreedTaskMixin {
	@Inject(at = @At("RETURN"), method = "setChildHome(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/VillagerEntity;Lnet/minecraft/util/math/BlockPos;)V", locals = LocalCapture.NO_CAPTURE)
	private void setChildHome(ServerWorld world, VillagerEntity child, BlockPos pos, CallbackInfo info) {
		VillagerBreedTask $this = (VillagerBreedTask) (Object) this;

		System.out.println("Remembered mosque");
		child.getBrain().remember(ExampleMod.MOSQUE_POINT, GlobalPos.create(world.getRegistryKey(), BlockPos.ORIGIN));
	}
}
