package com.yungnickyoung.minecraft.betterdungeons.services;

import com.yungnickyoung.minecraft.betterdungeons.module.ConfigModuleFabric;
import com.yungnickyoung.minecraft.betterdungeons.module.VanillaRemovalModuleFabric;

public class FabricModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        IModulesLoader.super.loadModules(); // Load common modules
        ConfigModuleFabric.init();
        VanillaRemovalModuleFabric.init();
    }
}
