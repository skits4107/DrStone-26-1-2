package com.skits4107.entities.custom;

import com.skits4107.core.IMedusa;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ThrownMedusa extends ThrowableProjectile implements IMedusa {
    protected ThrownMedusa(EntityType<? extends ThrowableProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {

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
        return position();
    }

    @Override
    public void activate(@Nullable ItemStack stack, float t, float d) {

    }

    @Override
    public boolean isActive(@Nullable ItemStack stack) {
        return false;
    }
}
