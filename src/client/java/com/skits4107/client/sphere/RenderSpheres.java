package com.skits4107.client.sphere;

import com.skits4107.network.AllSphereData;
import com.skits4107.network.SphereData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientLevel;

import java.util.ArrayList;
import java.util.List;

public class RenderSpheres {

    private static List<SphereData> sphereData = new ArrayList<SphereData>();


    public static void initialize(){
        //For hooking into render events later.
    }


    public static void allSphereDataHandler(AllSphereData payload, ClientPlayNetworking.Context context){
        ClientLevel level = context.client().level;

        if (level == null) {
            return;
        }

        sphereData = payload.spheres();
    }


}
