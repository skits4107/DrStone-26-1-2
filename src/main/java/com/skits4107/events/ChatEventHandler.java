package com.skits4107.events;

import com.skits4107.DrStone;
import com.skits4107.core.IMedusa;
import com.skits4107.entities.custom.PlacedMedusa;
import com.skits4107.entities.custom.ThrownMedusa;
import com.skits4107.items.ModItems;
import com.skits4107.items.custom.Medusa;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatEventHandler {


    public static void serverChatMessage(PlayerChatMessage message, ServerPlayer player, ChatType.Bound bound){
        String raw_text = message.decoratedContent().getString();
        String[] params = raw_text.strip().split(" ");

        if (params.length != 4){
            return;
        }

        if (!params[1].equals("meters") || !params[3].equals("seconds")){
            return;
        }

        try{
            float distance = Float.parseFloat(params[0]);
            float seconds = Float.parseFloat(params[2]);

            Vec3 pos = player.position();
            Vec3 begin = new Vec3(pos.x - 3, pos.y - 3, pos.z - 3);
            Vec3 end = new Vec3(pos.x + 3, pos.y + 3, pos.z + 3);
            AABB box = new AABB(begin, end);

            //Activate all the medusas of every player within a 3 block radius of the offending player.
            List<ServerPlayer> players = player.level().getEntitiesOfClass(ServerPlayer.class, box);

            //should include the player that sent the command
            for (ServerPlayer plyr : players){
                setPlayerMedusa(plyr, seconds, distance);
            }

            //Activate the other surounding medusas

            List<PlacedMedusa> placedMedusas = player.level().getEntitiesOfClass(PlacedMedusa.class, box);
            for (PlacedMedusa m : placedMedusas){
                m.activate(null, seconds, distance, m);
            }

            List<ThrownMedusa> thrownMedusas = player.level().getEntitiesOfClass(ThrownMedusa.class, box);
            for (ThrownMedusa m : thrownMedusas){
                m.activate(null, seconds, distance, m);
            }
        }
        catch (Exception e){
            DrStone.LOGGER.info("invalid medusa paramters");
            DrStone.LOGGER.error(e.getMessage());
        }

    }

    private static void setPlayerMedusa(ServerPlayer player, float seconds, float distance){
        Inventory inv = player.getInventory();


        Iterator<ItemStack> items = inv.iterator();
        while(items.hasNext()){
            ItemStack stack = items.next();
            if (!stack.isEmpty() && stack.getItem() == ModItems.MEDUSA){
                ModItems.MEDUSA.activate(stack, seconds, distance, player);
            }
        }


    }
}
