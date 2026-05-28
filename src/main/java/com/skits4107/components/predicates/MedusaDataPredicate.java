package com.skits4107.components.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.skits4107.components.ModComponents;
import com.skits4107.core.MedusaData;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.predicates.DataComponentPredicate;

import java.util.Optional;

public record MedusaDataPredicate(Optional<Boolean> active) implements DataComponentPredicate
{
    public static final Codec<MedusaDataPredicate> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.BOOL.optionalFieldOf("active")
                                    .forGetter(MedusaDataPredicate::active)
                    ).apply(instance, MedusaDataPredicate::new)
            );

    @Override
    public boolean matches(DataComponentGetter components) {
        MedusaData data = components.get(ModComponents.MEDUSA_DATA_COMPONENT_TYPE);
        if (data == null){
            return false;
        }

        return active.map(expected -> data.active() == expected).orElse(true);
    }
}