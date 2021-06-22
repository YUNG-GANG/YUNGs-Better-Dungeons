package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigZombieDungeon {
    public final ForgeConfigSpec.ConfigValue<Integer> zombieDungeonStartMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> zombieDungeonStartMaxY;
    public final ForgeConfigSpec.ConfigValue<Boolean> enableZombieDungeons;
    public final ForgeConfigSpec.ConfigValue<Integer> zombieDungeonSeparationDistance;

    public ConfigZombieDungeon(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
            .comment(
                "##########################################################################################################\n" +
                "# Zombie Dungeon settings.\n" +
                "##########################################################################################################")
            .push("Zombie Dungeons");

        zombieDungeonStartMinY = BUILDER
            .comment(
                " The minimum y-value at which the STARTING POINT of the zombie dungeon can spawn.\n" +
                " Note that this point is the bottom of the zombie dungeon, so the various pieces of the dungeon can extend below it.\n" +
                " Default: 50")
            .worldRestart()
            .define("Zombie Dungeon Min Start Y", 50);

        zombieDungeonStartMaxY = BUILDER
            .comment(
                " The maximum y-value at which the STARTING POINT of the zombie dungeon can spawn.\n" +
                " Note that this point is the bottom of the zombie dungeon, so the various pieces of the dungeon can extend below it.\n" +
                " Default: 51")
            .worldRestart()
            .define("Zombie Dungeon Max Start Y", 51);

        enableZombieDungeons = BUILDER
            .comment(
                " Whether or not Zombie Dungeons from Better Dungeons should spawn.\n" +
                " Default: true")
            .worldRestart()
            .define("Spawn Zombie Dungeons", true);

        zombieDungeonSeparationDistance = BUILDER
            .comment(
                " The average number of chunks between adjacent Zombie Dungeons.\n" +
                " This controls how often Zombie Dungeons spawn. Higher value = more rare.\n" +
                " Default: 48")
            .worldRestart()
            .define("Zombie Dungeon Average Separation Distance", 48);

        BUILDER.pop();
    }
}