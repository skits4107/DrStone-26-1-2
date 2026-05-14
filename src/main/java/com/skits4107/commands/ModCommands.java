package com.skits4107.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.skits4107.sphere.SphereManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ModCommands {

    private static int spawnSphereCommand(CommandContext<CommandSourceStack> context){
        float meters = FloatArgumentType.getFloat(context, "meters");
        Vec3 pos = Vec3Argument.getVec3(context, "pos");

        SphereManager.createSphere(pos, meters);

        context.getSource().sendSuccess(() -> Component.literal("summoned medusa sphere of %f meters".formatted(meters)), false);
        return 1;
    }


    public static void register(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            dispatcher.register(Commands.literal("summon_sphere")
                    .then(Commands.argument("meters", FloatArgumentType.floatArg(0))
                            .then(
                                Commands.argument("pos", Vec3Argument.vec3(false))
                                    .executes(ModCommands::spawnSphereCommand)
                            )
                    ));

        });
    }
}
