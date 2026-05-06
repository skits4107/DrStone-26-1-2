package com.skits4107.sphere;

import net.minecraft.server.level.ServerLevel;

public class MedusaSphere {

    private boolean done = false;

    private float radius = 0f;

    private float maxRadius = 0f;

    private final float expansionRate = 1f;

    public MedusaSphere (float size){
        maxRadius = size;
    }

    public void onTick(ServerLevel level){
        


    }

    public boolean isDone(){
        return done;
    }
}
