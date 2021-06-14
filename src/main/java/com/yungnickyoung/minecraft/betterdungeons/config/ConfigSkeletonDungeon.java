package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigSkeletonDungeon {
    public final ForgeConfigSpec.ConfigValue<Integer> skeletonDungeonStartMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> skeletonDungeonStartMaxY;
    public final ForgeConfigSpec.ConfigValue<Boolean> enableSkeletonDungeons;
    public final ForgeConfigSpec.ConfigValue<Integer> skeletonDungeonSeparationDistance;

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
                " Default: 48")
            .worldRestart()
            .define("Skeleton Dungeon Average Separation Distance", 48);

        BUILDER.pop();
    }
}