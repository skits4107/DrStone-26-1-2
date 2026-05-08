package com.skits4107.client.sphere;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.skits4107.DrStone;
import com.skits4107.network.AllSphereData;
import com.skits4107.network.SphereData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.minecraft.client.renderer.RenderPipelines.MATRICES_PROJECTION_SNIPPET;

public class SphereRenderPipeline {

    private static List<SphereData> sphereData = new ArrayList<SphereData>();

    private static final RenderPipeline SPHERE_RENDER_PIPELINE = RenderPipelines.register(RenderPipeline.builder(MATRICES_PROJECTION_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(DrStone.MOD_ID, "pipeline/medusa_sphere"))
            .withVertexShader(Identifier.fromNamespaceAndPath(DrStone.MOD_ID, "shaders/core/medusa_sphere"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(DrStone.MOD_ID, "shaders/core/medusa_sphere"))
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
            .withDepthStencilState(Optional.empty())
            .withUniform("SphereCenter", UniformType.UNIFORM_BUFFER)
            .withUniform("SphereRadius", UniformType.UNIFORM_BUFFER)
            .build()
    );

    private static final ByteBufferBuilder allocator = new ByteBufferBuilder(RenderType.SMALL_BUFFER_SIZE);
    private static BufferBuilder buffer;

    private static final Vector4f COLOR_MODULATOR = new Vector4f(1f, 1f, 1f, 1f);
    private static final Vector3f MODEL_OFFSET = new Vector3f();
    private static final Matrix4f TEXTURE_MATRIX = new Matrix4f();
    private static MappableRingBuffer vertexBuffer;




    public static void initialize(){
        //For hooking into render events later.

        LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register(SphereRenderPipeline::extractAndDrawSpheres);
    }

    private static void extractAndDrawSpheres(LevelRenderContext context) {
        //renderWaypoint(context);
        //drawFilledThroughWalls(Minecraft.getInstance(), FILLED_THROUGH_WALLS);
    }

    public static void allSphereDataHandler(AllSphereData payload, ClientPlayNetworking.Context context){
        ClientLevel level = context.client().level;

        if (level == null) {
            return;
        }

        sphereData = payload.spheres();
    }


}
