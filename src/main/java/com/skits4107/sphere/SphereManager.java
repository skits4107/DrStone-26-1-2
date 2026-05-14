package com.skits4107.sphere;

import com.skits4107.network.AllSphereData;
import com.skits4107.network.SphereData;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class SphereManager {

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


        //A potential future optimization:
        // only send sphere data of spheres in range of a given players render distance.
        AllSphereData payload = buildPacket();
        for (ServerPlayer player : PlayerLookup.level(level)) {
            ServerPlayNetworking.send(player, payload);
        }
    }

   private static AllSphereData buildPacket(){
        List<SphereData> data = new ArrayList<>();
        for (MedusaSphere sphere: spheres){
            data.add(new SphereData(sphere.getPos(), sphere.getRadius()));
        }
        return new AllSphereData(data);
   }

   public static void createSphere(Vec3 pos, float size){
        MedusaSphere sphere = new MedusaSphere(pos, size);
        spheres.add(sphere);
   }
}
