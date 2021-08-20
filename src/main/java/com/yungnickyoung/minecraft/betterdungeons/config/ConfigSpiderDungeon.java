package com.yungnickyoung.minecraft.betterdungeons.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigSpiderDungeon {
    @ConfigEntry.Gui.Tooltip(count = 4)
    public int spiderDungeonStartMinY = 70;

    @ConfigEntry.Gui.Tooltip(count = 4)
    public int spiderDungeonStartMaxY = 71;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean enableSpiderDungeons = true;

    @ConfigEntry.Gui.Tooltip(count = 3)
    public int spiderDungeonSeparationDistance = 44;

    @ConfigEntry.Gui.Tooltip(count = 5)
    public String whitelistedDimensions = "[minecraft:overworld]";

    @ConfigEntry.Gui.Tooltip(count = 5)
    public String blacklistedBiomes = "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river, minecraft:deep_warm_ocean]";
}