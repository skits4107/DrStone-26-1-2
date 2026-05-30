package com.skits4107.client.entities.states;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class PlacedMedusaRenderState extends EntityRenderState {

    public ItemStackRenderState item;

    public PlacedMedusaRenderState(ItemStackRenderState item){
        this.item = item;
    }
}
