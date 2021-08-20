package com.yungnickyoung.minecraft.betterdungeons.init;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon.SkeletonDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.small_dungeon.SmallDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon.ZombieDungeonStructure;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class BDModConfiguredStructures {
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_SMALL_DUNGEON = BDModStructures.SMALL_DUNGEON.configure(FeatureConfig.DEFAULT);
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_SPIDER_DUNGEON = BDModStructures.SPIDER_DUNGEON.configure(FeatureConfig.DEFAULT);
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_SKELETON_DUNGEON = BDModStructures.SKELETON_DUNGEON.configure(FeatureConfig.DEFAULT);
    public static ConfiguredStructureFeature<? ,?> CONFIGURED_ZOMBIE_DUNGEON = BDModStructures.ZOMBIE_DUNGEON.configure(FeatureConfig.DEFAULT);

    public static void init() {
        registerConfiguredStructures();
        addConfiguredStructuresToBiomes();
    }

    private static void registerConfiguredStructures() {
        MutableRegistry<ConfiguredStructureFeature<?, ?>> registry = (MutableRegistry<ConfiguredStructureFeature<?, ?>>) BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new Identifier(BetterDungeons.MOD_ID, "small_dungeon"), CONFIGURED_SMALL_DUNGEON);
        Registry.register(registry, new Identifier(BetterDungeons.MOD_ID, "spider_dungeon"), CONFIGURED_SPIDER_DUNGEON);
        Registry.register(registry, new Identifier(BetterDungeons.MOD_ID, "skeleton_dungeon"), CONFIGURED_SKELETON_DUNGEON);
        Registry.register(registry, new Identifier(BetterDungeons.MOD_ID, "zombie_dungeon"), CONFIGURED_ZOMBIE_DUNGEON);
    }

    @SuppressWarnings("deprecation")
    private static void addConfiguredStructuresToBiomes() {
        RegistryKey<ConfiguredFeature<?, ?>> vanillaDungeon = BuiltinRegistries.CONFIGURED_FEATURE.getKey(ConfiguredFeatures.MONSTER_ROOM).orElse(null);

        // Remove vanilla dungeons, if enabled
        if (BetterDungeons.CONFIG.betterDungeons.general.removeVanillaDungeons && vanillaDungeon != null) {
            BiomeModifications.create(new Identifier(BetterDungeons.MOD_ID, "vanilla_dungeon_removal"))
                .add(ModificationPhase.REMOVALS,
                    biomeSelectionContext -> biomeSelectionContext.hasFeature(vanillaDungeon),
                    modificationContext -> modificationContext.getGenerationSettings().removeFeature(vanillaDungeon));
        }

        // Add dungeons to all applicable biomes
        if (BetterDungeons.CONFIG.betterDungeons.smallDungeon.enableSmallDungeons)
            BiomeModifications.create(new Identifier(BetterDungeons.MOD_ID, "small_dungeon_addition"))
                .add(ModificationPhase.ADDITIONS,
                    biomeSelectionContext -> !SmallDungeonStructure.blacklistedBiomes.contains(biomeSelectionContext.getBiomeKey().getValue().toString()),
                    modificationContext -> modificationContext.getGenerationSettings().addBuiltInStructure(CONFIGURED_SMALL_DUNGEON));

        if (BetterDungeons.CONFIG.betterDungeons.spiderDungeon.enableSpiderDungeons)
            BiomeModifications.create(new Identifier(BetterDungeons.MOD_ID, "spider_dungeon_addition"))
                .add(ModificationPhase.ADDITIONS,
                    biomeSelectionContext -> !SpiderDungeonStructure.blacklistedBiomes.contains(biomeSelectionContext.getBiomeKey().getValue().toString()),
                    modificationContext -> modificationContext.getGenerationSettings().addBuiltInStructure(CONFIGURED_SPIDER_DUNGEON));

        if (BetterDungeons.CONFIG.betterDungeons.skeletonDungeon.enableSkeletonDungeons)
            BiomeModifications.create(new Identifier(BetterDungeons.MOD_ID, "skeleton_dungeon_addition"))
                .add(ModificationPhase.ADDITIONS,
                    biomeSelectionContext -> !SkeletonDungeonStructure.blacklistedBiomes.contains(biomeSelectionContext.getBiomeKey().getValue().toString()),
                    modificationContext -> modificationContext.getGenerationSettings().addBuiltInStructure(CONFIGURED_SKELETON_DUNGEON));

        if (BetterDungeons.CONFIG.betterDungeons.zombieDungeon.enableZombieDungeons)
            BiomeModifications.create(new Identifier(BetterDungeons.MOD_ID, "zombie_dungeon_addition"))
                .add(ModificationPhase.ADDITIONS,
                    biomeSelectionContext -> !ZombieDungeonStructure.blacklistedBiomes.contains(biomeSelectionContext.getBiomeKey().getValue().toString()),
                    modificationContext -> modificationContext.getGenerationSettings().addBuiltInStructure(CONFIGURED_ZOMBIE_DUNGEON));
    }
}
