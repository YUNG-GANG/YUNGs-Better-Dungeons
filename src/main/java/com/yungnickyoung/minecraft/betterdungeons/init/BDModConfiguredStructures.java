package com.yungnickyoung.minecraft.betterdungeons.init;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import com.yungnickyoung.minecraft.betterdungeons.mixin.accessor.ChunkGeneratorAccessor;
import com.yungnickyoung.minecraft.betterdungeons.mixin.accessor.StructureSettingsAccessor;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon.SkeletonDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.small_dungeon.SmallDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon.ZombieDungeonStructure;
import com.yungnickyoung.minecraft.yungsapi.api.YungJigsawConfig;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BDModConfiguredStructures {
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_SMALL_DUNGEON = BDModStructureFeatures.SMALL_DUNGEON.get()
            .configured(new YungJigsawConfig(new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon"), 10));

    public static ConfiguredStructureFeature<?, ?> CONFIGURED_SPIDER_DUNGEON = BDModStructureFeatures.SPIDER_DUNGEON.get()
            .configured(NoneFeatureConfiguration.INSTANCE);

    public static ConfiguredStructureFeature<?, ?> CONFIGURED_SKELETON_DUNGEON = BDModStructureFeatures.SKELETON_DUNGEON.get()
            .configured(new YungJigsawConfig(new ResourceLocation(BetterDungeons.MOD_ID, "skeleton_dungeon"), 20));

    public static ConfiguredStructureFeature<? ,?> CONFIGURED_ZOMBIE_DUNGEON = BDModStructureFeatures.ZOMBIE_DUNGEON.get()
            .configured(new YungJigsawConfig(new ResourceLocation(BetterDungeons.MOD_ID, "zombie_dungeon"), 20));

    public static void init() {
        // Register event listeners
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BDModConfiguredStructures::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(BDModConfiguredStructures::addStructuresToBiomesAndDimensions);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon"), CONFIGURED_SMALL_DUNGEON);
        Registry.register(registry, new ResourceLocation(BetterDungeons.MOD_ID, "spider_dungeon"), CONFIGURED_SPIDER_DUNGEON);
        Registry.register(registry, new ResourceLocation(BetterDungeons.MOD_ID, "skeleton_dungeon"), CONFIGURED_SKELETON_DUNGEON);
        Registry.register(registry, new ResourceLocation(BetterDungeons.MOD_ID, "zombie_dungeon"), CONFIGURED_ZOMBIE_DUNGEON);
    }


    private static void addStructuresToBiomesAndDimensions(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel serverLevel) {
            addStructureToBiomes(serverLevel);
            addStructureToDimensions(serverLevel);
        }
    }

    /**
     * Adds our structures to whitelisted biomes.
     * Currently uses a workaround method, since Forge's BiomeLoadingEvent is broken for structures.
     */
    private static void addStructureToBiomes(ServerLevel serverLevel) {
        ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
        StructureSettings worldStructureSettings = chunkGenerator.getSettings();

        // Make a copy of the structure-biome map
        ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = ImmutableMap.builder();
        ((StructureSettingsAccessor) worldStructureSettings).getConfiguredStructures().entrySet().forEach(tempStructureToMultiMap::put);

        // Create multimaps of Configured Structures to biomes
        ImmutableMultimap.Builder<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> smallDungeonBiomeMap = ImmutableMultimap.builder();
        ImmutableMultimap.Builder<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> spiderDungeonBiomeMap = ImmutableMultimap.builder();
        ImmutableMultimap.Builder<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> skeletonDungeonBiomeMap = ImmutableMultimap.builder();
        ImmutableMultimap.Builder<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> zombieDungeonBiomeMap = ImmutableMultimap.builder();

        // Add the structures to the whitelisted biomes
        for (Map.Entry<ResourceKey<Biome>, Biome> biomeEntry : serverLevel.registryAccess().ownedRegistry(Registry.BIOME_REGISTRY).get().entrySet()) {
            String biomeName = biomeEntry.getKey().location().toString();

            if (isBiomeWhitelistedForStructure(biomeName, SmallDungeonStructure.blacklistedBiomes, BDConfig.smallDungeons.enableSmallDungeons.get()))
                smallDungeonBiomeMap.put(BDModConfiguredStructures.CONFIGURED_SMALL_DUNGEON, biomeEntry.getKey());

            if (isBiomeWhitelistedForStructure(biomeName, SpiderDungeonStructure.blacklistedBiomes, BDConfig.spiderDungeons.enableSpiderDungeons.get()))
                spiderDungeonBiomeMap.put(BDModConfiguredStructures.CONFIGURED_SPIDER_DUNGEON, biomeEntry.getKey());

            if (isBiomeWhitelistedForStructure(biomeName, SkeletonDungeonStructure.blacklistedBiomes, BDConfig.skeletonDungeons.enableSkeletonDungeons.get()))
                skeletonDungeonBiomeMap.put(BDModConfiguredStructures.CONFIGURED_SKELETON_DUNGEON, biomeEntry.getKey());

            if (isBiomeWhitelistedForStructure(biomeName, ZombieDungeonStructure.blacklistedBiomes, BDConfig.zombieDungeons.enableZombieDungeons.get()))
                zombieDungeonBiomeMap.put(BDModConfiguredStructures.CONFIGURED_ZOMBIE_DUNGEON, biomeEntry.getKey());
        }

        // Add our structures and their associated configured structures + containing biomes to the settings
        tempStructureToMultiMap.put(BDModStructureFeatures.SMALL_DUNGEON.get(), smallDungeonBiomeMap.build());
        tempStructureToMultiMap.put(BDModStructureFeatures.SPIDER_DUNGEON.get(), spiderDungeonBiomeMap.build());
        tempStructureToMultiMap.put(BDModStructureFeatures.SKELETON_DUNGEON.get(), skeletonDungeonBiomeMap.build());
        tempStructureToMultiMap.put(BDModStructureFeatures.ZOMBIE_DUNGEON.get(), zombieDungeonBiomeMap.build());

        // Save our updates
        ((StructureSettingsAccessor) worldStructureSettings).setConfiguredStructures(tempStructureToMultiMap.build());

    }

    /**
     * Adds our structures to whitelisted dimensions.
     */
    private static void addStructureToDimensions(ServerLevel serverLevel) {
        // Skip Terraforged's chunk generator as they are a special case of a mod locking down their chunkgenerator.
        // Credits to TelepathicGrunt for this.
        ResourceLocation chunkGenResourceLocation = Registry.CHUNK_GENERATOR.getKey(((ChunkGeneratorAccessor) serverLevel.getChunkSource().getGenerator()).invokeCodec());
        if (chunkGenResourceLocation != null && chunkGenResourceLocation.getNamespace().equals("terraforged")) {
            return;
        }

        ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
        StructureSettings structureSettings = chunkGenerator.getSettings();

        // Need temp map as some mods use custom chunk generators with immutable maps in themselves.
        Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(structureSettings.structureConfig());

        // Don't spawn in superflats
        if (chunkGenerator instanceof FlatLevelSource && serverLevel.dimension().equals(Level.OVERWORLD)) {
            tempMap.keySet().remove(BDModStructureFeatures.SKELETON_DUNGEON.get());
            tempMap.keySet().remove(BDModStructureFeatures.SPIDER_DUNGEON.get());
            tempMap.keySet().remove(BDModStructureFeatures.ZOMBIE_DUNGEON.get());
            tempMap.keySet().remove(BDModStructureFeatures.SMALL_DUNGEON.get());
            return;
        }

        // Handler that adds or removes a structure based on whether it is whitelisted in this dimension
        TriConsumer<List<String>, StructureFeature<?>, StructureFeatureConfiguration> whitelistHandler = (List<String> whitelist, StructureFeature<?> structure, StructureFeatureConfiguration structureConfig) -> {
            if (whitelist.contains(serverLevel.dimension().location().toString())) {
                tempMap.putIfAbsent(structure, structureConfig);
            } else {
                tempMap.remove(structure);
            }
        };

        whitelistHandler.accept(SmallDungeonStructure.whitelistedDimensions, BDModStructureFeatures.SMALL_DUNGEON.get(), BDModStructureFeatures.STRUCTURES.get(BDModStructureFeatures.SMALL_DUNGEON.get()));
        whitelistHandler.accept(SpiderDungeonStructure.whitelistedDimensions, BDModStructureFeatures.SPIDER_DUNGEON.get(), BDModStructureFeatures.STRUCTURES.get(BDModStructureFeatures.SPIDER_DUNGEON.get()));
        whitelistHandler.accept(SkeletonDungeonStructure.whitelistedDimensions, BDModStructureFeatures.SKELETON_DUNGEON.get(), BDModStructureFeatures.STRUCTURES.get(BDModStructureFeatures.SKELETON_DUNGEON.get()));
        whitelistHandler.accept(ZombieDungeonStructure.whitelistedDimensions, BDModStructureFeatures.ZOMBIE_DUNGEON.get(), BDModStructureFeatures.STRUCTURES.get(BDModStructureFeatures.ZOMBIE_DUNGEON.get()));

        ((StructureSettingsAccessor) structureSettings).setStructureConfig(tempMap);
    }

    private static boolean isBiomeWhitelistedForStructure(String biomeName, List<String> blacklist, boolean isDungeonEnabled) {
        return isDungeonEnabled && !blacklist.contains(biomeName);
    }
}
