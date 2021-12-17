package com.yungnickyoung.minecraft.betterdungeons.init;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon.SkeletonDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.small_dungeon.SmallDungeonStructureFeature;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon.ZombieDungeonStructure;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;

public class BDModStructureFeatures {
    public static StructureFeature<YungJigsawConfig> SMALL_DUNGEON = new SmallDungeonStructureFeature();
    public static StructureFeature<NoneFeatureConfiguration> SPIDER_DUNGEON = new SpiderDungeonStructure();
    public static StructureFeature<YungJigsawConfig> SKELETON_DUNGEON = new SkeletonDungeonStructure();
    public static StructureFeature<YungJigsawConfig> ZOMBIE_DUNGEON = new ZombieDungeonStructure();

    public static StructureFeatureConfiguration SMALL_DUNGEON_CONFIG;
    public static StructureFeatureConfiguration SPIDER_DUNGEON_CONFIG;
    public static StructureFeatureConfiguration SKELETON_DUNGEON_CONFIG;
    public static StructureFeatureConfiguration ZOMBIE_DUNGEON_CONFIG;


    public static void init() {
        SMALL_DUNGEON_CONFIG = new StructureFeatureConfiguration(BetterDungeons.CONFIG.betterDungeons.smallDungeon.smallDungeonSeparationDistance, BetterDungeons.CONFIG.betterDungeons.smallDungeon.smallDungeonDistanceVariation, 34239823);
        SPIDER_DUNGEON_CONFIG = new StructureFeatureConfiguration(BetterDungeons.CONFIG.betterDungeons.spiderDungeon.spiderDungeonSeparationDistance, BetterDungeons.CONFIG.betterDungeons.spiderDungeon.spiderDungeonSeparationDistance / 2, 596523129);
        SKELETON_DUNGEON_CONFIG = new StructureFeatureConfiguration(BetterDungeons.CONFIG.betterDungeons.skeletonDungeon.skeletonDungeonSeparationDistance, BetterDungeons.CONFIG.betterDungeons.skeletonDungeon.skeletonDungeonSeparationDistance / 2, 732581671);
        ZOMBIE_DUNGEON_CONFIG = new StructureFeatureConfiguration(BetterDungeons.CONFIG.betterDungeons.zombieDungeon.zombieDungeonSeparationDistance, BetterDungeons.CONFIG.betterDungeons.zombieDungeon.zombieDungeonSeparationDistance / 2, 280667671);
        registerStructure(SMALL_DUNGEON, "small_dungeon", SMALL_DUNGEON_CONFIG);
        registerStructure(SPIDER_DUNGEON, "spider_dungeon", SPIDER_DUNGEON_CONFIG);
        registerStructure(SKELETON_DUNGEON, "skeleton_dungeon", SKELETON_DUNGEON_CONFIG);
        registerStructure(ZOMBIE_DUNGEON, "zombie_dungeon", ZOMBIE_DUNGEON_CONFIG);
    }

    private static void registerStructure(StructureFeature<?> structure, String name, StructureFeatureConfiguration structureConfig) {
        FabricStructureBuilder
            .create(new ResourceLocation(BetterDungeons.MOD_ID, name), structure)
            .step(GenerationStep.Decoration.UNDERGROUND_STRUCTURES)
            .defaultConfig(structureConfig)
            .register();
    }
}
