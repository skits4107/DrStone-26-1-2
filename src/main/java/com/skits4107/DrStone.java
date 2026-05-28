package com.skits4107;

import com.skits4107.commands.ModCommands;
import com.skits4107.components.ModComponents;
import com.skits4107.core.SphereManager;
import com.skits4107.items.ModItems;
import com.skits4107.network.AllSphereData;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrStone implements ModInitializer {
	public static final String MOD_ID = "drstone";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		PayloadTypeRegistry.clientboundPlay().register(AllSphereData.TYPE, AllSphereData.ALL_SPHERE_CODEC);

		ServerTickEvents.END_LEVEL_TICK.register(SphereManager::onTick);

		ModCommands.register();
		ModItems.init();
		ModComponents.init();
	}
}