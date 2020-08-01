package com.emmanuelmess.religiousvillagers

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

val FABRIC_ITEM = Item(Item.Settings().group(ItemGroup.MISC))

fun init() {
    Registry.register(
        Registry.ITEM,
        Identifier("tutorial", "fabric_item"),
        FABRIC_ITEM
    )
}