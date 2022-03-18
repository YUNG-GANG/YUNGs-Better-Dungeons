package com.yungnickyoung.minecraft.betterdungeons.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigSpiderDungeonFabric {
    @ConfigEntry.Gui.Tooltip(count = 4)
    public int spiderDungeonStartMinY = 70;

    @ConfigEntry.Gui.Tooltip(count = 4)
    public int spiderDungeonStartMaxY = 71;
}
