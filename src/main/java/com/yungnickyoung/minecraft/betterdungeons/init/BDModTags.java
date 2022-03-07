package com.yungnickyoung.minecraft.betterdungeons.init;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class BDModTags {
    public static TagKey<Biome> HAS_SMALL_DUNGEON;
    public static TagKey<Biome> HAS_SPIDER_DUNGEON;
    public static TagKey<Biome> HAS_SKELETON_DUNGEON;
    public static TagKey<Biome> HAS_ZOMBIE_DUNGEON;

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BDModTags::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            HAS_SMALL_DUNGEON = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterDungeons.MOD_ID, "has_small_dungeon"));
            HAS_SPIDER_DUNGEON = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterDungeons.MOD_ID, "has_spider_dungeon"));
            HAS_SKELETON_DUNGEON = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterDungeons.MOD_ID, "has_skeleton_dungeon"));
            HAS_ZOMBIE_DUNGEON = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterDungeons.MOD_ID, "has_zombie_dungeon"));
        });
    }
}
