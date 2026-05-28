package com.skits4107.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public record SphereData(Vec3 pos, float radius) {


    public static final StreamCodec<RegistryFriendlyByteBuf, SphereData> SPHERE_STREAM_CODEC = StreamCodec.composite(
            NetworkCodecs.VEC_3_STREAM_CODEC, SphereData::pos,
            ByteBufCodecs.FLOAT, SphereData::radius,
            SphereData::new
    );
}
