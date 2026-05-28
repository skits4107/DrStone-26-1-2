package com.skits4107.items.custom;

import com.skits4107.components.ModComponents;
import com.skits4107.core.IMedusa;
import com.skits4107.core.MedusaData;
import com.skits4107.core.SphereManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class Medusa extends Item implements IMedusa {
    public Medusa(Properties properties) {
        super(properties.component(ModComponents.MEDUSA_DATA_COMPONENT_TYPE, new MedusaData(0, 0, false, Vec3.ZERO)));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        activate(player.getItemInHand(hand), 5, 20);
        return super.use(level, player, hand);
    }

    //stack shouldn't be null in reality, its only nullable as entities that are medusa won't have item stacks.

    @Override
    public void MedusaTick(@Nullable ItemStack stack, Entity owner) {
        if (owner.level().isClientSide()){
            return;
        }
        float delta = 1.0f / owner.level().tickRateManager().tickrate();

        if (stack == null){
            return;
        }

        MedusaData oldData = getData(stack);

        if (oldData == null){
            oldData = new MedusaData(0, 0, false, Vec3.ZERO);
        }

        if (!oldData.active()){
            // keep position updated even if it is not active.
            MedusaData newData = new MedusaData(0, 0, false, owner.position());
            stack.set(ModComponents.MEDUSA_DATA_COMPONENT_TYPE, newData);
            return;
        }

        float newTime = oldData.t() - delta;
        boolean active = true;

        if (newTime <= 0){
            SphereManager.createSphere(owner.position(), oldData.d(), this, stack);
            active = false;
        }

        MedusaData newData = new MedusaData(newTime, oldData.d(), active, owner.position());
        stack.set(ModComponents.MEDUSA_DATA_COMPONENT_TYPE, newData);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(itemStack, level, owner, slot);

        MedusaTick(itemStack, owner);
    }

    @Override
    public float getTime(@Nullable ItemStack stack) {
        if (stack != null) {
            MedusaData data = getData(stack);
            return data.t();
        }
        return 0;
    }

    @Override
    public float getDistance(@Nullable ItemStack stack) {
        if (stack != null) {
            MedusaData data = getData(stack);
            return data.d();
        }
        return 0;
    }

    @Override
    public Vec3 getPosition(@Nullable ItemStack stack) {
        if (stack != null) {
            MedusaData data = getData(stack);
            return data.pos();
        }
        return Vec3.ZERO;
    }

    @Override
    public void activate(@Nullable ItemStack stack, float t, float d) {
        if (stack != null) {
            stack.set(ModComponents.MEDUSA_DATA_COMPONENT_TYPE, new MedusaData(t, d, true, getPosition(stack)));
        }
    }

    @Override
    public boolean isActive(@Nullable ItemStack stack) {
        if (stack != null) {
            MedusaData data = getData(stack);
            return data.active();
        }
        return false;
    }


    public MedusaData getData(ItemStack stack){
        MedusaData data = stack.get(ModComponents.MEDUSA_DATA_COMPONENT_TYPE);
        if (data == null){
            data = new MedusaData(0, 0, false, Vec3.ZERO);
        }
        return data;
    }
}
