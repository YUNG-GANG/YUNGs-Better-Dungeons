package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfigForge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ConfigModuleForge {
    public static void init() {
        // Register mod config with Forge
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BDConfigForge.SPEC, "betterdungeons-forge-1_18.toml");
        MinecraftForge.EVENT_BUS.addListener(ConfigModuleForge::onWorldLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigModuleForge::onConfigChange);
    }

    private static void onWorldLoad(WorldEvent.Load event) {
        bakeConfig();
    }

    private static void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == BDConfigForge.SPEC) {
            bakeConfig();
        }
    }

    private static void bakeConfig() {
        BetterDungeonsCommon.CONFIG.general.enableHeads = BDConfigForge.general.enableHeads.get();
        BetterDungeonsCommon.CONFIG.general.removeVanillaDungeons = BDConfigForge.general.removeVanillaDungeons.get();
        BetterDungeonsCommon.CONFIG.general.enableNetherBlocks = BDConfigForge.general.enableNetherBlocks.get();
        BetterDungeonsCommon.CONFIG.spiderDungeons.spiderDungeonStartMinY = BDConfigForge.spiderDungeons.spiderDungeonStartMinY.get();
        BetterDungeonsCommon.CONFIG.spiderDungeons.spiderDungeonStartMaxY = BDConfigForge.spiderDungeons.spiderDungeonStartMaxY.get();
        BetterDungeonsCommon.CONFIG.skeletonDungeons.skeletonDungeonStartMinY = BDConfigForge.skeletonDungeons.skeletonDungeonStartMinY.get();
        BetterDungeonsCommon.CONFIG.skeletonDungeons.skeletonDungeonStartMaxY = BDConfigForge.skeletonDungeons.skeletonDungeonStartMaxY.get();
        BetterDungeonsCommon.CONFIG.zombieDungeons.zombieDungeonStartMinY = BDConfigForge.zombieDungeons.zombieDungeonStartMinY.get();
        BetterDungeonsCommon.CONFIG.zombieDungeons.zombieDungeonStartMaxY = BDConfigForge.zombieDungeons.zombieDungeonStartMaxY.get();
        BetterDungeonsCommon.CONFIG.zombieDungeons.zombieDungeonMaxSurfaceStaircaseLength = BDConfigForge.zombieDungeons.zombieDungeonMaxSurfaceStaircaseLength.get();
        BetterDungeonsCommon.CONFIG.smallDungeons.smallDungeonMinY = BDConfigForge.smallDungeons.smallDungeonMinY.get();
        BetterDungeonsCommon.CONFIG.smallDungeons.smallDungeonMaxY = BDConfigForge.smallDungeons.smallDungeonMaxY.get();
        BetterDungeonsCommon.CONFIG.smallDungeons.bannerMaxCount = BDConfigForge.smallDungeons.bannerMaxCount.get();
        BetterDungeonsCommon.CONFIG.smallDungeons.chestMinCount = BDConfigForge.smallDungeons.chestMinCount.get();
        BetterDungeonsCommon.CONFIG.smallDungeons.chestMaxCount = BDConfigForge.smallDungeons.chestMaxCount.get();
        BetterDungeonsCommon.CONFIG.smallDungeons.enableOreProps = BDConfigForge.smallDungeons.enableOreProps.get();
    }
}
