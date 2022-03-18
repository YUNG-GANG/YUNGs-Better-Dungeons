package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigSkeletonDungeonForge {
    public final ForgeConfigSpec.ConfigValue<Integer> skeletonDungeonStartMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> skeletonDungeonStartMaxY;

    public ConfigSkeletonDungeonForge(final ForgeConfigSpec.Builder BUILDER) {
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

        BUILDER.pop();
    }
}