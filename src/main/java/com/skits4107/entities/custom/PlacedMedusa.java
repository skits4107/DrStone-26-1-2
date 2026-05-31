package com.skits4107.entities.custom;

import com.skits4107.core.IMedusa;
import com.skits4107.core.SphereManager;
import com.skits4107.entities.ModEntities;
import com.skits4107.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class PlacedMedusa extends Entity implements IMedusa {

    private static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(PlacedMedusa.class, EntityDataSerializers.BOOLEAN);

    //these are not needed by client so they don't need be synched data.
    private float time = 0.0f;
    private float distance = 0f;


    public PlacedMedusa(EntityType<?> type, Level level) {
        super(type, level);
    }

    public PlacedMedusa(Level level, Vec3 pos){
        super(ModEntities.PLACED_MEDUSA_ENTITY_TYPE, level);
        setPos(pos);
    }

    @Override
    public @NonNull InteractionResult interact(Player player, @NonNull InteractionHand hand, @NonNull Vec3 location) {
        if(level().isClientSide()){
            return super.interact(player, hand, location);
        }
        player.addItem(new ItemStack(ModItems.MEDUSA, 1));
        remove(RemovalReason.DISCARDED);
        return InteractionResult.SUCCESS;
    }


    @Override
    protected AABB makeBoundingBox(Vec3 position) {
        return new AABB(position.x-0.5, position.y-0.5, position.z-0.5, position.x+0.5, position.y+0.5, position.z+0.5);
    }

    @Override
    public boolean mayInteract(ServerLevel level, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public boolean isActive(){
        return entityData.get(ACTIVE);
    }

    public void setActive(boolean active){
        entityData.set(ACTIVE, active);
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
    public void tick() {
        super.tick();
        MedusaTick(null, this);
    }

    @Override
    public void activate(@Nullable ItemStack stack, float t, float d, @Nullable Entity owner) {
        time = t;
        distance = d;
        setActive(true);
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(ACTIVE, false);
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
