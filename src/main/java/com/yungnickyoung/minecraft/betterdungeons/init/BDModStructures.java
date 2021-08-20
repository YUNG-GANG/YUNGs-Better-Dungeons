package com.yungnickyoung.minecraft.betterdungeons.init;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon.SkeletonDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.small_dungeon.SmallDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon.ZombieDungeonStructure;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class BDModStructures {
    public static StructureFeature<DefaultFeatureConfig> SMALL_DUNGEON = new SmallDungeonStructure();
    public static StructureFeature<DefaultFeatureConfig> SPIDER_DUNGEON = new SpiderDungeonStructure();
    public static StructureFeature<DefaultFeatureConfig> SKELETON_DUNGEON = new SkeletonDungeonStructure();
    public static StructureFeature<DefaultFeatureConfig> ZOMBIE_DUNGEON = new ZombieDungeonStructure();

    public static void init() {
        registerStructure(SMALL_DUNGEON, "small_dungeon", new StructureConfig(BetterDungeons.CONFIG.betterDungeons.smallDungeon.smallDungeonSeparationDistance, BetterDungeons.CONFIG.betterDungeons.smallDungeon.smallDungeonDistanceVariation, 34239823));
        registerStructure(SPIDER_DUNGEON, "spider_dungeon", new StructureConfig(BetterDungeons.CONFIG.betterDungeons.spiderDungeon.spiderDungeonSeparationDistance, BetterDungeons.CONFIG.betterDungeons.spiderDungeon.spiderDungeonSeparationDistance / 2, 596523129));
        registerStructure(SKELETON_DUNGEON, "skeleton_dungeon", new StructureConfig(BetterDungeons.CONFIG.betterDungeons.skeletonDungeon.skeletonDungeonSeparationDistance, BetterDungeons.CONFIG.betterDungeons.skeletonDungeon.skeletonDungeonSeparationDistance / 2, 732581671));
        registerStructure(ZOMBIE_DUNGEON, "zombie_dungeon", new StructureConfig(BetterDungeons.CONFIG.betterDungeons.zombieDungeon.zombieDungeonSeparationDistance, BetterDungeons.CONFIG.betterDungeons.zombieDungeon.zombieDungeonSeparationDistance / 2, 280667671));
    }

    private static void registerStructure(StructureFeature<DefaultFeatureConfig> structure, String name, StructureConfig structureConfig) {
        FabricStructureBuilder
            .create(new Identifier(BetterDungeons.MOD_ID, name), structure)
            .step(GenerationStep.Feature.UNDERGROUND_STRUCTURES)
            .defaultConfig(structureConfig)
            .superflatFeature(structure.configure(FeatureConfig.DEFAULT))
            .register();
    }
}
