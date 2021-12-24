package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigSmallDungeons {
    public final ForgeConfigSpec.ConfigValue<Integer> smallDungeonMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> smallDungeonMaxY;
    public final ForgeConfigSpec.ConfigValue<Integer> bannerMaxCount;
    public final ForgeConfigSpec.ConfigValue<Integer> chestMinCount;
    public final ForgeConfigSpec.ConfigValue<Integer> chestMaxCount;
    public final ForgeConfigSpec.ConfigValue<Boolean> enableSmallDungeons;
    public final ForgeConfigSpec.ConfigValue<Integer> smallDungeonSeparationDistance;
    public final ForgeConfigSpec.ConfigValue<Integer> smallDungeonDistanceVariation;
    public final ForgeConfigSpec.ConfigValue<String> whitelistedDimensions;
    public final ForgeConfigSpec.ConfigValue<String> blacklistedBiomes;
    public final ForgeConfigSpec.ConfigValue<Boolean> enableOreProps;

    public ConfigSmallDungeons(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
                .comment(
                        """
                                ##########################################################################################################
                                # Small Dungeon settings.
                                ##########################################################################################################""")
                .push("Small Dungeons");

        smallDungeonMinY = BUILDER
                .comment(
                        " The minimum y-value at which small dungeons can spawn.\n" +
                        " Default: -50")
                .worldRestart()
                .define("Small Dungeon Min Y", -50);

        smallDungeonMaxY = BUILDER
                .comment(
                        " The maximum y-value at which small dungeons can spawn.\n" +
                        " Default: 50")
                .worldRestart()
                .define("Small Dungeon Max Start Y", 50);

        bannerMaxCount = BUILDER
                .comment(
                        " The maximum number of banners that can spawn in a single small dungeon.\n" +
                        " Default: 2")
                .worldRestart()
                .defineInRange("Small Dungeon Max Banner Count", 2, 0, 8);

        chestMinCount = BUILDER
                .comment(
                        " The minimum number of chests that are guaranteed to spawn in a single small dungeon.\n" +
                        " Default: 1")
                .worldRestart()
                .define("Small Dungeon Min Chest Count", 1);

        chestMaxCount = BUILDER
                .comment(
                        " The maximum number of chests that can spawn in a single small dungeon.\n" +
                        " Default: 2")
                .worldRestart()
                .define("Small Dungeon Max Chest Count", 2);

        enableSmallDungeons = BUILDER
                .comment(
                        " Whether or not the small dungeons (monster rooms) from Better Dungeons should spawn.\n" +
                        " Default: true")
                .worldRestart()
                .define("Spawn Small Dungeons", true);

        smallDungeonSeparationDistance = BUILDER
                .comment(
                        """
                                The average number of chunks between adjacent small dungeons.
                                This controls how often small dungeons spawn. Higher value = more rare.
                                Default: 10""".indent(1))
                .worldRestart()
                .define("Small Dungeon Average Separation Distance", 10);

        smallDungeonDistanceVariation = BUILDER
                .comment(
                        """
                                The variation in distance between small dungeons, in chunks.
                                This, combined with the Small Dungeon Average Separation Distance, controls the distribution
                                of spawn chances for small dungeons.
                                For example, if Small Dungeon Average Separation Distance is set to 10, and this value is set to 6, then
                                small dungeons will have anywhere from 10 - 6 = 4 chunks to 10 + 6 = 16 chunks between them.
                                In other words, spawn distribution = average distance +/- variation
                                Default: 6""".indent(1))
                .worldRestart()
                .define("Small Dungeon Separation Distance Variation", 6);

        whitelistedDimensions = BUILDER
                .comment(
                        """
                                List of dimensions that will have Small Dungeons.
                                List must be comma-separated values enclosed in square brackets.
                                Entries must have the mod namespace included.
                                For example: "[minecraft:overworld, minecraft:the_nether, undergarden:undergarden]"
                                Default: "[minecraft:overworld]\"""".indent(1))
                .worldRestart()
                .define("Small Dungeon Whitelisted Dimensions", "[minecraft:overworld]");

        blacklistedBiomes = BUILDER
                .comment(
                        """
                                List of biomes that will NOT have Small Dungeons.
                                List must be comma-separated values enclosed in square brackets.
                                Entries must have the mod namespace included.
                                For example: "[minecraft:plains, byg:alps]"
                                Default: "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river, minecraft:deep_warm_ocean]\"""".indent(1))
                .worldRestart()
                .define("Small Dungeon Blacklisted Biomes", "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river, minecraft:deep_warm_ocean]");

        enableOreProps = BUILDER
                .comment(
                        """
                                Whether or not Small Dungeons can rarely place ore blocks in the corners of the dungeon.
                                If this is set to false, any ore blocks that spawn as part of a corner prop will instead be replaced with air.
                                Default: true""".indent(1))
                .worldRestart()
                .define("Allow Ore Blocks in Corners", true);

        BUILDER.pop();
    }
}
