package com.skits4107.client.entities.states;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.Display;

public class ThrownMedusaRenderState extends EntityRenderState {

    public ItemStackRenderState item;

    public ThrownMedusaRenderState(ItemStackRenderState item){
        this.item = item;
    }
}
