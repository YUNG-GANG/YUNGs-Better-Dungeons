package com.yungnickyoung.minecraft.betterdungeons;

import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import com.yungnickyoung.minecraft.betterdungeons.init.*;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.skeleton_dungeon.SkeletonDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.small_dungeon.SmallDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.SpiderDungeonStructure;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.zombie_dungeon.ZombieDungeonStructure;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class BetterDungeons implements ModInitializer, DedicatedServerModInitializer, ClientModInitializer {
    public static final String MOD_ID = "betterdungeons";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    /** Better Dungeons config. Uses AutoConfig. **/
    public static BDConfig CONFIG;

    /** Global var for placing debug blocks when generating spider dungeons **/
    public static final boolean DEBUG_MODE = false;

    @Override
    public void onInitialize() {
        BDModConfig.init();
        BDModProcessors.init();
        BDModStructures.init();
        BDModConfiguredStructures.init();
        BDModStructurePieces.init();
    }

    /**
     * We must use the following init handlers to ensure our ServerWorldEvents.LOAD event always fires
     * after Fabric API's usage of the same event. This is done so that our changes don't get overwritten
     * by the Fabric API adding structure spacings to all dimensions.
     * Credit to TelepathicGrunt.
     */
    @Override
    public void onInitializeServer() {
        enforceDimensionWhitelist();
    }

    @Override
    public void onInitializeClient() {
        enforceDimensionWhitelist();
    }

    public static void enforceDimensionWhitelist() {
        // Controls the dimension blacklisting
        ServerWorldEvents.LOAD.register((MinecraftServer minecraftServer, ServerWorld serverWorld) -> {
            // We use a temp map to add our spacing because some mods handle immutable maps
            Map<StructureFeature<?>, StructureConfig> tempMap = new HashMap<>(serverWorld.getChunkManager().getChunkGenerator().getStructuresConfig().getStructures());
            String dimensionName = serverWorld.getRegistryKey().getValue().toString();

            // Handler that removes the given structure if it is not whitelisted
            BiConsumer<List<String>, StructureFeature<?>> whitelistHandler = (List<String> whitelist, StructureFeature<?> structure) -> {
                if (!whitelist.contains(dimensionName) || (serverWorld.getChunkManager().getChunkGenerator() instanceof FlatChunkGenerator && serverWorld.getRegistryKey().equals(World.OVERWORLD))) {
                    tempMap.keySet().remove(structure);
                }
            };

            whitelistHandler.accept(SmallDungeonStructure.whitelistedDimensions, BDModStructures.SMALL_DUNGEON);
            whitelistHandler.accept(SpiderDungeonStructure.whitelistedDimensions, BDModStructures.SPIDER_DUNGEON);
            whitelistHandler.accept(SkeletonDungeonStructure.whitelistedDimensions, BDModStructures.SKELETON_DUNGEON);
            whitelistHandler.accept(ZombieDungeonStructure.whitelistedDimensions, BDModStructures.ZOMBIE_DUNGEON);

            serverWorld.getChunkManager().getChunkGenerator().getStructuresConfig().structures = tempMap;
        });
    }
}
