package com.skits4107;

import com.skits4107.commands.ModCommands;
import com.skits4107.components.ModComponents;
import com.skits4107.core.SphereManager;
import com.skits4107.entities.ModEntities;
import com.skits4107.events.ModEvents;
import com.skits4107.items.ModItems;
import com.skits4107.network.AllSphereData;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DrStone implements ModInitializer {
	public static final String MOD_ID = "drstone";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		PayloadTypeRegistry.clientboundPlay().register(AllSphereData.TYPE, AllSphereData.ALL_SPHERE_CODEC);


		ModEvents.register();
		ModCommands.register();
		ModItems.init();
		ModComponents.init();
		ModEntities.init();
	}
}