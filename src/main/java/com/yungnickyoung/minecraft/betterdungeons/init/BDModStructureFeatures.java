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
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.HashMap;
import java.util.List;
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
        MinecraftForge.EVENT_BUS.addListener(BDModStructureFeatures::addStructuresToBiomesAndDimensions);
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

            // Register configured structure features
            Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
            Registry.register(registry, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon"), BDModConfiguredStructures.CONFIGURED_SMALL_DUNGEON);
            Registry.register(registry, new ResourceLocation(BetterDungeons.MOD_ID, "spider_dungeon"), BDModConfiguredStructures.CONFIGURED_SPIDER_DUNGEON);
            Registry.register(registry, new ResourceLocation(BetterDungeons.MOD_ID, "skeleton_dungeon"), BDModConfiguredStructures.CONFIGURED_SKELETON_DUNGEON);
            Registry.register(registry, new ResourceLocation(BetterDungeons.MOD_ID, "zombie_dungeon"), BDModConfiguredStructures.CONFIGURED_ZOMBIE_DUNGEON);

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
