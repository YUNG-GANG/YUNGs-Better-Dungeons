package com.yungnickyoung.minecraft.betterdungeons.services;

import com.yungnickyoung.minecraft.betterdungeons.module.StructurePieceTypeModuleForge;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorTypeModuleForge;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureTypeModuleForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class ForgeRegistryHelper implements IRegistryHelper {
    @Override
    public void registerStructureType(ResourceLocation resourceLocation, StructureType<? extends Structure> structureType) {
        StructureTypeModuleForge.STRUCTURE_TYPES.put(resourceLocation, structureType);
    }

    @Override
    public void registerStructurePieceType(ResourceLocation resourceLocation, StructurePieceType structurePieceType) {
        StructurePieceTypeModuleForge.STRUCTURE_PIECE_TYPES.put(resourceLocation, structurePieceType);
    }

    @Override
    public void registerStructureProcessorType(ResourceLocation resourceLocation, StructureProcessorType<? extends StructureProcessor> structureProcessorType) {
        StructureProcessorTypeModuleForge.STRUCTURE_PROCESSOR_TYPES.put(resourceLocation, structureProcessorType);
    }
}
