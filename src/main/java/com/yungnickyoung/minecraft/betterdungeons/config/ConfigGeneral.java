package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigGeneral {
    public final ForgeConfigSpec.ConfigValue<Boolean> enableHeads;
    public final ForgeConfigSpec.ConfigValue<Boolean> removeVanillaDungeons;
    public final ForgeConfigSpec.ConfigValue<Boolean> enableNetherBlocks;

    public ConfigGeneral(final ForgeConfigSpec.Builder BUILDER) {
        BUILDER
            .comment(
                "##########################################################################################################\n" +
                "# General settings.\n" +
                "##########################################################################################################")
            .push("General");

        enableHeads = BUILDER
            .comment(
                " Whether or not dungeons should be allowed to place skeleton skulls and other mob heads.\n" +
                " This option may be useful for some modpack creators.\n" +
                " Default: true")
            .worldRestart()
            .define("Enable Skulls & Heads", true);

        removeVanillaDungeons = BUILDER
            .comment(
                " Whether or not vanilla dungeons should be prevented from spawning in the world.\n" +
                " It is recommended to disable these, since the Small Dungeons are very similar in design.\n" +
                " Default: true")
            .worldRestart()
            .define("Remove Vanilla Dungeons", true);

        enableNetherBlocks = BUILDER
            .comment(
                " Some dungeons can rarely spawn Nether-related blocks such as soul sand, soul campfires, and soul lanterns.\n" +
                " Note that the blocks will be purely decorative - nothing progression-breaking like Ancient Debris.\n" +
                " Set this to false to prevent any Nether-related blocks from spawning in dungeons.\n" +
                " This option may be useful for some modpack creators.\n" +
                " Default: true")
            .worldRestart()
            .define("Enable Nether Blocks in Dungeons", true);

        BUILDER.pop();
    }
}
