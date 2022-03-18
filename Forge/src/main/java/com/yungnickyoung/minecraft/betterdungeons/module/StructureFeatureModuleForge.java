package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon.SkeletonDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.small_dungeon.SmallDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon.ZombieDungeonStructure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

public class StructureFeatureModuleForge {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(StructureFeature.class, StructureFeatureModuleForge::registerStructures);
        MinecraftForge.EVENT_BUS.addListener(StructureFeatureModuleForge::removeVanillaDungeons);
    }

    private static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
        IForgeRegistry<StructureFeature<?>> registry = event.getRegistry();
        StructureFeatureModule.SMALL_DUNGEON    = register(registry, "small_dungeon", new SmallDungeonStructure());
        StructureFeatureModule.SPIDER_DUNGEON   = register(registry, "spider_dungeon", new SpiderDungeonStructure());
        StructureFeatureModule.SKELETON_DUNGEON = register(registry, "skeleton_dungeon", new SkeletonDungeonStructure());
        StructureFeatureModule.ZOMBIE_DUNGEON   = register(registry, "zombie_dungeon", new ZombieDungeonStructure());
    }

    private static <FC extends FeatureConfiguration> StructureFeature<FC> register(IForgeRegistry<StructureFeature<?>> registry, String name, StructureFeature<FC> structureFeature) {
        structureFeature.setRegistryName(new ResourceLocation(BetterDungeonsCommon.MOD_ID, name));
        registry.register(structureFeature);
        return structureFeature;
    }

    /**
     * Removes vanilla dungeons, if disabled.
     */
    private static void removeVanillaDungeons(BiomeLoadingEvent event) {
        // Ensure non-null biome name.
        // This should never happen, but we check to prevent a NPE just in case.
        if (event.getName() == null) {
            BetterDungeonsCommon.LOGGER.error("Missing biome name! This is a critical error and should not occur.");
            BetterDungeonsCommon.LOGGER.error("Try running the game with the Blame mod for a more detailed breakdown.");
            BetterDungeonsCommon.LOGGER.error("Please report this issue!");
            return;
        }

        // Remove vanilla dungeons, if enabled
        if (BetterDungeonsCommon.CONFIG.general.removeVanillaDungeons)
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_STRUCTURES).removeIf(s -> s.value().feature().value().feature() == Feature.MONSTER_ROOM);
    }
}
