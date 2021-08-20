package com.yungnickyoung.minecraft.betterdungeons.config;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon.SkeletonDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.small_dungeon.SmallDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon.ZombieDungeonStructure;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.List;

@Config(name="betterdungeons-fabric-1_16")
public class BDConfig implements ConfigData {
    @ConfigEntry.Category("Better Dungeons")
    @ConfigEntry.Gui.TransitiveObject
    public ConfigBetterDungeons betterDungeons = new ConfigBetterDungeons();

    /**
     * Parses the whitelisted dimensions & blacklisted biomes strings and updates the stored values.
     */
    @Override
    public void validatePostLoad() {
        // Dimension whitelist
        SmallDungeonStructure.whitelistedDimensions = createDimensionWhitelist(betterDungeons.smallDungeon.whitelistedDimensions, SmallDungeonStructure.whitelistedDimensions);
        SpiderDungeonStructure.whitelistedDimensions = createDimensionWhitelist(betterDungeons.spiderDungeon.whitelistedDimensions, SpiderDungeonStructure.whitelistedDimensions);
        SkeletonDungeonStructure.whitelistedDimensions = createDimensionWhitelist(betterDungeons.skeletonDungeon.whitelistedDimensions, SkeletonDungeonStructure.whitelistedDimensions);
        ZombieDungeonStructure.whitelistedDimensions = createDimensionWhitelist(betterDungeons.zombieDungeon.whitelistedDimensions, ZombieDungeonStructure.whitelistedDimensions);

        // Biome blacklist
        SmallDungeonStructure.blacklistedBiomes = createBiomeBlacklist(betterDungeons.smallDungeon.blacklistedBiomes, SmallDungeonStructure.blacklistedBiomes);
        SpiderDungeonStructure.blacklistedBiomes = createBiomeBlacklist(betterDungeons.spiderDungeon.blacklistedBiomes, SpiderDungeonStructure.blacklistedBiomes);
        SkeletonDungeonStructure.blacklistedBiomes = createBiomeBlacklist(betterDungeons.skeletonDungeon.blacklistedBiomes, SkeletonDungeonStructure.blacklistedBiomes);
        ZombieDungeonStructure.blacklistedBiomes = createBiomeBlacklist(betterDungeons.zombieDungeon.blacklistedBiomes, ZombieDungeonStructure.blacklistedBiomes);

        // Validate small dungeon spacing
        int spacing = betterDungeons.smallDungeon.smallDungeonSeparationDistance;
        int separation = betterDungeons.smallDungeon.smallDungeonDistanceVariation;
        if (spacing < separation) {
            BetterDungeons.LOGGER.error("Small Dungeon Average Separation Distance cannot be less than Small Dungeon Separation Distance Variation!");
            BetterDungeons.LOGGER.error("Found: {} and {}", spacing, separation);
            BetterDungeons.LOGGER.error("Using default values instead...");
            betterDungeons.smallDungeon.smallDungeonSeparationDistance = 10;
            betterDungeons.smallDungeon.smallDungeonDistanceVariation = 6;
        }
    }

    private List<String> createDimensionWhitelist(String dimensionWhitelistString, List<String> defaultList) {
        // Dimension whitelisting
        int strLen = dimensionWhitelistString.length();

        // Validate the string's format
        if (strLen < 2 || dimensionWhitelistString.charAt(0) != '[' || dimensionWhitelistString.charAt(strLen - 1) != ']') {
            BetterDungeons.LOGGER.error("INVALID VALUE FOR SETTING 'Whitelisted Dimensions'. Using [minecraft:overworld] instead...");
            return defaultList;
        }

        // Parse string to list
        return Lists.newArrayList(dimensionWhitelistString.substring(1, strLen - 1).split(",\\s*"));
    }

    private List<String> createBiomeBlacklist(String biomeBlacklistString, List<String> defaultList) {
        // Biome blacklisting
        int strLen = biomeBlacklistString.length();

        // Validate the string's format
        if (strLen < 2 || biomeBlacklistString.charAt(0) != '[' || biomeBlacklistString.charAt(strLen - 1) != ']') {
            BetterDungeons.LOGGER.error("INVALID VALUE FOR SETTING 'Blacklisted Biomes'. Using default instead...");
            return defaultList;
        }

        // Parse string to list
        return Lists.newArrayList(biomeBlacklistString.substring(1, strLen - 1).split(",\\s*"));
    }
}
