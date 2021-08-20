package com.yungnickyoung.minecraft.betterdungeons.init;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

public class BDModConfig {
    public static void init() {
        AutoConfig.register(BDConfig.class, Toml4jConfigSerializer::new);
        BetterDungeons.CONFIG = AutoConfig.getConfigHolder(BDConfig.class).getConfig();
    }
}
