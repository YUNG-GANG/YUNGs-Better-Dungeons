package com.yungnickyoung.minecraft.betterdungeons.init;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.mixin.accessor.StructureSettingsAccessor;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon.SkeletonDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.small_dungeon.SmallDungeonStructureFeature;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon.ZombieDungeonStructure;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BDModConfiguredStructures {
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_SMALL_DUNGEON = BDModStructureFeatures.SMALL_DUNGEON
            .configured(new YungJigsawConfig(new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon"), 10));

    public static ConfiguredStructureFeature<?, ?> CONFIGURED_SPIDER_DUNGEON = BDModStructureFeatures.SPIDER_DUNGEON
            .configured(NoneFeatureConfiguration.INSTANCE);

    public static ConfiguredStructureFeature<?, ?> CONFIGURED_SKELETON_DUNGEON = BDModStructureFeatures.SKELETON_DUNGEON
            .configured(new YungJigsawConfig(new ResourceLocation(BetterDungeons.MOD_ID, "skeleton_dungeon"), 20));

    public static ConfiguredStructureFeature<? ,?> CONFIGURED_ZOMBIE_DUNGEON = BDModStructureFeatures.ZOMBIE_DUNGEON
            .configured(new YungJigsawConfig(new ResourceLocation(BetterDungeons.MOD_ID, "zombie_dungeon"), 20));

    public static void init() {
        registerConfiguredStructures();
        addConfiguredStructuresToBiomes();
        enforceDimensionWhitelist();
    }

    private static void registerConfiguredStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon"), CONFIGURED_SMALL_DUNGEON);
        Registry.register(registry, new ResourceLocation(BetterDungeons.MOD_ID, "spider_dungeon"), CONFIGURED_SPIDER_DUNGEON);
        Registry.register(registry, new ResourceLocation(BetterDungeons.MOD_ID, "skeleton_dungeon"), CONFIGURED_SKELETON_DUNGEON);
        Registry.register(registry, new ResourceLocation(BetterDungeons.MOD_ID, "zombie_dungeon"), CONFIGURED_ZOMBIE_DUNGEON);
    }

    private static void addConfiguredStructuresToBiomes() {
        // Remove vanilla dungeons, if enabled
        if (BetterDungeons.CONFIG.betterDungeons.general.removeVanillaDungeons) {
            BiomeModifications.create(new ResourceLocation(BetterDungeons.MOD_ID, "vanilla_dungeon_removal"))
                    .add(ModificationPhase.REMOVALS,
                            biomeSelectionContext -> biomeSelectionContext.hasBuiltInPlacedFeature(CavePlacements.MONSTER_ROOM),
                            modificationContext -> modificationContext.getGenerationSettings().removeBuiltInFeature(CavePlacements.MONSTER_ROOM))
                    .add(ModificationPhase.REMOVALS,
                            biomeSelectionContext -> biomeSelectionContext.hasBuiltInPlacedFeature(CavePlacements.MONSTER_ROOM_DEEP),
                            modificationContext -> modificationContext.getGenerationSettings().removeBuiltInFeature(CavePlacements.MONSTER_ROOM_DEEP));
        }

        // Add dungeons to all applicable biomes
        if (BetterDungeons.CONFIG.betterDungeons.smallDungeon.enableSmallDungeons)
            BiomeModifications.create(new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon_addition"))
                .add(ModificationPhase.ADDITIONS,
                    biomeSelectionContext -> !SmallDungeonStructureFeature.blacklistedBiomes.contains(biomeSelectionContext.getBiomeKey().location().toString()),
                    modificationContext -> modificationContext.getGenerationSettings().addBuiltInStructure(CONFIGURED_SMALL_DUNGEON));

        if (BetterDungeons.CONFIG.betterDungeons.spiderDungeon.enableSpiderDungeons)
            BiomeModifications.create(new ResourceLocation(BetterDungeons.MOD_ID, "spider_dungeon_addition"))
                .add(ModificationPhase.ADDITIONS,
                    biomeSelectionContext -> !SpiderDungeonStructure.blacklistedBiomes.contains(biomeSelectionContext.getBiomeKey().location().toString()),
                    modificationContext -> modificationContext.getGenerationSettings().addBuiltInStructure(CONFIGURED_SPIDER_DUNGEON));

        if (BetterDungeons.CONFIG.betterDungeons.skeletonDungeon.enableSkeletonDungeons)
            BiomeModifications.create(new ResourceLocation(BetterDungeons.MOD_ID, "skeleton_dungeon_addition"))
                .add(ModificationPhase.ADDITIONS,
                    biomeSelectionContext -> !SkeletonDungeonStructure.blacklistedBiomes.contains(biomeSelectionContext.getBiomeKey().location().toString()),
                    modificationContext -> modificationContext.getGenerationSettings().addBuiltInStructure(CONFIGURED_SKELETON_DUNGEON));

        if (BetterDungeons.CONFIG.betterDungeons.zombieDungeon.enableZombieDungeons)
            BiomeModifications.create(new ResourceLocation(BetterDungeons.MOD_ID, "zombie_dungeon_addition"))
                .add(ModificationPhase.ADDITIONS,
                    biomeSelectionContext -> !ZombieDungeonStructure.blacklistedBiomes.contains(biomeSelectionContext.getBiomeKey().location().toString()),
                    modificationContext -> modificationContext.getGenerationSettings().addBuiltInStructure(CONFIGURED_ZOMBIE_DUNGEON));
    }

    /**
     * Restrict the dimensions our structure can spawn in.
     *
     * @author TelepathicGrunt
     */
    public static void enforceDimensionWhitelist() {
        // This is for making sure our ServerWorldEvents.LOAD event always fires after Fabric API's so our changes don't get overwritten
        ResourceLocation runAfterFabricAPIPhase = new ResourceLocation(BetterDungeons.MOD_ID, "run_after_fabric_api");
        ServerWorldEvents.LOAD.addPhaseOrdering(Event.DEFAULT_PHASE, runAfterFabricAPIPhase);

        ServerWorldEvents.LOAD.register(runAfterFabricAPIPhase, (MinecraftServer minecraftServer, ServerLevel serverLevel) -> {
            // Grab this generator's structure settings
            StructureSettings structureSettings = serverLevel.getChunkSource().getGenerator().getSettings();

            // Create temporary mutable copy of structure config
            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureSettings.structureConfig());

            // Handler that adds or removes a structure based on whether it is whitelisted in this dimension
            TriConsumer<List<String>, StructureFeature<?>, StructureFeatureConfiguration> whitelistHandler = (List<String> whitelist, StructureFeature<?> structure, StructureFeatureConfiguration structureConfig) -> {
                if (whitelist.contains(serverLevel.dimension().location().toString())) {
                    tempMap.putIfAbsent(structure, structureConfig);
                } else {
                    tempMap.remove(structure);
                }
            };

            whitelistHandler.accept(SmallDungeonStructureFeature.whitelistedDimensions, BDModStructureFeatures.SMALL_DUNGEON, BDModStructureFeatures.SMALL_DUNGEON_CONFIG);
            whitelistHandler.accept(SpiderDungeonStructure.whitelistedDimensions, BDModStructureFeatures.SPIDER_DUNGEON, BDModStructureFeatures.SPIDER_DUNGEON_CONFIG);
            whitelistHandler.accept(SkeletonDungeonStructure.whitelistedDimensions, BDModStructureFeatures.SKELETON_DUNGEON, BDModStructureFeatures.SKELETON_DUNGEON_CONFIG);
            whitelistHandler.accept(ZombieDungeonStructure.whitelistedDimensions, BDModStructureFeatures.ZOMBIE_DUNGEON, BDModStructureFeatures.ZOMBIE_DUNGEON_CONFIG);

            ((StructureSettingsAccessor) structureSettings).setStructureConfig(tempMap);
        });
    }
}
