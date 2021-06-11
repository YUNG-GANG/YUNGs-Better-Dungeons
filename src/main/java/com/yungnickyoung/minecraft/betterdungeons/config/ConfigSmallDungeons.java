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

    public ConfigSmallDungeons(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
            .comment(
                "##########################################################################################################\n" +
                "# Small Dungeon settings.\n" +
                "##########################################################################################################")
            .push("Small Dungeons");

        smallDungeonMinY = BUILDER
            .comment(
                " The minimum y-value at which small dungeons can spawn.\n" +
                " Default: 11")
            .worldRestart()
            .define("Small Dungeon Min Y", 11);

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
                " The average number of chunks between adjacent small dungeons.\n" +
                " This controls how often small dungeons spawn. Higher value = more rare.\n" +
                " Default: 32")
            .worldRestart()
            .define("Small Dungeon Average Separation Distance", 32);

        BUILDER.pop();
    }
}
