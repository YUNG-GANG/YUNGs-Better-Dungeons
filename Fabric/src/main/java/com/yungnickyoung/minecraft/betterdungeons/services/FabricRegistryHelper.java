package com.yungnickyoung.minecraft.betterdungeons.services;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class FabricRegistryHelper implements IRegistryHelper {
    @Override
    public void registerStructurePieceType(ResourceLocation resourceLocation, StructurePieceType structurePieceType) {
        Registry.register(Registry.STRUCTURE_PIECE, resourceLocation, structurePieceType);
    }
}
