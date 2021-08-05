package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigSkeletonDungeon {
    public final ForgeConfigSpec.ConfigValue<Integer> skeletonDungeonStartMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> skeletonDungeonStartMaxY;
    public final ForgeConfigSpec.ConfigValue<Boolean> enableSkeletonDungeons;
    public final ForgeConfigSpec.ConfigValue<Integer> skeletonDungeonSeparationDistance;
    public final ForgeConfigSpec.ConfigValue<String> whitelistedDimensions;
    public final ForgeConfigSpec.ConfigValue<String> blacklistedBiomes;

    public ConfigSkeletonDungeon(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
            .comment(
                "##########################################################################################################\n" +
                "# Skeleton Dungeon settings.\n" +
                "##########################################################################################################")
            .push("Skeleton Dungeons");

        skeletonDungeonStartMinY = BUILDER
            .comment(
                " The minimum y-value at which the STARTING POINT of the skeleton dungeon can spawn.\n" +
                " Note that this point is the bottom of the skeleton dungeon, so the various pieces of the dungeon can extend above it.\n" +
                " Default: 11")
            .worldRestart()
            .define("Skeleton Dungeon Min Start Y", 11);

        skeletonDungeonStartMaxY = BUILDER
            .comment(
                " The maximum y-value at which the STARTING POINT of the skeleton dungeon can spawn.\n" +
                " Note that this point is the bottom of the skeleton dungeon, so the various pieces of the dungeon can extend above it.\n" +
                " Default: 30")
            .worldRestart()
            .define("Skeleton Dungeon Max Start Y", 30);

        enableSkeletonDungeons = BUILDER
            .comment(
                " Whether or not Skeleton Dungeons from Better Dungeons should spawn.\n" +
                " Default: true")
            .worldRestart()
            .define("Spawn Skeleton Dungeons", true);

        skeletonDungeonSeparationDistance = BUILDER
            .comment(
                " The average number of chunks between adjacent Skeleton Dungeons.\n" +
                " This controls how often Skeleton Dungeons spawn. Higher value = more rare.\n" +
                " Default: 46")
            .worldRestart()
            .define("Skeleton Dungeon Average Separation Distance", 46);

        whitelistedDimensions = BUILDER
            .comment(
                " List of dimensions that will have Skeleton Dungeons.\n" +
                " List must be comma-separated values enclosed in square brackets.\n" +
                " Entries must have the mod namespace included.\n" +
                " For example: \"[minecraft:overworld, minecraft:the_nether, undergarden:undergarden]\"\n" +
                " Default: \"[minecraft:overworld]\"")
            .worldRestart()
            .define("Whitelisted Dimensions", "[minecraft:overworld]");

        blacklistedBiomes = BUILDER
            .comment(
                " List of biomes that will NOT have Skeleton Dungeons.\n" +
                " List must be comma-separated values enclosed in square brackets.\n" +
                " Entries must have the mod namespace included.\n" +
                " For example: \"[minecraft:plains, byg:alps]\"\n" +
                " Default: \"[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river]\"")
            .worldRestart()
            .define("Blacklisted Biomes", "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river]");

        BUILDER.pop();
    }
}