package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigZombieDungeonForge {
    public final ForgeConfigSpec.ConfigValue<Integer> zombieDungeonMaxSurfaceStaircaseLength;

    public ConfigZombieDungeonForge(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
                .comment(
                        """
                                ##########################################################################################################
                                # Zombie Dungeon settings.
                                ##########################################################################################################""")
                .push("Zombie Dungeons");

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