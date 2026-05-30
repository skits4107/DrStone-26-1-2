package com.skits4107.entities.custom;

import com.skits4107.core.IMedusa;
import com.skits4107.entities.ModEntities;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;



public class ThrownMedusa extends ThrowableProjectile implements IMedusa {



    public ThrownMedusa(EntityType<? extends ThrownMedusa> type, Level level) {
        super(type, level);
    }

    public ThrownMedusa(Level level, Vec3 pos, boolean activated){
        super(ModEntities.THROWN_MEDUSA_ENTITY_TYPE, level);
        setPos(pos);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {

    }

    //TODO: handle medusa data

    //stack is likely completely null always for all these methods
    @Override
    public void MedusaTick(@Nullable ItemStack stack, @Nullable Entity owner) {

    }


    @Override
    public void activate(@Nullable ItemStack stack, float t, float d, @Nullable Entity owner) {

    }

    @Override
    protected void onHit(HitResult hitResult) {
        if (level().isClientSide()){
            return;
        }
        PlacedMedusa medusa = new PlacedMedusa(level(), position(), false);

        level().addFreshEntity(medusa);

        this.remove(RemovalReason.DISCARDED);
    }
}
