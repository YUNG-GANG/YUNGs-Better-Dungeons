package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import net.minecraftforge.fml.ModLoadingContext;

public class BDModConfig {
    public static void init() {
        // Register mod config with Forge
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, BDConfig.SPEC, "betterdungeons-forge-1_18.toml");
    }
}
