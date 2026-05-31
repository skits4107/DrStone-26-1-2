package com.skits4107.events;

import com.skits4107.core.SphereManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;

public class ModEvents {

    public static void register(){
        ServerTickEvents.END_LEVEL_TICK.register(SphereManager::onTick);
        ServerMessageEvents.CHAT_MESSAGE.register(ChatEventHandler::serverChatMessage);
    }
}
