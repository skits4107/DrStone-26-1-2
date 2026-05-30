package com.skits4107.client.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.skits4107.client.entities.states.PetrifiedEntityRenderState;
import com.skits4107.entities.custom.PetrifiedEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;

public class PetrifiedEntityRenderer extends EntityRenderer<PetrifiedEntity, PetrifiedEntityRenderState> {
    public PetrifiedEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public PetrifiedEntityRenderState createRenderState() {
        return null;
    }

    @Override
    public void submit(PetrifiedEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
