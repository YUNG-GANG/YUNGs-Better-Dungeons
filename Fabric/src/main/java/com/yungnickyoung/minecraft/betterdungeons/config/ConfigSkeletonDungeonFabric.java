package com.yungnickyoung.minecraft.betterdungeons.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigSkeletonDungeonFabric {
    @ConfigEntry.Gui.Tooltip(count = 3)
    public int skeletonDungeonStartMinY = -50;

    @ConfigEntry.Gui.Tooltip(count = 3)
    public int skeletonDungeonStartMaxY = -30;
}
