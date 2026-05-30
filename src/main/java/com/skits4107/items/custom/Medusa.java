package com.skits4107.items.custom;

import com.skits4107.DrStone;
import com.skits4107.components.ModComponents;
import com.skits4107.core.IMedusa;
import com.skits4107.core.MedusaData;
import com.skits4107.core.SphereManager;
import com.skits4107.entities.custom.ThrownMedusa;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.logging.Logger;

public class Medusa extends Item implements IMedusa {
    public Medusa(Properties properties) {
        super(properties.component(ModComponents.MEDUSA_DATA_COMPONENT_TYPE, new MedusaData(0, 0, false, Vec3.ZERO)));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()){
            return super.use(level, player, hand);
        }


        ItemStack item = player.getItemInHand(hand);

        ThrownMedusa medusa = new ThrownMedusa(level, player.position(), getData(item).active());
        medusa.shootFromRotation(player, player.getXRot(), player.getYRot(), 0f, 2.0f, 0.5f);

        if(player.gameMode() != GameType.CREATIVE) {
            player.getItemInHand(hand).shrink(1);
        }
        level.addFreshEntity(medusa);

      //  activate(player.getItemInHand(hand), 5, 20, player);
        return super.use(level, player, hand);
    }

    //stack shouldn't be null in reality, its only nullable as entities that are medusa won't have item stacks.

    @Override
    public void MedusaTick(@Nullable ItemStack stack, @Nullable Entity owner) {
        if (owner.level().isClientSide()){
            return;
        }
        float delta = 1.0f / owner.level().tickRateManager().tickrate();

        if (stack == null || owner == null){
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
            SphereManager.createSphere(owner.position(), oldData.d(), owner, (ServerLevel) owner.level());
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
    public void activate(@Nullable ItemStack stack, float t, float d, @Nullable Entity owner) {
        if (stack != null && owner != null) {
            stack.set(ModComponents.MEDUSA_DATA_COMPONENT_TYPE, new MedusaData(t, d, true, owner.position()));
        }
    }


    public MedusaData getData(ItemStack stack){
        MedusaData data = stack.get(ModComponents.MEDUSA_DATA_COMPONENT_TYPE);
        if (data == null){
            data = new MedusaData(0, 0, false, Vec3.ZERO);
        }
        return data;
    }
}
