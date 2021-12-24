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
                        """
                                ##########################################################################################################
                                # Skeleton Dungeon settings.
                                ##########################################################################################################""")
                .push("Skeleton Dungeons");

        skeletonDungeonStartMinY = BUILDER
                .comment(
                        """
                                The minimum y-value at which the STARTING POINT of the skeleton dungeon can spawn.
                                Note that this point is the bottom of the skeleton dungeon, so the various pieces of the dungeon can extend above it.
                                Default: -50""".indent(1))
                .worldRestart()
                .define("Skeleton Dungeon Min Start Y", -50);

        skeletonDungeonStartMaxY = BUILDER
                .comment(
                        """
                                The maximum y-value at which the STARTING POINT of the skeleton dungeon can spawn.
                                Note that this point is the bottom of the skeleton dungeon, so the various pieces of the dungeon can extend above it.
                                Default: -30""".indent(1))
                .worldRestart()
                .define("Skeleton Dungeon Max Start Y", -30);

        enableSkeletonDungeons = BUILDER
                .comment(
                        " Whether or not Skeleton Dungeons from Better Dungeons should spawn.\n" +
                        " Default: true")
                .worldRestart()
                .define("Spawn Skeleton Dungeons", true);

        skeletonDungeonSeparationDistance = BUILDER
                .comment(
                        """
                                The average number of chunks between adjacent Skeleton Dungeons.
                                This controls how often Skeleton Dungeons spawn. Higher value = more rare.
                                Default: 46""".indent(1))
                .worldRestart()
                .define("Skeleton Dungeon Average Separation Distance", 46);

        whitelistedDimensions = BUILDER
                .comment(
                        """
                                List of dimensions that will have Skeleton Dungeons.
                                List must be comma-separated values enclosed in square brackets.
                                Entries must have the mod namespace included.
                                For example: "[minecraft:overworld, minecraft:the_nether, undergarden:undergarden]"
                                Default: "[minecraft:overworld]\"""".indent(1))
                .worldRestart()
                .define("Skeleton Dungeon Whitelisted Dimensions", "[minecraft:overworld]");

        blacklistedBiomes = BUILDER
                .comment(
                        """
                                List of biomes that will NOT have Skeleton Dungeons.
                                List must be comma-separated values enclosed in square brackets.
                                Entries must have the mod namespace included.
                                For example: "[minecraft:plains, byg:alps]"
                                Default: "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river, minecraft:deep_warm_ocean]\"""".indent(1))
                .worldRestart()
                .define("Skeleton Dungeon Blacklisted Biomes", "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river, minecraft:deep_warm_ocean]");

        BUILDER.pop();
    }
}