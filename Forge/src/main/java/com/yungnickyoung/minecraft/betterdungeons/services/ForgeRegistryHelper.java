package com.yungnickyoung.minecraft.betterdungeons.services;

import com.yungnickyoung.minecraft.betterdungeons.module.StructurePieceTypeModuleForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class ForgeRegistryHelper implements IRegistryHelper {
    @Override
    public void registerStructurePieceType(ResourceLocation resourceLocation, StructurePieceType structurePieceType) {
        StructurePieceTypeModuleForge.STRUCTURE_PIECE_TYPES.put(resourceLocation, structurePieceType);
    }
}
