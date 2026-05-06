package com.skits4107.network;

import com.skits4107.DrStone;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public record AllSphereData(List<SphereData> spheres) implements CustomPacketPayload {

    public static final Identifier ALL_SPHERE_ID = Identifier.fromNamespaceAndPath(DrStone.MOD_ID, "allspherepacket");

    public static final CustomPacketPayload.Type<AllSphereData> TYPE = new CustomPacketPayload.Type<>(ALL_SPHERE_ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, AllSphereData> ALL_SPHERE_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, SphereData.SPHERE_CODEC), AllSphereData::spheres,
            AllSphereData::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
