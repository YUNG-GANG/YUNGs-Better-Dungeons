package com.yungnickyoung.minecraft.betterdungeons.init;

import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;

public class ModConfiguredStructures {
    public static StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> CONFIGURED_SMALL_DUNGEON = ModStructures.SMALL_DUNGEON.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
    public static StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> CONFIGURED_SPIDER_DUNGEON = ModStructures.SPIDER_DUNGEON.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
//    public static StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> CONFIGURED_SKELETON_DUNGEON = ModStructures.SKELETON_DUNGEON.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
//    public static StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> CONFIGURED_ZOMBIE_DUNGEON = ModStructures.ZOMBIE_DUNGEON.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
}
