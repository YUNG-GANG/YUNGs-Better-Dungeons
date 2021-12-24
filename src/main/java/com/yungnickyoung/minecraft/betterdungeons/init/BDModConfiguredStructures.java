package com.yungnickyoung.minecraft.betterdungeons.init;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BDModConfiguredStructures {
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_SMALL_DUNGEON = BDModStructureFeatures.SMALL_DUNGEON.get()
            .configured(new YungJigsawConfig(new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon"), 10));

    public static ConfiguredStructureFeature<?, ?> CONFIGURED_SPIDER_DUNGEON = BDModStructureFeatures.SPIDER_DUNGEON.get()
            .configured(NoneFeatureConfiguration.INSTANCE);

    public static ConfiguredStructureFeature<?, ?> CONFIGURED_SKELETON_DUNGEON = BDModStructureFeatures.SKELETON_DUNGEON.get()
            .configured(new YungJigsawConfig(new ResourceLocation(BetterDungeons.MOD_ID, "skeleton_dungeon"), 20));

    public static ConfiguredStructureFeature<? ,?> CONFIGURED_ZOMBIE_DUNGEON = BDModStructureFeatures.ZOMBIE_DUNGEON.get()
            .configured(new YungJigsawConfig(new ResourceLocation(BetterDungeons.MOD_ID, "zombie_dungeon"), 20));
}
