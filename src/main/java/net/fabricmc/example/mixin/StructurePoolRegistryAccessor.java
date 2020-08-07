package net.fabricmc.example.mixin;

import net.minecraft.structure.pool.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

import java.util.*;

@Mixin(StructurePoolRegistry.class)
public interface StructurePoolRegistryAccessor {
    @Accessor("pools")
    public Map<Identifier, StructurePool> getPools();
}
