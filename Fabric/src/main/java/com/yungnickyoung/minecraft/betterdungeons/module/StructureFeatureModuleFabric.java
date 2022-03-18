package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon.SkeletonDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.small_dungeon.SmallDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon.ZombieDungeonStructure;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class StructureFeatureModuleFabric {
    public static void init() {
        // Register structures
        StructureFeatureModule.SMALL_DUNGEON    = register("small_dungeon", new SmallDungeonStructure());
        StructureFeatureModule.SPIDER_DUNGEON   = register("spider_dungeon", new SpiderDungeonStructure());
        StructureFeatureModule.SKELETON_DUNGEON = register("skeleton_dungeon", new SkeletonDungeonStructure());
        StructureFeatureModule.ZOMBIE_DUNGEON   = register("zombie_dungeon", new ZombieDungeonStructure());

        // Remove vanilla dungeons, if enabled
        if (BetterDungeonsCommon.CONFIG.general.removeVanillaDungeons) {
            BiomeModifications.create(new ResourceLocation(BetterDungeonsCommon.MOD_ID, "vanilla_dungeon_removal"))
                    .add(ModificationPhase.REMOVALS,
                            biomeSelectionContext -> biomeSelectionContext.hasBuiltInPlacedFeature(CavePlacements.MONSTER_ROOM.value()),
                            modificationContext -> modificationContext.getGenerationSettings().removeBuiltInFeature(CavePlacements.MONSTER_ROOM.value()))
                    .add(ModificationPhase.REMOVALS,
                            biomeSelectionContext -> biomeSelectionContext.hasBuiltInPlacedFeature(CavePlacements.MONSTER_ROOM_DEEP.value()),
                            modificationContext -> modificationContext.getGenerationSettings().removeBuiltInFeature(CavePlacements.MONSTER_ROOM_DEEP.value()));
        }
    }

    private static <FC extends FeatureConfiguration> StructureFeature<FC> register(String name, StructureFeature<FC> structureFeature) {
        return Registry.register(Registry.STRUCTURE_FEATURE, new ResourceLocation(BetterDungeonsCommon.MOD_ID, name), structureFeature);
    }
}
