package com.yungnickyoung.minecraft.betterdungeons.services;

import com.yungnickyoung.minecraft.betterdungeons.module.*;

public class ForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        ConfigModuleForge.init();
        TagModuleForge.init();
        StructureProcessorModuleForge.init();
        StructureFeatureModuleForge.init();
        StructureFeaturePieceModuleForge.init();
    }
}
