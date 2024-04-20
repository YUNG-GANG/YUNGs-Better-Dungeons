package com.yungnickyoung.minecraft.betterdungeons.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class BDConfigNeoForge {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ConfigGeneralNeoForge general;
    public static final ConfigZombieDungeonNeoForge zombieDungeons;
    public static final ConfigSmallDungeonsNeoForge smallDungeons;
    public static final ConfigSmallNetherDungeonsNeoForge smallNetherDungeons;

    static {
        BUILDER.push("YUNG's Better Dungeons");

        general = new ConfigGeneralNeoForge(BUILDER);
        zombieDungeons = new ConfigZombieDungeonNeoForge(BUILDER);
        smallDungeons = new ConfigSmallDungeonsNeoForge(BUILDER);
        smallNetherDungeons = new ConfigSmallNetherDungeonsNeoForge(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}