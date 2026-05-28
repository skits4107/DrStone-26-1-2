package com.skits4107.mixin;

import com.skits4107.items.custom.Medusa;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickItem(CallbackInfo ci){
        ItemEntity entity = (ItemEntity)(Object)this;
        ItemStack stack = entity.getItem();

        if (stack.getItem() instanceof Medusa item) {
            item.MedusaTick(stack, entity);
        }
    }
}
