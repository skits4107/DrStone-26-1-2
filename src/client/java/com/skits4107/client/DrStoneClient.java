package com.skits4107.client;

import com.skits4107.client.sphere.SphereRenderPipeline;
import com.skits4107.network.AllSphereData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrStoneClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("drstone-client");
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		ClientPlayNetworking.registerGlobalReceiver(AllSphereData.TYPE, SphereRenderPipeline::allSphereDataHandler);
		SphereRenderPipeline.initialize();
	}
}