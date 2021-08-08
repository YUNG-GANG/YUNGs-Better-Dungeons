package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigSpiderDungeon {
    public final ForgeConfigSpec.ConfigValue<Integer> spiderDungeonStartMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> spiderDungeonStartMaxY;
    public final ForgeConfigSpec.ConfigValue<Boolean> enableSpiderDungeons;
    public final ForgeConfigSpec.ConfigValue<Integer> spiderDungeonSeparationDistance;
    public final ForgeConfigSpec.ConfigValue<String> whitelistedDimensions;
    public final ForgeConfigSpec.ConfigValue<String> blacklistedBiomes;
    public final ForgeConfigSpec.ConfigValue<Boolean> useQuarkCobbedstone;

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
                " Default: 44")
            .worldRestart()
            .define("Spider Dungeon Average Separation Distance", 44);

        whitelistedDimensions = BUILDER
            .comment(
                " List of dimensions that will have Spider Dungeons.\n" +
                " List must be comma-separated values enclosed in square brackets.\n" +
                " Entries must have the mod namespace included.\n" +
                " For example: \"[minecraft:overworld, minecraft:the_nether, undergarden:undergarden]\"\n" +
                " Default: \"[minecraft:overworld]\"")
            .worldRestart()
            .define("Spider Dungeon Whitelisted Dimensions", "[minecraft:overworld]");

        blacklistedBiomes = BUILDER
            .comment(
                " List of biomes that will NOT have Spider Dungeons.\n" +
                " List must be comma-separated values enclosed in square brackets.\n" +
                " Entries must have the mod namespace included.\n" +
                " For example: \"[minecraft:plains, byg:alps]\"\n" +
                " Default: \"[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river, minecraft:deep_warm_ocean]\"")
            .worldRestart()
            .define("Spider Dungeon Blacklisted Biomes", "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river, minecraft:deep_warm_ocean]");

        useQuarkCobbedstone = BUILDER
            .comment(
                " Whether or not Cobbedstone from Quark should be used when generating Spider Caves, if Quark is installed.\n" +
                " If Quark is not installed, this setting has no effect.\n" +
                " Default: true")
            .worldRestart()
            .define("Use Quark Cobbedstone", true);

        BUILDER.pop();
    }
}