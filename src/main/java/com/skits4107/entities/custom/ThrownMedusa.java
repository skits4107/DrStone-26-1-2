package com.skits4107.entities.custom;

import com.skits4107.core.IMedusa;
import com.skits4107.core.SphereManager;
import com.skits4107.entities.ModEntities;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;



public class ThrownMedusa extends ThrowableProjectile implements IMedusa {

    private static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(ThrownMedusa.class, EntityDataSerializers.BOOLEAN);
    private float time = 0.0f;
    private float distance = 0f;

    public ThrownMedusa(EntityType<? extends ThrownMedusa> type, Level level) {
        super(type, level);
    }

    public ThrownMedusa(Level level, Vec3 pos){
        super(ModEntities.THROWN_MEDUSA_ENTITY_TYPE, level);
        setPos(pos);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(ACTIVE, false);
    }

    public boolean isActive(){
        return entityData.get(ACTIVE);
    }

    public void setActive(boolean active){
        entityData.set(ACTIVE, active);
    }

    @Override
    public void tick() {
        super.tick();
        MedusaTick(null, this);
    }

    //TODO: handle medusa data

    //stack is likely completely null always for all these methods
    @Override
    public void MedusaTick(@Nullable ItemStack stack, @Nullable Entity owner) {
        if (level().isClientSide()){
            return;
        }
        if (!isActive()){
            return;
        }
        float delta = 1.0f / level().tickRateManager().tickrate();
        time -= delta;

        if (time <= 0){
            SphereManager.createSphere(position(), distance, this, (ServerLevel) level());
            setActive(false);
        }
    }


    @Override
    public void activate(@Nullable ItemStack stack, float t, float d, @Nullable Entity owner) {
        time = t;
        distance = d;
        setActive(true);
    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (level().isClientSide()){
            return;
        }
        PlacedMedusa medusa = new PlacedMedusa(level(), position());
        if (isActive()){
            medusa.activate(null, time, distance, medusa);
        }
        level().addFreshEntity(medusa);

        this.remove(RemovalReason.DISCARDED);
    }
}
