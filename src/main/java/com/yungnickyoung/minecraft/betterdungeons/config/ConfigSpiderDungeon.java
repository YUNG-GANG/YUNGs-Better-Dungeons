package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigSpiderDungeon {
    public final ForgeConfigSpec.ConfigValue<Integer> spiderDungeonStartMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> spiderDungeonStartMaxY;
    public final ForgeConfigSpec.ConfigValue<Integer> spiderDungeonMaxY;
    public final ForgeConfigSpec.ConfigValue<Integer> spiderDungeonSize;
    public final ForgeConfigSpec.ConfigValue<Double> spiderDungeonCobwebReplacementChanceNormal;
    public final ForgeConfigSpec.ConfigValue<Double> spiderDungeonCobwebReplacementChanceSpawner;

    public ConfigSpiderDungeon(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
            .comment(
                "##########################################################################################################\n" +
                "# General settings.\n" +
                "##########################################################################################################")
            .push("General");

        spiderDungeonStartMinY = BUILDER
            .comment(
                " The minimum y-value at which the STARTING ROOM of the spider dungeon can spawn.\n" +
                " Note that the various pieces of the dungeon can extend above or below this value.\n" +
                " Default: 30")
            .worldRestart()
            .define("Spider Dungeon Min Start Y", 30);

        spiderDungeonStartMaxY = BUILDER
            .comment(
                " The maximum y-value at which the STARTING ROOM of the spider dungeon can spawn.\n" +
                " Note that the various pieces of the dungeon can extend above or below this value.\n" +
                " Default: 31")
            .worldRestart()
            .define("Spider Dungeon Max Start Y", 31);

        spiderDungeonMaxY = BUILDER
            .comment(
                " The maximum y-value at which ANY piece of the spider dungeon can spawn.\n" +
                " If any piece attempts to spawn such that any part of it is above this y-value,\n" +
                " it will not spawn.\n" +
                " In other words, this is a hard cap above which no part of the dungeon can generate.\n" +
                " Default: 60")
            .worldRestart()
            .define("Spider Dungeon Max Y", 60);

        spiderDungeonSize = BUILDER
            .comment(
                " The max number of \"pieces\" the spider dungeon will generate from the center.\n" +
                " This number controls the general size of dungeons. Bigger number = bigger dungeons.\n" +
                " It is HIGHLY recommended to keep this an even number!\n" +
                " Default: 16")
            .worldRestart()
            .define("Spider Dungeon Size", 16);

        spiderDungeonCobwebReplacementChanceNormal = BUILDER
            .comment(
                " The rate at which cobwebs will spawn in various parts of the spider dungeon.\n" +
                " Default: 0.1")
            .worldRestart()
            .define("Spider Dungeon Cobweb Spawn Rate (NORMAL)", 0.1);

        spiderDungeonCobwebReplacementChanceSpawner = BUILDER
            .comment(
                " The rate at which cobwebs will spawn around spider spawners in spider dungeons.\n" +
                " Default: 0.3")
            .worldRestart()
            .define("Spider Dungeon Cobweb Spawn Rate (SPAWNER)", 0.3);

        BUILDER.pop();
    }
}