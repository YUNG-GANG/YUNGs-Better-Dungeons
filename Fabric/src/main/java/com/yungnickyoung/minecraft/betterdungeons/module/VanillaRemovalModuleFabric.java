package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.resources.ResourceLocation;

/**
 * In 1.19, Forge allows for Biome modification via resources.
 * But for Fabric, we must use the BiomeModifications API.
 */
public class VanillaRemovalModuleFabric {
    public static void init() {
        // Remove vanilla dungeons, if enabled
        if (BetterDungeonsCommon.CONFIG.general.removeVanillaDungeons) {
            BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(BetterDungeonsCommon.MOD_ID, "vanilla_dungeon_removal"))
                    .add(ModificationPhase.REMOVALS,
                            biomeSelectionContext -> biomeSelectionContext.hasPlacedFeature(CavePlacements.MONSTER_ROOM),
                            modificationContext -> modificationContext.getGenerationSettings().removeFeature(CavePlacements.MONSTER_ROOM))
                    .add(ModificationPhase.REMOVALS,
                            biomeSelectionContext -> biomeSelectionContext.hasPlacedFeature(CavePlacements.MONSTER_ROOM_DEEP),
                            modificationContext -> modificationContext.getGenerationSettings().removeFeature(CavePlacements.MONSTER_ROOM_DEEP));
        }
    }
}
