package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class TagModuleFabric {
    public static void init() {
        TagModule.HAS_SMALL_DUNGEON = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "has_small_mineshaft"));
        TagModule.HAS_SPIDER_DUNGEON = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "has_spider_mineshaft"));
        TagModule.HAS_SKELETON_DUNGEON = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "has_skeleton_mineshaft"));
        TagModule.HAS_ZOMBIE_DUNGEON = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "has_zombie_mineshaft"));
    }
}
