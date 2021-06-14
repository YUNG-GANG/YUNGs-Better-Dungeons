package com.yungnickyoung.minecraft.betterdungeons.init;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon.SkeletonDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.small_dungeon.SmallDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BDModStructures {
    public static final DeferredRegister<Structure<?>> DEFERRED_REGISTRY = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BetterDungeons.MOD_ID);
    public static final RegistryObject<Structure<NoFeatureConfig>> SMALL_DUNGEON = DEFERRED_REGISTRY.register("small_dungeon", SmallDungeonStructure::new);
    public static final RegistryObject<Structure<NoFeatureConfig>> SPIDER_DUNGEON = DEFERRED_REGISTRY.register("spider_dungeon", SpiderDungeonStructure::new);
    public static final RegistryObject<Structure<NoFeatureConfig>> SKELETON_DUNGEON = DEFERRED_REGISTRY.register("skeleton_dungeon", SkeletonDungeonStructure::new);
//    public static final RegistryObject<Structure<NoFeatureConfig>> ZOMBIE_DUNGEON = DEFERRED_REGISTRY.register("zombie_dungeon", () -> new ZombieDungeonStructure(NoFeatureConfig.field_236558_a_));

    public static void init() {
        // Register our deferred registry
        DEFERRED_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());

        // Register event listeners
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BDModStructures::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(BDModStructures::addDimensionalSpacing);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, BDModStructures::onBiomeLoad);
    }


    /**
     * Set up Better Dungeons structures.
     */
    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Add dungeons to the structure map
            Structure.NAME_STRUCTURE_BIMAP.put("Small Dungeon".toLowerCase(Locale.ROOT), SMALL_DUNGEON.get());
            Structure.NAME_STRUCTURE_BIMAP.put("Spider Dungeon".toLowerCase(Locale.ROOT), SPIDER_DUNGEON.get());
            Structure.NAME_STRUCTURE_BIMAP.put("Skeleton Dungeon".toLowerCase(Locale.ROOT), SKELETON_DUNGEON.get());
//            Structure.NAME_STRUCTURE_BIMAP.put("Zombie Dungeon".toLowerCase(Locale.ROOT), ZOMBIE_DUNGEON.get());

            // Add structure + spacing settings to default dimension structures.
            // Note that we make a similar change in the WorldEvent.Load handler
            // as a safety for custom dimension support.
            DimensionStructuresSettings.field_236191_b_ =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                    .putAll(DimensionStructuresSettings.field_236191_b_)
                    // TODO - edit structure separation settings & expose to config
                    .put(SMALL_DUNGEON.get(), new StructureSeparationSettings(BDConfig.smallDungeons.smallDungeonSeparationDistance.get(), BDConfig.smallDungeons.smallDungeonSeparationDistance.get() / 2, 34239823))
                    .put(SPIDER_DUNGEON.get(), new StructureSeparationSettings(BDConfig.spiderDungeons.spiderDungeonSeparationDistance.get(), BDConfig.spiderDungeons.spiderDungeonSeparationDistance.get() / 2, 596523129))
                    .put(SKELETON_DUNGEON.get(), new StructureSeparationSettings(BDConfig.skeletonDungeons.skeletonDungeonSeparationDistance.get(), BDConfig.skeletonDungeons.skeletonDungeonSeparationDistance.get() / 2, 732581671))
//                    .put(ZOMBIE_DUNGEON.get(), new StructureSeparationSettings(85, 50, 59390292))
                    .build();

            // Register the configured structure features
            Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(BetterDungeons.MOD_ID, "small_dungeon"), BDModConfiguredStructures.CONFIGURED_SMALL_DUNGEON);
            Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(BetterDungeons.MOD_ID, "spider_dungeon"), BDModConfiguredStructures.CONFIGURED_SPIDER_DUNGEON);
            Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(BetterDungeons.MOD_ID, "skeleton_dungeon"), BDModConfiguredStructures.CONFIGURED_SKELETON_DUNGEON);
//            Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(BetterDungeons.MOD_ID, "zombie_dungeon"), ModConfiguredStructures.CONFIGURED_ZOMBIE_DUNGEON);

            // Add structure features to this to prevent any issues if other mods' custom ChunkGenerators use FlatGenerationSettings.STRUCTURES
            FlatGenerationSettings.STRUCTURES.put(SMALL_DUNGEON.get(), BDModConfiguredStructures.CONFIGURED_SMALL_DUNGEON);
            FlatGenerationSettings.STRUCTURES.put(SPIDER_DUNGEON.get(), BDModConfiguredStructures.CONFIGURED_SPIDER_DUNGEON);
            FlatGenerationSettings.STRUCTURES.put(SKELETON_DUNGEON.get(), BDModConfiguredStructures.CONFIGURED_SKELETON_DUNGEON);
//            FlatGenerationSettings.STRUCTURES.put(ZOMBIE_DUNGEON.get(), ModConfiguredStructures.CONFIGURED_ZOMBIE_DUNGEON);

            // Register pieces
            BDModStructurePieces.init();

            // Register separation settings for mods that might need it, like Terraforged
            WorldGenRegistries.NOISE_SETTINGS.getEntries().forEach(settings -> {
                Map<Structure<?>, StructureSeparationSettings> structureMap = settings.getValue().getStructures().func_236195_a_();

                // Precaution in case a mod makes the structure map immutable like datapacks do
                if (structureMap instanceof ImmutableMap) {
                    Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                    tempMap.put(SMALL_DUNGEON.get(), DimensionStructuresSettings.field_236191_b_.get(SMALL_DUNGEON.get()));
                    tempMap.put(SPIDER_DUNGEON.get(), DimensionStructuresSettings.field_236191_b_.get(SPIDER_DUNGEON.get()));
                    tempMap.put(SKELETON_DUNGEON.get(), DimensionStructuresSettings.field_236191_b_.get(SKELETON_DUNGEON.get()));
//                    tempMap.put(ZOMBIE_DUNGEON.get(), DimensionStructuresSettings.field_236191_b_.get(ZOMBIE_DUNGEON.get()));
                    settings.getValue().getStructures().field_236193_d_ = tempMap;
                } else {
                    structureMap.put(SMALL_DUNGEON.get(), DimensionStructuresSettings.field_236191_b_.get(SMALL_DUNGEON.get()));
                    structureMap.put(SPIDER_DUNGEON.get(), DimensionStructuresSettings.field_236191_b_.get(SPIDER_DUNGEON.get()));
                    structureMap.put(SKELETON_DUNGEON.get(), DimensionStructuresSettings.field_236191_b_.get(SKELETON_DUNGEON.get()));
//                    structureMap.put(ZOMBIE_DUNGEON.get(), DimensionStructuresSettings.field_236191_b_.get(ZOMBIE_DUNGEON.get()));
                }
            });
        });
    }

    /**
     * Adds the appropriate structure feature to each biome as it loads in.
     */
    private static void onBiomeLoad(BiomeLoadingEvent event) {
        // Remove vanilla dungeons
        event.getGeneration().getFeatures(GenerationStage.Decoration.UNDERGROUND_STRUCTURES).removeIf(supplier -> supplier.get().feature.equals(Features.MONSTER_ROOM.feature));

        // Don't spawn in water biomes
        if (event.getCategory() == Biome.Category.OCEAN || event.getCategory() == Biome.Category.RIVER || event.getCategory() == Biome.Category.BEACH)
            return;

        // Add dungeons to biome generation settings
        if (BDConfig.smallDungeons.enableSmallDungeons.get())
            event.getGeneration().getStructures().add(() -> BDModConfiguredStructures.CONFIGURED_SMALL_DUNGEON);

        if (BDConfig.spiderDungeons.enableSpiderDungeons.get())
            event.getGeneration().getStructures().add(() -> BDModConfiguredStructures.CONFIGURED_SPIDER_DUNGEON);

        if (BDConfig.skeletonDungeons.enableSkeletonDungeons.get())
            event.getGeneration().getStructures().add(() -> BDModConfiguredStructures.CONFIGURED_SKELETON_DUNGEON);

//        event.getGeneration().getStructures().add(() -> ModConfiguredStructures.CONFIGURED_ZOMBIE_DUNGEON);
    }

    /**
     * We must manually add the separation settings for our structure to spawn.
     */
    private static Method GETCODEC_METHOD; // Cached instance since this will never change once initialized

    @SuppressWarnings("unchecked")
    private static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

            // Skip Terraforged's chunk generator as they are a special case of a mod locking down their chunkgenerator.
            // Credits to TelepathicGrunt for this.
            try {
                if (GETCODEC_METHOD == null) {
                    GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                }

                ResourceLocation chunkGenResourceLocation  = Registry.CHUNK_GENERATOR_CODEC.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkProvider().generator));
                if (chunkGenResourceLocation  != null && chunkGenResourceLocation .getNamespace().equals("terraforged")) {
                    return;
                }
            } catch (Exception e) {
                BetterDungeons.LOGGER.error("Was unable to check if " + serverWorld.getDimensionKey().getLocation() + " is using Terraforged's ChunkGenerator.");
            }

            // Prevent spawning in superflat world
            if (serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator && serverWorld.getDimensionKey().equals(World.OVERWORLD)) {
                return;
            }

            // We use a temp map because some mods handle immutable maps.
            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
            tempMap.put(BDModStructures.SMALL_DUNGEON.get(), DimensionStructuresSettings.field_236191_b_.get(BDModStructures.SMALL_DUNGEON.get()));
            tempMap.put(BDModStructures.SPIDER_DUNGEON.get(), DimensionStructuresSettings.field_236191_b_.get(BDModStructures.SPIDER_DUNGEON.get()));
            tempMap.put(BDModStructures.SKELETON_DUNGEON.get(), DimensionStructuresSettings.field_236191_b_.get(BDModStructures.SKELETON_DUNGEON.get()));
//            tempMap.put(ModStructures.ZOMBIE_DUNGEON.get(), DimensionStructuresSettings.field_236191_b_.get(ModStructures.ZOMBIE_DUNGEON.get()));
            serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
        }
    }
}
