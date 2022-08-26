package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigSmallNetherDungeonsForge {
    public final ForgeConfigSpec.ConfigValue<Boolean> enabled;
    public final ForgeConfigSpec.ConfigValue<Integer> bannerMaxCount;

    public ConfigSmallNetherDungeonsForge(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
                .comment(
                        """
                                ##########################################################################################################
                                # Small Nether Dungeon settings.
                                # These are disabled by default.
                                ##########################################################################################################""")
                .push("Small Nether Dungeons");

        enabled = BUILDER
                .comment(
                        " Whether or not small Nether dungeons should spawn.\n" +
                        " Default: false")
                .worldRestart()
                .define("Enable Small Nether Dungeons", false);

        bannerMaxCount = BUILDER
                .comment(
                        " The maximum number of banners that can spawn in a single small Nether dungeon.\n" +
                        " Default: 2")
                .worldRestart()
                .defineInRange("Small Nether Dungeon Max Banner Count", 2, 0, 8);

        BUILDER.pop();
    }
}
