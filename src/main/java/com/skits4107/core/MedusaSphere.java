package com.skits4107.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class MedusaSphere {

    public static final Codec<MedusaSphere> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codecs.VEC3_CODEC.fieldOf("position").forGetter(MedusaSphere::getPos),
            Codec.FLOAT.fieldOf("radius").forGetter(MedusaSphere::getRadius),
            Codec.FLOAT.fieldOf("max_radius").forGetter(MedusaSphere::getMaxRadius),
            UUIDUtil.CODEC.optionalFieldOf("medusa_uuid")
                    .forGetter(sphere -> Optional.ofNullable(sphere.getMedusaUUID()))
    ).apply(instance, MedusaSphere::new));

    private float radius = 0f;

    private float maxRadius = 0f;

    private float expansionRate = 2f;

    private Vec3 position;

    private Entity medusa;

    private UUID medusaUUID;

    // constructor should only be used in SphereManager to ensure all spheres are tracked.

    MedusaSphere(Vec3 pos, float size) {
        position = pos;
        maxRadius = size;
        medusaUUID = null;
    }

    MedusaSphere (Vec3 pos, float size, @Nullable Entity medusa){
        position = pos;
        maxRadius = size;
        this.medusa = medusa;
        if (medusa != null) {
            medusaUUID = medusa.getUUID();
        }
    }

    //for saving and loading spheres.
    MedusaSphere (Vec3 pos, float radius, float maxRadius, Optional<UUID> entityID){
        position = pos;
        this.radius = radius;
        this.maxRadius = maxRadius;
        entityID.ifPresent(uuid -> medusaUUID = uuid);
    }

    public void onTick(ServerLevel level){
         if (medusaUUID != null && medusa == null){
             medusa = level.getEntity(medusaUUID);
         }

        float tickRate = level.tickRateManager().tickrate();
        radius += expansionRate / tickRate;

        if (medusa != null) {
            position = medusa.position();
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

    public UUID getMedusaUUID(){
         return medusaUUID;
    }
}
