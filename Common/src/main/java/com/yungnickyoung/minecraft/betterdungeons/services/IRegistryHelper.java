package com.yungnickyoung.minecraft.betterdungeons.services;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public interface IRegistryHelper {
    void registerStructurePieceType(ResourceLocation resourceLocation, StructurePieceType structurePieceType);
}
