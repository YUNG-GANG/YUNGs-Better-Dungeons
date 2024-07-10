package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.structure.StructureType;

@AutoRegister(BetterDungeonsCommon.MOD_ID)
public class StructureTypeModule {
    @AutoRegister("spider_dungeon")
    public static StructureType<SpiderDungeonStructure> SPIDER_DUNGEON = () -> SpiderDungeonStructure.CODEC;
}
