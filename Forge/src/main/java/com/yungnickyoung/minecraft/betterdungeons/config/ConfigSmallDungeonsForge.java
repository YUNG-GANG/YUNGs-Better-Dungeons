package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigSmallDungeonsForge {
    public final ForgeConfigSpec.ConfigValue<Integer> smallDungeonMinY;
    public final ForgeConfigSpec.ConfigValue<Integer> smallDungeonMaxY;
    public final ForgeConfigSpec.ConfigValue<Integer> bannerMaxCount;
    public final ForgeConfigSpec.ConfigValue<Integer> chestMinCount;
    public final ForgeConfigSpec.ConfigValue<Integer> chestMaxCount;
    public final ForgeConfigSpec.ConfigValue<Boolean> enableOreProps;

    public ConfigSmallDungeonsForge(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
                .comment(
                        """
                                ##########################################################################################################
                                # Small Dungeon settings.
                                ##########################################################################################################""")
                .push("Small Dungeons");

        smallDungeonMinY = BUILDER
                .comment(
                        " The minimum y-value at which small dungeons can spawn.\n" +
                        " Default: -50")
                .worldRestart()
                .define("Small Dungeon Min Y", -50);

        smallDungeonMaxY = BUILDER
                .comment(
                        " The maximum y-value at which small dungeons can spawn.\n" +
                        " Default: 50")
                .worldRestart()
                .define("Small Dungeon Max Start Y", 50);

        bannerMaxCount = BUILDER
                .comment(
                        " The maximum number of banners that can spawn in a single small dungeon.\n" +
                        " Default: 2")
                .worldRestart()
                .defineInRange("Small Dungeon Max Banner Count", 2, 0, 8);

        chestMinCount = BUILDER
                .comment(
                        " The minimum number of chests that are guaranteed to spawn in a single small dungeon.\n" +
                        " Default: 1")
                .worldRestart()
                .define("Small Dungeon Min Chest Count", 1);

        chestMaxCount = BUILDER
                .comment(
                        " The maximum number of chests that can spawn in a single small dungeon.\n" +
                        " Default: 2")
                .worldRestart()
                .define("Small Dungeon Max Chest Count", 2);

        enableOreProps = BUILDER
                .comment(
                        """
                                Whether or not Small Dungeons can rarely place ore blocks in the corners of the dungeon.
                                If this is set to false, any ore blocks that spawn as part of a corner prop will instead be replaced with air.
                                Default: true""".indent(1))
                .worldRestart()
                .define("Allow Ore Blocks in Corners", true);

        BUILDER.pop();
    }
}
