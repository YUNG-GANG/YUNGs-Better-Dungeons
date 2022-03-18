package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class TagModuleForge {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(TagModuleForge::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            TagModule.HAS_SMALL_DUNGEON = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "has_small_dungeon"));
            TagModule.HAS_SPIDER_DUNGEON = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "has_spider_dungeon"));
            TagModule.HAS_SKELETON_DUNGEON = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "has_skeleton_dungeon"));
            TagModule.HAS_ZOMBIE_DUNGEON = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "has_zombie_dungeon"));
        });
    }
}
