package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BDConfigForge {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ConfigGeneralForge general;
    public static final ConfigSpiderDungeonForge spiderDungeons;
    public static final ConfigSkeletonDungeonForge skeletonDungeons;
    public static final ConfigZombieDungeonForge zombieDungeons;
    public static final ConfigSmallDungeonsForge smallDungeons;

    static {
        BUILDER.push("YUNG's Better Dungeons");

        general = new ConfigGeneralForge(BUILDER);
        spiderDungeons = new ConfigSpiderDungeonForge(BUILDER);
        skeletonDungeons = new ConfigSkeletonDungeonForge(BUILDER);
        zombieDungeons = new ConfigZombieDungeonForge(BUILDER);
        smallDungeons = new ConfigSmallDungeonsForge(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}