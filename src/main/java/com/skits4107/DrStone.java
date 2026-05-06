package com.skits4107;

import com.skits4107.sphere.MedusaSpheres;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrStone implements ModInitializer {
	public static final String MOD_ID = "drstone";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		ServerTickEvents.END_LEVEL_TICK.register(MedusaSpheres::onTick);
	}
}