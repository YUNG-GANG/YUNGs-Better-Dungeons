package com.yungnickyoung.minecraft.betterdungeons.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigSmallDungeonFabric {
    @ConfigEntry.Gui.Tooltip(count = 2)
    public int smallDungeonMinY = -50;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public int smallDungeonMaxY = 50;

    @ConfigEntry.Gui.Tooltip(count = 2)
    @ConfigEntry.BoundedDiscrete(min = 0, max = 8)
    public int bannerMaxCount = 2;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public int chestMinCount = 1;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public int chestMaxCount = 2;

    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean enableOreProps = true;
}
