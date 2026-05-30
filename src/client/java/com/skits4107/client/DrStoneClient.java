package com.skits4107.client;

import com.skits4107.client.entities.renderers.PetrifiedEntityRenderer;
import com.skits4107.client.entities.renderers.PlacedMedusaRenderer;
import com.skits4107.client.entities.renderers.ThrownMedusaRenderer;
import com.skits4107.client.sphere.SphereRenderPipeline;
import com.skits4107.entities.ModEntities;
import com.skits4107.network.AllSphereData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.renderer.entity.EntityRenderers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrStoneClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("drstone-client");
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		ClientPlayNetworking.registerGlobalReceiver(AllSphereData.TYPE, SphereRenderPipeline::allSphereDataHandler);
		SphereRenderPipeline.initialize();

		EntityRenderers.register(ModEntities.PETRIFIED_ENTITY_TYPE, PetrifiedEntityRenderer::new);
		EntityRenderers.register(ModEntities.PLACED_MEDUSA_ENTITY_TYPE, PlacedMedusaRenderer::new);
		EntityRenderers.register(ModEntities.THROWN_MEDUSA_ENTITY_TYPE, ThrownMedusaRenderer::new);
	}
}