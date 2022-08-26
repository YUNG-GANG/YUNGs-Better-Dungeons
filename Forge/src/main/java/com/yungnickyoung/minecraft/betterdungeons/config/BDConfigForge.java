package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BDConfigForge {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ConfigGeneralForge general;
    public static final ConfigZombieDungeonForge zombieDungeons;
    public static final ConfigSmallDungeonsForge smallDungeons;
    public static final ConfigSmallNetherDungeonsForge smallNetherDungeons;

    static {
        BUILDER.push("YUNG's Better Dungeons");

        general = new ConfigGeneralForge(BUILDER);
        zombieDungeons = new ConfigZombieDungeonForge(BUILDER);
        smallDungeons = new ConfigSmallDungeonsForge(BUILDER);
        smallNetherDungeons = new ConfigSmallNetherDungeonsForge(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}