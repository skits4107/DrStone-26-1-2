package com.skits4107.client.sphere;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.skits4107.DrStone;
import com.skits4107.network.AllSphereData;
import com.skits4107.network.SphereData;
import com.skits4107.network.SphereData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.util.*;

import static net.minecraft.client.renderer.RenderPipelines.MATRICES_PROJECTION_SNIPPET;

public class SphereRenderPipeline {



    private static List<SphereData> SphereData = new ArrayList<SphereData>();

    private static final RenderPipeline SPHERE_RENDER_PIPELINE = RenderPipelines.register(RenderPipeline.builder(MATRICES_PROJECTION_SNIPPET)
            .withLocation(Identifier.fromNamespaceAndPath(DrStone.MOD_ID, "pipeline/medusa_sphere"))
            .withVertexShader(Identifier.fromNamespaceAndPath(DrStone.MOD_ID, "core/medusa_sphere"))
            .withFragmentShader(Identifier.fromNamespaceAndPath(DrStone.MOD_ID, "core/medusa_sphere"))
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS)
            .withDepthStencilState(Optional.empty())
            .withUniform("Sphere", UniformType.UNIFORM_BUFFER)
            .build()
    );

    private static final ByteBufferBuilder allocator = new ByteBufferBuilder(RenderType.SMALL_BUFFER_SIZE);
    private static BufferBuilder buffer;

    private static final Vector4f COLOR_MODULATOR = new Vector4f(1f, 1f, 1f, 1f);
    private static final Vector3f MODEL_OFFSET = new Vector3f();
    private static final Matrix4f TEXTURE_MATRIX = new Matrix4f();


    private static MappableRingBuffer vertexBuffer;
    private static MappableRingBuffer sphereUniformBuffer;




    public static void initialize(){
        //For hooking into render events later.

        LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN.register(SphereRenderPipeline::extractAndDrawSpheres);
    }

    private static void extractAndDrawSpheres(LevelRenderContext context) {
        for (SphereData sphere : SphereData){
            renderSphere(context, sphere);
            drawSphere(Minecraft.getInstance(), SPHERE_RENDER_PIPELINE, sphere);
        }
        //renderWaypoint(context);
        //drawFilledThroughWalls(Minecraft.getInstance(), FILLED_THROUGH_WALLS);
    }

    private static void renderSphere(LevelRenderContext context, SphereData sphere){
        PoseStack matrices = context.poseStack();
        Vec3 camera = context.levelState().cameraRenderState.pos;

        matrices.pushPose();
        matrices.translate(-camera.x, -camera.y, -camera.z);

        if (buffer == null) {
            buffer = new BufferBuilder(allocator, SPHERE_RENDER_PIPELINE.getVertexFormatMode(), SPHERE_RENDER_PIPELINE.getVertexFormat());
        }

        renderSphereBox(matrices.last().pose(), buffer, sphere);

        matrices.popPose();
    }

    private static void renderSphereBox(Matrix4fc positionMatrix, BufferBuilder buffer, SphereData sphere){
        //10% bigger than actual sphere radius, to ensure the fully sphere can render.
        double radius = sphere.radius() * 1.1;
        Vec3 pos = sphere.pos();
        float minX = (float) (pos.x - radius);
        float maxX = (float)(pos.x + radius);
        float minY = (float)(pos.y - radius);
        float maxY = (float)(pos.y + radius);
        float minZ = (float)(pos.z - radius);
        float maxZ = (float)(pos.z + radius);

        buffer.addVertex(positionMatrix, minX, minY, maxZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, maxX, minY, maxZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, maxX, maxY, maxZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, minX, maxY, maxZ).setColor(0, 0, 0, 0);

        // Back face
        buffer.addVertex(positionMatrix, maxX, minY, minZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, minX, minY, minZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, minX, maxY, minZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, maxX, maxY, minZ).setColor(0, 0, 0, 0);

        // Left face
        buffer.addVertex(positionMatrix, minX, minY, minZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, minX, minY, maxZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, minX, maxY, maxZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, minX, maxY, minZ).setColor(0, 0, 0, 0);

        // Right face
        buffer.addVertex(positionMatrix, maxX, minY, maxZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, maxX, minY, minZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, maxX, maxY, minZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, maxX, maxY, maxZ).setColor(0, 0, 0, 0);

        // Top face
        buffer.addVertex(positionMatrix, minX, maxY, maxZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, maxX, maxY, maxZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, maxX, maxY, minZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, minX, maxY, minZ).setColor(0, 0, 0, 0);

        // Bottom face
        buffer.addVertex(positionMatrix, minX, minY, minZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, maxX, minY, minZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, maxX, minY, maxZ).setColor(0, 0, 0, 0);
        buffer.addVertex(positionMatrix, minX, minY, maxZ).setColor(0, 0, 0, 0);

    }

    private static void drawSphere(Minecraft client, @SuppressWarnings("SameParameterValue") RenderPipeline pipeline, SphereData sphere) {
        // Build the buffer
        MeshData builtBuffer = buffer.buildOrThrow();
        MeshData.DrawState drawParameters = builtBuffer.drawState();
        VertexFormat format = drawParameters.format();

        GpuBuffer vertices = upload(drawParameters, format, builtBuffer);
        GpuBuffer uniforms = uploadSphere(sphere);

        draw(client, pipeline, builtBuffer, drawParameters, vertices, format, uniforms);

        // Rotate the vertex buffer so we are less likely to use buffers that the GPU is using
        vertexBuffer.rotate();
        sphereUniformBuffer.rotate();
        buffer = null;
    }

    private static GpuBuffer uploadSphere(SphereData sphere){
        if (sphereUniformBuffer == null) {
            sphereUniformBuffer = new MappableRingBuffer(() -> "Sphere uniform", GpuBuffer.USAGE_UNIFORM | GpuBuffer.USAGE_MAP_WRITE, 16 + 4);
        }

        CommandEncoder encoder = RenderSystem.getDevice().createCommandEncoder();

        try (GpuBuffer.MappedView view = encoder.mapBuffer(sphereUniformBuffer.currentBuffer().slice(0, 16+4), false, true)) {
            long ptr = MemoryUtil.memAddress(view.data());
            MemoryUtil.memPutFloat(ptr,      (float) sphere.pos().x);
            MemoryUtil.memPutFloat(ptr + 4,  (float) sphere.pos().y);
            MemoryUtil.memPutFloat(ptr + 8,  (float) sphere.pos().z);
            MemoryUtil.memPutFloat(ptr + 12, 0f);
            MemoryUtil.memPutFloat(ptr + 16, (float)sphere.radius());
        }

        return sphereUniformBuffer.currentBuffer();
    }

    private static void draw(Minecraft client, RenderPipeline pipeline, MeshData builtBuffer, MeshData.DrawState drawParameters, GpuBuffer vertices, VertexFormat format, GpuBuffer uniforms) {
        GpuBuffer indices;
        VertexFormat.IndexType indexType;

        if (pipeline.getVertexFormatMode() == VertexFormat.Mode.QUADS) {
            // Sort the quads if there is translucency
            builtBuffer.sortQuads(allocator, RenderSystem.getProjectionType().vertexSorting());
            // Upload the index buffer
            indices = pipeline.getVertexFormat().uploadImmediateIndexBuffer(builtBuffer.indexBuffer());
            indexType = builtBuffer.drawState().indexType();
        } else {
            // Use the general shape index buffer for non-quad draw modes
            RenderSystem.AutoStorageIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(pipeline.getVertexFormatMode());
            indices = shapeIndexBuffer.getBuffer(drawParameters.indexCount());
            indexType = shapeIndexBuffer.type();
        }

        // Actually execute the draw

        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                .writeTransform(RenderSystem.getModelViewMatrix(), COLOR_MODULATOR, MODEL_OFFSET, TEXTURE_MATRIX);

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> DrStone.MOD_ID + " example render pipeline rendering", client.getMainRenderTarget().getColorTextureView(), OptionalInt.empty(), client.getMainRenderTarget().getDepthTextureView(), OptionalDouble.empty())) {
            renderPass.setPipeline(pipeline);

            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicTransforms);

            renderPass.setUniform("Sphere", uniforms.slice(0, 16+4));

            // Bind texture if applicable:
            // Sampler0 is used for texture inputs in vertices
            // renderPass.bindTexture("Sampler0", textureSetup.texure0(), textureSetup.sampler0());

            renderPass.setVertexBuffer(0, vertices);
            renderPass.setIndexBuffer(indices, indexType);

            // The base vertex is the starting index when we copied the data into the vertex buffer divided by vertex size
            //noinspection ConstantValue
            renderPass.drawIndexed(0 / format.getVertexSize(), 0, drawParameters.indexCount(), 1);
        }

        builtBuffer.close();
    }

    private static GpuBuffer upload(MeshData.DrawState drawParameters, VertexFormat format, MeshData builtBuffer) {
        // Calculate the size needed for the vertex buffer
        int vertexBufferSize = drawParameters.vertexCount() * format.getVertexSize();

        // Initialize or resize the vertex buffer as needed
        if (vertexBuffer == null || vertexBuffer.size() < vertexBufferSize) {
            if (vertexBuffer != null) {
                vertexBuffer.close();
            }

            vertexBuffer = new MappableRingBuffer(() -> DrStone.MOD_ID + " example render pipeline", GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_MAP_WRITE, vertexBufferSize);
        }

        // Copy vertex data into the vertex buffer
        CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();

        try (GpuBuffer.MappedView mappedView = commandEncoder.mapBuffer(vertexBuffer.currentBuffer().slice(0, builtBuffer.vertexBuffer().remaining()), false, true)) {
            MemoryUtil.memCopy(builtBuffer.vertexBuffer(), mappedView.data());
        }

        return vertexBuffer.currentBuffer();
    }

    public static void close() {
        allocator.close();

        if (vertexBuffer != null) {
            vertexBuffer.close();
            vertexBuffer = null;
        }
        if (sphereUniformBuffer != null){
            sphereUniformBuffer.close();
            sphereUniformBuffer = null;
        }
    }

    public static void allSphereDataHandler(AllSphereData payload, ClientPlayNetworking.Context context){
        ClientLevel level = context.client().level;

        if (level == null) {
            return;
        }

        SphereData = payload.spheres();
    }


}
