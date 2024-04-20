package com.yungnickyoung.minecraft.betterdungeons.services;

import com.yungnickyoung.minecraft.betterdungeons.module.ConfigModuleNeoForge;

public class NeoForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        IModulesLoader.super.loadModules(); // Load common modules
        ConfigModuleNeoForge.init();
    }
}
