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
            BiomeModifications.create(new ResourceLocation(BetterDungeonsCommon.MOD_ID, "vanilla_dungeon_removal"))
                    .add(ModificationPhase.REMOVALS,
                            biomeSelectionContext -> biomeSelectionContext.hasBuiltInPlacedFeature(CavePlacements.MONSTER_ROOM.value()),
                            modificationContext -> modificationContext.getGenerationSettings().removeBuiltInFeature(CavePlacements.MONSTER_ROOM.value()))
                    .add(ModificationPhase.REMOVALS,
                            biomeSelectionContext -> biomeSelectionContext.hasBuiltInPlacedFeature(CavePlacements.MONSTER_ROOM_DEEP.value()),
                            modificationContext -> modificationContext.getGenerationSettings().removeBuiltInFeature(CavePlacements.MONSTER_ROOM_DEEP.value()));
        }
    }
}
