package com.skits4107.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.Vec3;

import static com.skits4107.core.Codecs.VEC3_CODEC;

public record MedusaData(float t, float d, boolean active, Vec3 pos) {
    public static final Codec<MedusaData> MEDUSA_DATA_CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.FLOAT.fieldOf("t").forGetter(MedusaData::t),
                            Codec.FLOAT.fieldOf("d").forGetter(MedusaData::d),
                            Codec.BOOL.fieldOf("active").forGetter(MedusaData::active),
                            VEC3_CODEC.fieldOf("pos").forGetter(MedusaData::pos)
                    ).apply(instance, MedusaData::new)
            );
}
