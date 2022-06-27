package com.yungnickyoung.minecraft.betterdungeons.services;

import com.yungnickyoung.minecraft.betterdungeons.module.StructurePieceTypeModule;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureProcessorTypeModule;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureTypeModule;

public interface IModulesLoader {
    default void loadModules() {
        StructureTypeModule.init();
        StructurePieceTypeModule.init();
        StructureProcessorTypeModule.init();
    }
}
