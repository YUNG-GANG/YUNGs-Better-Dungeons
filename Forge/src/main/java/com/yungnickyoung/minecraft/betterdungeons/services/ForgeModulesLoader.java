package com.yungnickyoung.minecraft.betterdungeons.services;

import com.yungnickyoung.minecraft.betterdungeons.module.*;

public class ForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        IModulesLoader.super.loadModules(); // Load common modules
        ConfigModuleForge.init();
        StructureProcessorTypeModuleForge.init();
        StructureTypeModuleForge.init();
        StructurePieceTypeModuleForge.init();
    }
}
