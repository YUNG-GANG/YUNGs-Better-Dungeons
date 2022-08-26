package com.yungnickyoung.minecraft.betterdungeons.services;

import com.yungnickyoung.minecraft.betterdungeons.module.StructurePieceTypeModule;

public interface IModulesLoader {
    default void loadModules() {
        StructurePieceTypeModule.init();
    }
}
