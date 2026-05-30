package com.skits4107.entities;

import com.skits4107.DrStone;
import com.skits4107.entities.custom.PetrifiedEntity;
import com.skits4107.entities.custom.PlacedMedusa;
import com.skits4107.entities.custom.ThrownMedusa;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    public static final EntityType<PetrifiedEntity> PETRIFIED_ENTITY_TYPE = register(
            "petrified_entity",
            EntityType.Builder.<PetrifiedEntity>of(PetrifiedEntity::new, MobCategory.MISC).sized(1.0f, 1.0f)
            );

    public static final EntityType<PlacedMedusa> PLACED_MEDUSA_ENTITY_TYPE = register(
            "placed_medusa",
            EntityType.Builder.<PlacedMedusa>of(PlacedMedusa::new, MobCategory.MISC).sized(1.0f, 1.0f)
    );

    public static final EntityType<ThrownMedusa> THROWN_MEDUSA_ENTITY_TYPE = register(
            "thrown_medusa",
            EntityType.Builder.<ThrownMedusa>of(ThrownMedusa::new, MobCategory.MISC).sized(1.0f, 1.0f)
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(DrStone.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void init() {}
}
