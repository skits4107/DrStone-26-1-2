package com.skits4107.components;

import com.skits4107.DrStone;
import com.skits4107.core.MedusaData;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class ModComponents {

    public static final DataComponentType<MedusaData> MEDUSA_DATA_COMPONENT_TYPE = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(DrStone.MOD_ID, "medusa_data"),
            DataComponentType.<MedusaData>builder().persistent(MedusaData.MEDUSA_DATA_CODEC).build()
    );

    public static void init(){}
}
