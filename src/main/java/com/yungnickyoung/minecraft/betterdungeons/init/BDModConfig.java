package com.yungnickyoung.minecraft.betterdungeons.init;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon.SkeletonDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.small_dungeon.SmallDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon.ZombieDungeonStructure;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;

public class BDModConfig {
    public static void init() {
        // Register mod config with Forge
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, BDConfig.SPEC, "betterdungeons-forge-1_18.toml");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BDModConfig::configChanged);
    }

    /**
     * Parses the whitelisted dimensions & blacklisted biomes strings and updates the stored values.
     */
    private static void configChanged(ModConfigEvent event) {
        ModConfig config = event.getConfig();

        if (config.getSpec() == BDConfig.SPEC) {
            // Dimension whitelist
            SmallDungeonStructure.whitelistedDimensions = createDimensionWhitelist(BDConfig.smallDungeons.whitelistedDimensions, SmallDungeonStructure.whitelistedDimensions);
            SpiderDungeonStructure.whitelistedDimensions = createDimensionWhitelist(BDConfig.spiderDungeons.whitelistedDimensions, SpiderDungeonStructure.whitelistedDimensions);
            SkeletonDungeonStructure.whitelistedDimensions = createDimensionWhitelist(BDConfig.skeletonDungeons.whitelistedDimensions, SkeletonDungeonStructure.whitelistedDimensions);
            ZombieDungeonStructure.whitelistedDimensions = createDimensionWhitelist(BDConfig.zombieDungeons.whitelistedDimensions, ZombieDungeonStructure.whitelistedDimensions);

            // Biome blacklist
            SmallDungeonStructure.blacklistedBiomes = createBiomeBlacklist(BDConfig.smallDungeons.blacklistedBiomes, SmallDungeonStructure.blacklistedBiomes);
            SpiderDungeonStructure.blacklistedBiomes = createBiomeBlacklist(BDConfig.spiderDungeons.blacklistedBiomes, SpiderDungeonStructure.blacklistedBiomes);
            SkeletonDungeonStructure.blacklistedBiomes = createBiomeBlacklist(BDConfig.skeletonDungeons.blacklistedBiomes, SkeletonDungeonStructure.blacklistedBiomes);
            ZombieDungeonStructure.blacklistedBiomes = createBiomeBlacklist(BDConfig.zombieDungeons.blacklistedBiomes, ZombieDungeonStructure.blacklistedBiomes);

            // Validate small dungeon spacing
            int spacing = BDConfig.smallDungeons.smallDungeonSeparationDistance.get();
            int separation = BDConfig.smallDungeons.smallDungeonDistanceVariation.get();
            if (spacing < separation) {
                BetterDungeons.LOGGER.error("Small Dungeon Average Separation Distance cannot be less than Small Dungeon Separation Distance Variation!");
                BetterDungeons.LOGGER.error("Found: {} and {}", spacing, separation);
                BetterDungeons.LOGGER.error("Using default values instead...");
                BDConfig.smallDungeons.smallDungeonSeparationDistance.set(10);
                BDConfig.smallDungeons.smallDungeonDistanceVariation.set(6);
            }
        }
    }

    private static List<String> createDimensionWhitelist(
            ForgeConfigSpec.ConfigValue<String> dimWhitelistConfig,
            List<String> defaultList
    ) {
        // Dimension whitelisting
        String rawStringofList = dimWhitelistConfig.get();
        int strLen = rawStringofList.length();

        // Validate the string's format
        if (strLen < 2 || rawStringofList.charAt(0) != '[' || rawStringofList.charAt(strLen - 1) != ']') {
            BetterDungeons.LOGGER.error("INVALID VALUE FOR SETTING 'Whitelisted Dimensions'. Using [minecraft:overworld] instead...");
            return defaultList;
        }

        // Parse string to list
        return Lists.newArrayList(rawStringofList.substring(1, strLen - 1).split(",\\s*"));
    }

    private static List<String> createBiomeBlacklist(
            ForgeConfigSpec.ConfigValue<String> biomeBlacklistConfig,
            List<String> defaultList
    ) {
        // Biome blacklisting
        String rawStringofList = biomeBlacklistConfig.get();
        int strLen = rawStringofList.length();

        // Validate the string's format
        if (strLen < 2 || rawStringofList.charAt(0) != '[' || rawStringofList.charAt(strLen - 1) != ']') {
            BetterDungeons.LOGGER.error("INVALID VALUE FOR SETTING 'Blacklisted Biomes'. Using default instead...");
            return defaultList;
        }

        // Parse string to list
        return Lists.newArrayList(rawStringofList.substring(1, strLen - 1).split(",\\s*"));
    }
}
