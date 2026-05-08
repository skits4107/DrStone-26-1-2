package com.skits4107.client.sphere;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.skits4107.DrStone;
import com.skits4107.network.AllSphereData;
import com.skits4107.network.SphereData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.minecraft.client.renderer.RenderPipelines.MATRICES_PROJECTION_SNIPPET;

public class RenderSpheres {

    private static List<SphereData> sphereData = new ArrayList<SphereData>();

    private static RenderSpheres instance;

    private static final RenderPipeline SPHERE_RENDER_PIPELINE = RenderPipeline.builder(new RenderPipeline.Snippet[]{MATRICES_PROJECTION_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath(DrStone.MOD_ID, "pipeline/medusa_sphere"))
            .withVertexShader(Identifier.fromNamespaceAndPath(DrStone.MOD_ID, "shaders/core/medusa_sphere"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(DrStone.MOD_ID, "shaders/core/medusa_sphere"))
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
            .withDepthStencilState(Optional.empty())
            .withUniform("SphereCenter", UniformType.UNIFORM_BUFFER)
            .withUniform("SphereRadius", UniformType.UNIFORM_BUFFER)
            .build();

    public static RenderSpheres getInstance(){
        return instance;
    }

    public void initialize(){
        //For hooking into render events later.
        instance = this;

    }

    public static void allSphereDataHandler(AllSphereData payload, ClientPlayNetworking.Context context){
        ClientLevel level = context.client().level;

        if (level == null) {
            return;
        }

        sphereData = payload.spheres();
    }


}
