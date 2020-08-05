package net.fabricmc.example.mixin;

import net.fabricmc.example.*;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.passive.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static net.fabricmc.example.ReligiousVillagersMod.*;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {
	@Inject(at = @At("HEAD"), method = "initBrain(Lnet/minecraft/entity/ai/brain/Brain;)V", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void initBrain(Brain<VillagerEntity> brain, CallbackInfo info) {
		VillagerEntity $this = (VillagerEntity) (Object) this;

		if (!$this.isBaby()) {
			brain.setTaskList(
					ReligiousVillagersMod.PRAY,
					createPrayTasks(0.75F)
			);
		}
	}
}
