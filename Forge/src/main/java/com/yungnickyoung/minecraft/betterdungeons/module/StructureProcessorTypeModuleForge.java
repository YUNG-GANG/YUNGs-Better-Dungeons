package com.yungnickyoung.minecraft.betterdungeons.module;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;
import java.util.Map;

public class StructureProcessorTypeModuleForge {
    public static Map<ResourceLocation, StructureProcessorType<? extends StructureProcessor>> STRUCTURE_PROCESSOR_TYPES = new HashMap<>();

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(StructureProcessorTypeModuleForge::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> STRUCTURE_PROCESSOR_TYPES.forEach((name, structurePieceType) -> Registry.register(Registry.STRUCTURE_PROCESSOR, name, structurePieceType)));
    }
}
