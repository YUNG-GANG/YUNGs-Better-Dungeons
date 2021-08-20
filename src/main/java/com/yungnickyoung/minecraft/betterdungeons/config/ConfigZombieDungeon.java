package com.yungnickyoung.minecraft.betterdungeons.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigZombieDungeon {
    @ConfigEntry.Gui.Tooltip(count = 3)
    public int zombieDungeonStartMinY = 50;

    @ConfigEntry.Gui.Tooltip(count = 3)
    public int zombieDungeonStartMaxY = 51;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean enableZombieDungeons = true;

    @ConfigEntry.Gui.Tooltip(count = 3)
    public int zombieDungeonSeparationDistance = 48;

    @ConfigEntry.Gui.Tooltip(count = 3)
    public int zombieDungeonMaxSurfaceStaircaseLength = 20;

    @ConfigEntry.Gui.Tooltip(count = 5)
    public String whitelistedDimensions = "[minecraft:overworld]";

    @ConfigEntry.Gui.Tooltip(count = 5)
    public String blacklistedBiomes = "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river, minecraft:deep_warm_ocean]";
}