package com.yungnickyoung.minecraft.betterdungeons.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigBetterDungeons {
    @ConfigEntry.Category("General Settings")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public ConfigGeneral general = new ConfigGeneral();

    @ConfigEntry.Category("Spider Dungeons")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public ConfigSpiderDungeon spiderDungeon = new ConfigSpiderDungeon();

    @ConfigEntry.Category("Skeleton Dungeons")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public ConfigSkeletonDungeon skeletonDungeon = new ConfigSkeletonDungeon();

    @ConfigEntry.Category("Zombie Dungeons")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public ConfigZombieDungeon zombieDungeon = new ConfigZombieDungeon();

    @ConfigEntry.Category("Small Dungeons")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public ConfigSmallDungeons smallDungeon = new ConfigSmallDungeons();
}
