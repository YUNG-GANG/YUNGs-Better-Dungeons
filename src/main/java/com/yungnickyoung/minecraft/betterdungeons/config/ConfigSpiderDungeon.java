package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigSpiderDungeon {
    public final ForgeConfigSpec.ConfigValue<Integer> spiderDungeonStartMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> spiderDungeonStartMaxY;
    public final ForgeConfigSpec.ConfigValue<Boolean> useQuarkCobbedstone;

    public ConfigSpiderDungeon(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
                .comment(
                        """
                                ##########################################################################################################
                                # Spider Dungeon settings.
                                ##########################################################################################################""")
                .push("Spider Dungeons");

        spiderDungeonStartMinY = BUILDER
                .comment(
                        """
                                The minimum y-value at which the STARTING POINT of the spider dungeon can spawn.
                                Note that this point is the top of the spider dungeon, so the various pieces of the dungeon can extend far below it.
                                Any parts that spawn above the surface (according to the heightmap) will not be placed.
                                Default: 70""".indent(1))
                .worldRestart()
                .define("Spider Dungeon Min Start Y", 70);

        spiderDungeonStartMaxY = BUILDER
                .comment(
                        """
                                The maximum y-value at which the STARTING POINT of the spider dungeon can spawn.
                                Note that this point is the top of the spider dungeon, so the various pieces of the dungeon can extend far below it.
                                Any parts that spawn above the surface (according to the heightmap) will not be placed.
                                Default: 71""".indent(1))
                .worldRestart()
                .define("Spider Dungeon Max Start Y", 71);

        useQuarkCobbedstone = BUILDER
                .comment(
                        """
                                Whether or not Cobbedstone from Quark should be used when generating Spider Caves, if Quark is installed.
                                If Quark is not installed, this setting has no effect.
                                Default: true""".indent(1))
                .worldRestart()
                .define("Use Quark Cobbedstone", true);

        BUILDER.pop();
    }
}