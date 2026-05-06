package com.skits4107.sphere;

import net.minecraft.server.level.ServerLevel;
import org.jspecify.annotations.NonNull;

import java.util.*;

public class MedusaSpheres {

    private static List<MedusaSphere> spheres = new ArrayList<>();



    public static void onTick(ServerLevel level){
        if (level.isClientSide()) return;

        List<MedusaSphere> toRemove = new ArrayList<>();
        for (MedusaSphere sphere : spheres){
            if (sphere.isDone()){
                toRemove.add(sphere);
                continue;
            }

            sphere.onTick(level);
        }

        for (MedusaSphere sphere : toRemove){
            spheres.remove(sphere);
        }
    }

    public static List<MedusaSphere> getSpheres(){
        return spheres;
    }

    public static void addSphere(MedusaSphere sphere){
        spheres.add(sphere);
    }
}
