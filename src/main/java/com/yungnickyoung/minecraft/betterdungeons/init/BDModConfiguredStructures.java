package com.yungnickyoung.minecraft.betterdungeons.init;

import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class BDModConfiguredStructures {
    public static StructureFeature<?, ?> CONFIGURED_SMALL_DUNGEON = BDModStructures.SMALL_DUNGEON.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
    public static StructureFeature<?, ?> CONFIGURED_SPIDER_DUNGEON = BDModStructures.SPIDER_DUNGEON.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
    public static StructureFeature<?, ?> CONFIGURED_SKELETON_DUNGEON = BDModStructures.SKELETON_DUNGEON.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
//    public static StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> CONFIGURED_ZOMBIE_DUNGEON = ModStructures.ZOMBIE_DUNGEON.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
}
