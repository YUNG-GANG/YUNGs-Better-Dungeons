package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigSpiderDungeon {
    public final ForgeConfigSpec.ConfigValue<Integer> spiderDungeonStartMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> spiderDungeonStartMaxY;
    public final ForgeConfigSpec.ConfigValue<Boolean> enableSpiderDungeons;
    public final ForgeConfigSpec.ConfigValue<Integer> spiderDungeonSeparationDistance;

    public ConfigSpiderDungeon(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
            .comment(
                "##########################################################################################################\n" +
                "# Spider Dungeon settings.\n" +
                "##########################################################################################################")
            .push("Spider Dungeons");

        spiderDungeonStartMinY = BUILDER
            .comment(
                " The minimum y-value at which the STARTING POINT of the spider dungeon can spawn.\n" +
                " Note that this point is the top of the spider dungeon, so the various pieces of the dungeon can extend far below it.\n" +
                " Any parts that spawn above the surface (according to the heightmap) will not be placed.\n" +
                " Default: 70")
            .worldRestart()
            .define("Spider Dungeon Min Start Y", 70);

        spiderDungeonStartMaxY = BUILDER
            .comment(
                " The maximum y-value at which the STARTING POINT of the spider dungeon can spawn.\n" +
                " Note that this point is the top of the spider dungeon, so the various pieces of the dungeon can extend far below it.\n" +
                " Any parts that spawn above the surface (according to the heightmap) will not be placed.\n" +
                " Default: 71")
            .worldRestart()
            .define("Spider Dungeon Max Start Y", 71);

        enableSpiderDungeons = BUILDER
            .comment(
                " Whether or not Spider Dungeons from Better Dungeons should spawn.\n" +
                " Default: true")
            .worldRestart()
            .define("Spawn Spider Dungeons", true);

        spiderDungeonSeparationDistance = BUILDER
            .comment(
                " The average number of chunks between adjacent Spider Dungeons.\n" +
                " This controls how often Spider Dungeons spawn. Higher value = more rare.\n" +
                " Default: 48")
            .worldRestart()
            .define("Spider Dungeon Average Separation Distance", 48);

        BUILDER.pop();
    }
}