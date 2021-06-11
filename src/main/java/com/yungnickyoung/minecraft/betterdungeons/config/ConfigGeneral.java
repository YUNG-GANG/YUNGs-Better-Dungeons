package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigGeneral {
    public final ForgeConfigSpec.ConfigValue<Boolean> enableHeads;
    public final ForgeConfigSpec.ConfigValue<Boolean> removeVanillaDungeons;

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

        BUILDER.pop();
    }
}
