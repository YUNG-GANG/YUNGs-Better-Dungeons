package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.services.Services;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class StructureTypeModule {
    public static StructureType<SpiderDungeonStructure> SPIDER_DUNGEON = () -> SpiderDungeonStructure.CODEC;

    public static void init() {
        Services.REGISTRY.registerStructureType(new ResourceLocation(BetterDungeonsCommon.MOD_ID, "spider_dungeon"), SPIDER_DUNGEON);
    }
}
