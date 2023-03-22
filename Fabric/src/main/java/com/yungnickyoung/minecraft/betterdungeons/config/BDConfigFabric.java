package com.yungnickyoung.minecraft.betterdungeons.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name="betterdungeons-fabric-1_19_4")
public class BDConfigFabric implements ConfigData {
    @ConfigEntry.Category("Better Dungeons")
    @ConfigEntry.Gui.TransitiveObject
    public ConfigBetterDungeonsFabric betterDungeons = new ConfigBetterDungeonsFabric();
}
