package com.skits4107.sphere;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public class MedusaSphere {

    private boolean done = false;

    private float radius = 0f;

    private float maxRadius = 0f;

    private final float expansionRate = 1f;

    private Vec3 position;

    // constructor should only be used in SphereManager to ensure all spheres are tracked.
    MedusaSphere (Vec3 pos, float size){
        position = pos;
        maxRadius = size;
    }

    public void onTick(ServerLevel level){
        float tickRate = level.tickRateManager().tickrate();

        radius += expansionRate / tickRate;
    }

    public boolean isDone(){
        return done;
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
