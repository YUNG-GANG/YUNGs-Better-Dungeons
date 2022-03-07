package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigZombieDungeon {
    public final ForgeConfigSpec.ConfigValue<Integer> zombieDungeonStartMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> zombieDungeonStartMaxY;
    public final ForgeConfigSpec.ConfigValue<Integer> zombieDungeonMaxSurfaceStaircaseLength;

    public ConfigZombieDungeon(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
                .comment(
                        """
                                ##########################################################################################################
                                # Zombie Dungeon settings.
                                ##########################################################################################################""")
                .push("Zombie Dungeons");

        zombieDungeonStartMinY = BUILDER
                .comment(
                        """
                                The minimum y-value at which the STARTING POINT of the zombie dungeon can spawn.
                                Note that this point is the bottom of the zombie dungeon, so the various pieces of the dungeon can extend below it.
                                Default: 50""".indent(1))
                .worldRestart()
                .define("Zombie Dungeon Min Start Y", 50);

        zombieDungeonStartMaxY = BUILDER
                .comment(
                        """
                                The maximum y-value at which the STARTING POINT of the zombie dungeon can spawn.
                                Note that this point is the bottom of the zombie dungeon, so the various pieces of the dungeon can extend below it.
                                Default: 51""".indent(1))
                .worldRestart()
                .define("Zombie Dungeon Max Start Y", 51);

        zombieDungeonMaxSurfaceStaircaseLength = BUILDER
                .comment(
                        """
                                The longest distance that can be checked when attempting to generate a surface entrance staircase.
                                Making this too large may cause problems.
                                Default: 20""".indent(1))
                .worldRestart()
                .define("Zombie Dungeon Surface Entrance Staircase Max Length", 20);

        BUILDER.pop();
    }
}