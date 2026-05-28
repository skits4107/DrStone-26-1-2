package com.skits4107.core;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public interface IMedusa {

    // item version need to reference item stack. entities not as much.
     void MedusaTick(@Nullable ItemStack stack, Entity owner);
     float getTime(@Nullable ItemStack stack);
     float getDistance(@Nullable ItemStack stack);
     Vec3 getPosition(@Nullable ItemStack stack);
     void activate(@Nullable ItemStack stack, float t, float d);
    boolean isActive(@Nullable ItemStack stack);
}
