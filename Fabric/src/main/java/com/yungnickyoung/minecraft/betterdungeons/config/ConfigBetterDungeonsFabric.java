package com.yungnickyoung.minecraft.betterdungeons.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigBetterDungeonsFabric {
    @ConfigEntry.Category("General Settings")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public ConfigGeneralFabric general = new ConfigGeneralFabric();

    @ConfigEntry.Category("Zombie Dungeons")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public ConfigZombieDungeonFabric zombieDungeon = new ConfigZombieDungeonFabric();

    @ConfigEntry.Category("Small Dungeons")
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public ConfigSmallDungeonFabric smallDungeon = new ConfigSmallDungeonFabric();
}
