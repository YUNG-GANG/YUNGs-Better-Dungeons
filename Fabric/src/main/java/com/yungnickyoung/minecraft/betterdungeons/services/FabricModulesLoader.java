package com.yungnickyoung.minecraft.betterdungeons.services;

import com.yungnickyoung.minecraft.betterdungeons.module.*;

public class FabricModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        ConfigModuleFabric.init();
        TagModuleFabric.init();
        StructureProcessorModuleFabric.init();
        StructureFeatureModuleFabric.init();
        StructureFeaturePieceModuleFabric.init();
    }
}
