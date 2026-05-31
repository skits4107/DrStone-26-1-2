package com.skits4107.client.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.skits4107.client.entities.states.ThrownMedusaRenderState;
import com.skits4107.components.ModComponents;
import com.skits4107.core.MedusaData;
import com.skits4107.entities.custom.ThrownMedusa;
import com.skits4107.items.ModItems;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class ThrownMedusaRenderer extends EntityRenderer<ThrownMedusa, ThrownMedusaRenderState> {


    //TODO: add proper medusa model so item model doesnt have to be used.


    private final ItemModelResolver resolver;

    private ItemStack medusa;

    public ThrownMedusaRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.resolver = context.getItemModelResolver();
    }

    private ItemStack getMedusaStack(boolean active) {
        //update the item stack reference used if it doesnt exist or has changed state
        if (medusa == null || medusa.get(ModComponents.MEDUSA_DATA_COMPONENT_TYPE).active() != active) {
            medusa = new ItemStack(ModItems.MEDUSA, 1);

            // By default it is the inactive model. If the medusa is active we want the active model.
            if (active){
                medusa.set(ModComponents.MEDUSA_DATA_COMPONENT_TYPE, new MedusaData(1, 1, true, Vec3.ZERO));
            }
        }
        return medusa;
    }

    @Override
    public @NonNull ThrownMedusaRenderState createRenderState() {
        return new ThrownMedusaRenderState(new ItemStackRenderState());
    }

    @Override
    public void extractRenderState(ThrownMedusa entity, ThrownMedusaRenderState state, float partialTicks) {
        resolver.updateForNonLiving(state.item, getMedusaStack(entity.isActive()), ItemDisplayContext.GROUND, entity);
        super.extractRenderState(entity, state, partialTicks);
    }

    @Override
    public void submit(ThrownMedusaRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();

        if (state.item != null) {
            state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        }

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
