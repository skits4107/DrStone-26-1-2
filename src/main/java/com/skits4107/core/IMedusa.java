package com.skits4107.core;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public interface IMedusa {

    // item version need to reference item stack. entity medusas dont have any items to worry about.
    // item also needs owner, entity medusas are their own owners
     void MedusaTick(@Nullable ItemStack stack, @Nullable Entity owner);
     void activate(@Nullable ItemStack stack, float t, float d, @Nullable Entity owner);

}
