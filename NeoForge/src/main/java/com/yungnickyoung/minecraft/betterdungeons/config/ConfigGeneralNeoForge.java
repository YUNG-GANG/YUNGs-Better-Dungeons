package com.yungnickyoung.minecraft.betterdungeons.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigGeneralNeoForge {
    public final ModConfigSpec.ConfigValue<Boolean> enableHeads;
    public final ModConfigSpec.ConfigValue<Boolean> enableNetherBlocks;

    public ConfigGeneralNeoForge(final ModConfigSpec.Builder BUILDER) {
        BUILDER
                .comment(
                        """
                                ##########################################################################################################
                                # General settings.
                                ##########################################################################################################""")
                .push("General");

        enableHeads = BUILDER
                .comment(
                        """
                                Whether or not dungeons should be allowed to place skeleton skulls and other mob heads.
                                This option may be useful for some modpack creators.
                                Default: true""".indent(1))
                .worldRestart()
                .define("Enable Skulls & Heads", true);

        enableNetherBlocks = BUILDER
                .comment(
                        """
                                Some dungeons can rarely spawn Nether-related blocks such as soul sand, soul campfires, and soul lanterns.
                                Note that the blocks will be purely decorative - nothing progression-breaking like Ancient Debris.
                                Set this to false to prevent any Nether-related blocks from spawning in dungeons.
                                This option may be useful for some modpack creators.
                                Default: true""".indent(1))
                .worldRestart()
                .define("Enable Nether Blocks in Dungeons", true);

        BUILDER.pop();
    }
}