package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfigForge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ConfigModuleForge {
    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BDConfigForge.SPEC, "betterdungeons-forge-1_19.toml");
        MinecraftForge.EVENT_BUS.addListener(ConfigModuleForge::onWorldLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigModuleForge::onConfigChange);
    }

    private static void onWorldLoad(LevelEvent.Load event) {
        bakeConfig();
    }

    private static void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == BDConfigForge.SPEC) {
            bakeConfig();
        }
    }

    private static void bakeConfig() {
        BetterDungeonsCommon.CONFIG.general.enableHeads = BDConfigForge.general.enableHeads.get();
        BetterDungeonsCommon.CONFIG.general.enableNetherBlocks = BDConfigForge.general.enableNetherBlocks.get();
        BetterDungeonsCommon.CONFIG.zombieDungeons.zombieDungeonMaxSurfaceStaircaseLength = BDConfigForge.zombieDungeons.zombieDungeonMaxSurfaceStaircaseLength.get();
        BetterDungeonsCommon.CONFIG.smallDungeons.bannerMaxCount = BDConfigForge.smallDungeons.bannerMaxCount.get();
        BetterDungeonsCommon.CONFIG.smallDungeons.chestMinCount = BDConfigForge.smallDungeons.chestMinCount.get();
        BetterDungeonsCommon.CONFIG.smallDungeons.chestMaxCount = BDConfigForge.smallDungeons.chestMaxCount.get();
        BetterDungeonsCommon.CONFIG.smallDungeons.enableOreProps = BDConfigForge.smallDungeons.enableOreProps.get();
        BetterDungeonsCommon.CONFIG.smallNetherDungeons.enabled = BDConfigForge.smallNetherDungeons.enabled.get();
        BetterDungeonsCommon.CONFIG.smallNetherDungeons.witherSkeletonsDropWitherSkulls = BDConfigForge.smallNetherDungeons.witherSkeletonsDropWitherSkulls.get();
        BetterDungeonsCommon.CONFIG.smallNetherDungeons.blazesDropBlazeRods = BDConfigForge.smallNetherDungeons.blazesDropBlazeRods.get();
        BetterDungeonsCommon.CONFIG.smallNetherDungeons.bannerMaxCount = BDConfigForge.smallNetherDungeons.bannerMaxCount.get();
    }
}
