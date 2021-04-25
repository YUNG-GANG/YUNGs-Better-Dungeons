package com.yungnickyoung.minecraft.betterdungeons.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BDConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ConfigSpiderDungeon spiderDungeons;

    static {
        BUILDER.push("YUNG's Better Dungeons");

        spiderDungeons = new ConfigSpiderDungeon(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
