package com.yungnickyoung.minecraft.betterdungeons.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigGeneral {
    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean enableHeads = true;

    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean removeVanillaDungeons = true;

    @ConfigEntry.Gui.Tooltip(count = 5)
    public boolean enableNetherBlocks = true;
}
