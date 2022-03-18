package com.yungnickyoung.minecraft.betterdungeons.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigZombieDungeonFabric {
    @ConfigEntry.Gui.Tooltip(count = 3)
    public int zombieDungeonStartMinY = 50;

    @ConfigEntry.Gui.Tooltip(count = 3)
    public int zombieDungeonStartMaxY = 51;

    @ConfigEntry.Gui.Tooltip(count = 3)
    public int zombieDungeonMaxSurfaceStaircaseLength = 20;
}
