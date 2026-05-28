package com.skits4107.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skits4107.DrStone;
import com.skits4107.core.Codecs;
import com.skits4107.core.MedusaSphere;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class SphereSavedData extends SavedData {

    private List<MedusaSphere> spheres = new ArrayList<>();


    public static final Codec<SphereSavedData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    MedusaSphere.CODEC.listOf()
                            .fieldOf("spheres")
                            .forGetter(SphereSavedData::getSpheres)
            ).apply(instance, SphereSavedData::new));

    private static final SavedDataType<SphereSavedData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(DrStone.MOD_ID, "saved_sphere_data"), // The unique name for this saved data.
            SphereSavedData::new, // If there's no 'SphereSavedData', yet create one and refresh fields.
            CODEC, // The codec used for serialization/deserialization.
            null // A data fixer, which is not needed here.
    );

    public SphereSavedData(){
        this.spheres = new ArrayList<>();
    }

    public SphereSavedData(List<MedusaSphere> spheres) {
        this.spheres = new ArrayList<>(spheres);
    }

    public List<MedusaSphere> getSpheres(){
        return spheres;
    }

    public  void addSphere(MedusaSphere sphere){
        spheres.add(sphere);

        setDirty();
    }

    public  void removeSphere(MedusaSphere sphere){
        spheres.remove(sphere);

        setDirty();
    }

    public static SphereSavedData getSavedSphereData(ServerLevel level) {

        if (level == null) {
            return new SphereSavedData(); // Return a new instance if the level is null.
        }

        // The first time the following 'computeIfAbsent' function is called, it creates a new 'SavedBlockData'
        // instance and stores it inside the 'DimensionDataStorage'.
        // Subsequent calls to 'computeIfAbsent' returns the saved 'SavedBlockData' NBT on disk to the Codec in our type,
        // using the Codec to decode the NBT into our saved data.
        return level.getDataStorage().computeIfAbsent(TYPE);
    }




}
