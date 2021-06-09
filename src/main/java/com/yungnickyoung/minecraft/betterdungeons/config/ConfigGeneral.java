package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigGeneral {
    public final ForgeConfigSpec.ConfigValue<Boolean> enableHeads;

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

        BUILDER.pop();
    }
}
