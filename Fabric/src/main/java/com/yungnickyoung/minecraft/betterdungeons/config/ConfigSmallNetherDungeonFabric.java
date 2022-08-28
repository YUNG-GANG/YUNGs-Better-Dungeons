package com.yungnickyoung.minecraft.betterdungeons.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigSmallNetherDungeonFabric {
    @ConfigEntry.Gui.Tooltip
    public boolean enabled = false;

    @ConfigEntry.Gui.Tooltip
    public boolean witherSkeletonsDropWitherSkulls = true;

    @ConfigEntry.Gui.Tooltip
    public boolean blazesDropBlazeRods = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    @ConfigEntry.BoundedDiscrete(min = 0, max = 8)
    public int bannerMaxCount = 2;
}
