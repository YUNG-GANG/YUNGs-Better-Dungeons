package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsNeoForge;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfigNeoForge;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

public class ConfigModuleNeoForge {
    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BDConfigNeoForge.SPEC, "betterdungeons-neoforge-1_20_4.toml");
        NeoForge.EVENT_BUS.addListener(ConfigModuleNeoForge::onWorldLoad);
        BetterDungeonsNeoForge.loadingContextEventBus.addListener(ConfigModuleNeoForge::onConfigChange);
    }

    private static void onWorldLoad(LevelEvent.Load event) {
        bakeConfig();
    }

    private static void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == BDConfigNeoForge.SPEC) {
            bakeConfig();
        }
    }

    private static void bakeConfig() {
        BetterDungeonsCommon.CONFIG.general.enableHeads = BDConfigNeoForge.general.enableHeads.get();
        BetterDungeonsCommon.CONFIG.general.enableNetherBlocks = BDConfigNeoForge.general.enableNetherBlocks.get();
        BetterDungeonsCommon.CONFIG.zombieDungeons.zombieDungeonMaxSurfaceStaircaseLength = BDConfigNeoForge.zombieDungeons.zombieDungeonMaxSurfaceStaircaseLength.get();
        BetterDungeonsCommon.CONFIG.smallDungeons.bannerMaxCount = BDConfigNeoForge.smallDungeons.bannerMaxCount.get();
        BetterDungeonsCommon.CONFIG.smallDungeons.chestMinCount = BDConfigNeoForge.smallDungeons.chestMinCount.get();
        BetterDungeonsCommon.CONFIG.smallDungeons.chestMaxCount = BDConfigNeoForge.smallDungeons.chestMaxCount.get();
        BetterDungeonsCommon.CONFIG.smallDungeons.enableOreProps = BDConfigNeoForge.smallDungeons.enableOreProps.get();
        BetterDungeonsCommon.CONFIG.smallNetherDungeons.enabled = BDConfigNeoForge.smallNetherDungeons.enabled.get();
        BetterDungeonsCommon.CONFIG.smallNetherDungeons.witherSkeletonsDropWitherSkulls = BDConfigNeoForge.smallNetherDungeons.witherSkeletonsDropWitherSkulls.get();
        BetterDungeonsCommon.CONFIG.smallNetherDungeons.blazesDropBlazeRods = BDConfigNeoForge.smallNetherDungeons.blazesDropBlazeRods.get();
        BetterDungeonsCommon.CONFIG.smallNetherDungeons.bannerMaxCount = BDConfigNeoForge.smallNetherDungeons.bannerMaxCount.get();
    }
}
