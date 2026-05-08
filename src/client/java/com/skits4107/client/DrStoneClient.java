package com.skits4107.client;

import com.skits4107.client.sphere.RenderSpheres;
import com.skits4107.network.AllSphereData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class DrStoneClient implements ClientModInitializer {

	public static RenderSpheres renderSpheres;
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		ClientPlayNetworking.registerGlobalReceiver(AllSphereData.TYPE, RenderSpheres::allSphereDataHandler);

		renderSpheres = new RenderSpheres();
		renderSpheres.initialize();
	}
}