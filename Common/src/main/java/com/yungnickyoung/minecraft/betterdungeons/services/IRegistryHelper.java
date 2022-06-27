package com.yungnickyoung.minecraft.betterdungeons.services;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public interface IRegistryHelper {
    void registerStructureType(ResourceLocation resourceLocation, StructureType<? extends Structure> structureType);

    void registerStructurePieceType(ResourceLocation resourceLocation, StructurePieceType structurePieceType);

    void registerStructureProcessorType(ResourceLocation resourceLocation, StructureProcessorType<? extends StructureProcessor> structureProcessorType);
}
