package com.skits4107.core;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class MedusaSphere {

    private boolean done = false;

    private float radius = 0f;

    private float maxRadius = 0f;

    private final float expansionRate = 1f;

    private Vec3 position;

    private IMedusa medusa = null;

    private ItemStack medusaItem = null;

    // constructor should only be used in SphereManager to ensure all spheres are tracked.
    MedusaSphere (Vec3 pos, float size){
        position = pos;
        maxRadius = size;
    }
    MedusaSphere (Vec3 pos, float size, IMedusa medusa){
        position = pos;
        maxRadius = size;
        this.medusa = medusa;
    }
    MedusaSphere (Vec3 pos, float size, IMedusa medusa, ItemStack medusaItem){
        position = pos;
        maxRadius = size;
        this.medusa = medusa;
        this.medusaItem = medusaItem;
    }

    public void onTick(ServerLevel level){
        float tickRate = level.tickRateManager().tickrate();
        radius += expansionRate / tickRate;

        if (medusa != null) {
            position = medusa.getPosition(medusaItem);
        }
    }

    public boolean isDone(){
        return radius >= maxRadius;
    }

    public float getRadius(){
        return radius;
    }

    public float getMaxRadius(){
        return maxRadius;
    }

    public Vec3 getPos(){
        return position;
    }
}
