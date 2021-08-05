package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigZombieDungeon {
    public final ForgeConfigSpec.ConfigValue<Integer> zombieDungeonStartMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> zombieDungeonStartMaxY;
    public final ForgeConfigSpec.ConfigValue<Boolean> enableZombieDungeons;
    public final ForgeConfigSpec.ConfigValue<Integer> zombieDungeonSeparationDistance;
    public final ForgeConfigSpec.ConfigValue<Integer> zombieDungeonMaxSurfaceStaircaseLength;
    public final ForgeConfigSpec.ConfigValue<String> whitelistedDimensions;
    public final ForgeConfigSpec.ConfigValue<String> blacklistedBiomes;

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

        zombieDungeonMaxSurfaceStaircaseLength = BUILDER
            .comment(
                " The longest distance that can be checked when attempting to generate a surface entrance staircase.\n" +
                " Making this too large may cause problems.\n" +
                " Default: 20")
            .worldRestart()
            .define("Zombie Dungeon Surface Entrance Staircase Max Length", 20);

        whitelistedDimensions = BUILDER
            .comment(
                " List of dimensions that will have Zombie Dungeons.\n" +
                " List must be comma-separated values enclosed in square brackets.\n" +
                " Entries must have the mod namespace included.\n" +
                " For example: \"[minecraft:overworld, minecraft:the_nether, undergarden:undergarden]\"\n" +
                " Default: \"[minecraft:overworld]\"")
            .worldRestart()
            .define("Whitelisted Dimensions", "[minecraft:overworld]");

        blacklistedBiomes = BUILDER
            .comment(
                " List of biomes that will NOT have Zombie Dungeons.\n" +
                " List must be comma-separated values enclosed in square brackets.\n" +
                " Entries must have the mod namespace included.\n" +
                " For example: \"[minecraft:plains, byg:alps]\"\n" +
                " Default: \"[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river]\"")
            .worldRestart()
            .define("Blacklisted Biomes", "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river]");

        BUILDER.pop();
    }
}