package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon.SkeletonDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.small_dungeon.SmallDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon.ZombieDungeonStructure;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BDModStructureFeatures {
    public static final DeferredRegister<StructureFeature<?>> DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BetterDungeons.MOD_ID);
    public static final RegistryObject<StructureFeature<YungJigsawConfig>> SMALL_DUNGEON          = register("small_dungeon", SmallDungeonStructure::new);
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> SPIDER_DUNGEON = register("spider_dungeon", SpiderDungeonStructure::new);
    public static final RegistryObject<StructureFeature<YungJigsawConfig>> SKELETON_DUNGEON       = register("skeleton_dungeon", SkeletonDungeonStructure::new);
    public static final RegistryObject<StructureFeature<YungJigsawConfig>> ZOMBIE_DUNGEON         = register("zombie_dungeon", ZombieDungeonStructure::new);

    private static <T extends FeatureConfiguration> RegistryObject<StructureFeature<T>> register(String id, Supplier<StructureFeature<T>> structureFeatureSupplier) {
        return DEFERRED_REGISTRY.register(id, structureFeatureSupplier);
    }

    public static void init() {
        // Register our deferred registry
        DEFERRED_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());

        // Register event listeners
        MinecraftForge.EVENT_BUS.addListener(BDModStructureFeatures::removeVanillaDungeons);
    }

    /**
     * Removes vanilla dungeons, if disabled.
     */
    private static void removeVanillaDungeons(BiomeLoadingEvent event) {
        // Ensure non-null biome name.
        // This should never happen, but we check to prevent a NPE just in case.
        if (event.getName() == null) {
            BetterDungeons.LOGGER.error("Missing biome name! This is a critical error and should not occur.");
            BetterDungeons.LOGGER.error("Try running the game with the Blame mod for a more detailed breakdown.");
            BetterDungeons.LOGGER.error("Please report this issue!");
            return;
        }

        // Remove vanilla dungeons, if enabled
        if (BDConfig.general.removeVanillaDungeons.get())
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_STRUCTURES).removeIf(s -> s.value().feature().value().feature() == Feature.MONSTER_ROOM);
    }
}
