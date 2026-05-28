package com.skits4107.entities.custom;

import com.skits4107.core.IMedusa;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class PlacedMedusa extends Entity implements IMedusa {



    public PlacedMedusa(EntityType<?> type, Level level) {
        super(type, level);
    }
    //TODO: handle medusa data

    //stack is likely completely null always for all these methods

    @Override
    public void MedusaTick(@Nullable ItemStack stack, Entity owner) {

    }

    @Override
    public float getTime(@Nullable ItemStack stack) {
        return 0;
    }

    @Override
    public float getDistance(@Nullable ItemStack stack) {
        return 0;
    }

    @Override
    public Vec3 getPosition(@Nullable ItemStack stack) {
        return null;
    }

    @Override
    public void activate(@Nullable ItemStack stack, float t, float d) {

    }

    @Override
    public boolean isActive(@Nullable ItemStack stack) {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {

    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {

    }
}
