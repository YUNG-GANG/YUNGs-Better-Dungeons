package com.yungnickyoung.minecraft.betterdungeons.init;

import com.google.common.collect.ImmutableMap;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import com.yungnickyoung.minecraft.betterdungeons.mixin.accessor.StructureSettingsAccessor;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon.SkeletonDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.small_dungeon.SmallDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon.ZombieDungeonStructure;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BDModStructureFeatures {
    public static final DeferredRegister<StructureFeature<?>> DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BetterDungeons.MOD_ID);
    public static final RegistryObject<StructureFeature<YungJigsawConfig>> SMALL_DUNGEON          = register("small_dungeon", SmallDungeonStructure::new);
    public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> SPIDER_DUNGEON = register("spider_dungeon", SpiderDungeonStructure::new);
    public static final RegistryObject<StructureFeature<YungJigsawConfig>> SKELETON_DUNGEON       = register("skeleton_dungeon", SkeletonDungeonStructure::new);
    public static final RegistryObject<StructureFeature<YungJigsawConfig>> ZOMBIE_DUNGEON         = register("zombie_dungeon", ZombieDungeonStructure::new);

    public static final Map<StructureFeature<?>, StructureFeatureConfiguration> STRUCTURES = new HashMap<>();

    private static <T extends FeatureConfiguration> RegistryObject<StructureFeature<T>> register(String id, Supplier<StructureFeature<T>> structureFeatureSupplier) {
        return DEFERRED_REGISTRY.register(id, structureFeatureSupplier);
    }

    public static void init() {
        // Register our deferred registry
        DEFERRED_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());

        // Register event listeners
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BDModStructureFeatures::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(BDModStructureFeatures::removeVanillaDungeons);
        MinecraftForge.EVENT_BUS.addListener(BDModStructureFeatures::setupStructureSpawns);
    }

    /**
     * Registers the Better Dungeons structures.
     */
    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Add structures
            addStructure(SMALL_DUNGEON.get(), STRUCTURES.get(SMALL_DUNGEON.get()));
            addStructure(SPIDER_DUNGEON.get(), STRUCTURES.get(SPIDER_DUNGEON.get()));
            addStructure(SKELETON_DUNGEON.get(), STRUCTURES.get(SKELETON_DUNGEON.get()));
            addStructure(ZOMBIE_DUNGEON.get(), STRUCTURES.get(ZOMBIE_DUNGEON.get()));

            // Populate structure configs
            STRUCTURES.put(SMALL_DUNGEON.get(), new StructureFeatureConfiguration(BDConfig.smallDungeons.smallDungeonDistanceVariation.get(), BDConfig.smallDungeons.smallDungeonSeparationDistance.get(), 34239823));
            STRUCTURES.put(SPIDER_DUNGEON.get(), new StructureFeatureConfiguration(BDConfig.spiderDungeons.spiderDungeonSeparationDistance.get(), BDConfig.spiderDungeons.spiderDungeonSeparationDistance.get() / 2, 596523129));
            STRUCTURES.put(SMALL_DUNGEON.get(), new StructureFeatureConfiguration(BDConfig.skeletonDungeons.skeletonDungeonSeparationDistance.get(), BDConfig.skeletonDungeons.skeletonDungeonSeparationDistance.get() / 2, 732581671));
            STRUCTURES.put(SMALL_DUNGEON.get(), new StructureFeatureConfiguration(BDConfig.zombieDungeons.zombieDungeonSeparationDistance.get(), BDConfig.zombieDungeons.zombieDungeonSeparationDistance.get() / 2, 280667671));
        });
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
            event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_STRUCTURES).removeIf(s -> s.get().equals(CavePlacements.MONSTER_ROOM) || s.get().equals(CavePlacements.MONSTER_ROOM_DEEP));
    }

    /**
     * Adds the provided structure to StructureFeature's registry map, and adds separation settings.
     */
    private static void addStructure(StructureFeature<?> structureFeature, StructureFeatureConfiguration structureFeatureConfig) {
        // Add the structure to the structures map
        StructureFeature.STRUCTURES_REGISTRY.put(structureFeature.getRegistryName().toString(), structureFeature);

        // Add our structure and its spacing to the default settings map
        StructureSettingsAccessor.setDEFAULTS(
                ImmutableMap.<StructureFeature<?>, StructureFeatureConfiguration>builder()
                        .putAll(StructureSettings.DEFAULTS)
                        .put(structureFeature, structureFeatureConfig)
                        .build());

        // Add our structure and its spacing to the noise generation settings registry.
        // This usually isn't necessary but is good to have for mod compat, i.e. Terraforged.
        BuiltinRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<StructureFeature<?>, StructureFeatureConfiguration> structureMap = settings.getValue().structureSettings().structureConfig();

            if (structureMap instanceof ImmutableMap) {
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureMap);
                tempMap.put(structureFeature, structureFeatureConfig);
                ((StructureSettingsAccessor)settings.getValue().structureSettings()).setStructureConfig(tempMap);
            } else {
                structureMap.put(structureFeature, structureFeatureConfig);
            }
        });
    }

    /**
     * Limits the spawns for each structure to the appropriate mobs.
     */
    public static void setupStructureSpawns(final StructureSpawnListGatherEvent event) {
        StructureFeature<?> structureFeature = event.getStructure();
        if (structureFeature == SPIDER_DUNGEON.get()) {
            event.addEntitySpawns(MobCategory.MONSTER, SpiderDungeonStructure.ENEMIES);
        } else if (structureFeature == SKELETON_DUNGEON.get()) {
            event.addEntitySpawns(MobCategory.MONSTER, SkeletonDungeonStructure.ENEMIES);
        } else if (structureFeature == ZOMBIE_DUNGEON.get()) {
            event.addEntitySpawns(MobCategory.MONSTER, ZombieDungeonStructure.ENEMIES);
        }
    }
}
