package com.skits4107.core;

import com.skits4107.network.AllSphereData;
import com.skits4107.network.SphereData;
import com.skits4107.world.SphereSavedData;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class SphereManager {


    public static void onTick(ServerLevel level){
        if (level.isClientSide()) return;

        SphereSavedData data = SphereSavedData.getSavedSphereData(level);

        List<MedusaSphere> toRemove = new ArrayList<>();
        for (MedusaSphere sphere : data.getSpheres()){
            if (sphere.isDone()){
                toRemove.add(sphere);
                continue;
            }

            sphere.onTick(level);
        }
        data.setDirty();

        for (MedusaSphere sphere : toRemove){
            data.removeSphere(sphere);
        }


        //A potential future optimization:
        // only send sphere data of spheres in range of a given players render distance.
        AllSphereData payload = buildPacket(level, data.getSpheres());
        for (ServerPlayer player : PlayerLookup.level(level)) {
            ServerPlayNetworking.send(player, payload);
        }
    }

   private static AllSphereData buildPacket(ServerLevel level, List<MedusaSphere> spheres){
        List<SphereData> data = new ArrayList<>();

        for (MedusaSphere sphere: spheres){
            data.add(new SphereData(sphere.getPos(), sphere.getRadius()));
        }
        return new AllSphereData(data);
   }


    public static void createSphere(Vec3 pos, float size, @Nullable Entity medusa, ServerLevel level){
        MedusaSphere sphere = new MedusaSphere(pos, size, medusa);
        SphereSavedData data = SphereSavedData.getSavedSphereData(level);
        data.addSphere(sphere);
    }
}
