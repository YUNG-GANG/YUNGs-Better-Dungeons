package com.yungnickyoung.minecraft.betterdungeons.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigSmallDungeons {
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

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean enableSmallDungeons = true;

    @ConfigEntry.Gui.Tooltip(count = 3)
    public int smallDungeonSeparationDistance = 10;

    @ConfigEntry.Gui.Tooltip(count = 7)
    public int smallDungeonDistanceVariation = 6;

    @ConfigEntry.Gui.Tooltip(count = 5)
    public String whitelistedDimensions = "[minecraft:overworld]";

    @ConfigEntry.Gui.Tooltip(count = 5)
    public String blacklistedBiomes = "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river, minecraft:deep_warm_ocean]";

    @ConfigEntry.Gui.Tooltip(count = 3)
    public boolean enableOreProps = true;
}
