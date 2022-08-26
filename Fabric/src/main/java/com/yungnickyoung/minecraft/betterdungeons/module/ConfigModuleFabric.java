package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfigFabric;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.world.InteractionResult;

public class ConfigModuleFabric {
    public static void init() {
        AutoConfig.register(BDConfigFabric.class, Toml4jConfigSerializer::new);
        AutoConfig.getConfigHolder(BDConfigFabric.class).registerSaveListener(ConfigModuleFabric::bakeConfig);
        AutoConfig.getConfigHolder(BDConfigFabric.class).registerLoadListener(ConfigModuleFabric::bakeConfig);
        bakeConfig(AutoConfig.getConfigHolder(BDConfigFabric.class).get());
    }

    private static InteractionResult bakeConfig(ConfigHolder<BDConfigFabric> configHolder, BDConfigFabric configFabric) {
        bakeConfig(configFabric);
        return InteractionResult.SUCCESS;
    }

    private static void bakeConfig(BDConfigFabric configFabric) {
        BetterDungeonsCommon.CONFIG.general.removeVanillaDungeons = configFabric.betterDungeons.general.removeVanillaDungeons;
        BetterDungeonsCommon.CONFIG.general.enableHeads = configFabric.betterDungeons.general.enableHeads;
        BetterDungeonsCommon.CONFIG.general.enableNetherBlocks = configFabric.betterDungeons.general.enableNetherBlocks;
        BetterDungeonsCommon.CONFIG.zombieDungeons.zombieDungeonMaxSurfaceStaircaseLength = configFabric.betterDungeons.zombieDungeon.zombieDungeonMaxSurfaceStaircaseLength;
        BetterDungeonsCommon.CONFIG.smallDungeons.bannerMaxCount = configFabric.betterDungeons.smallDungeon.bannerMaxCount;
        BetterDungeonsCommon.CONFIG.smallDungeons.chestMinCount = configFabric.betterDungeons.smallDungeon.chestMinCount;
        BetterDungeonsCommon.CONFIG.smallDungeons.chestMaxCount = configFabric.betterDungeons.smallDungeon.chestMaxCount;
        BetterDungeonsCommon.CONFIG.smallDungeons.enableOreProps = configFabric.betterDungeons.smallDungeon.enableOreProps;
        BetterDungeonsCommon.CONFIG.smallNetherDungeons.enabled = configFabric.betterDungeons.smallNetherDungeon.enabled;
        BetterDungeonsCommon.CONFIG.smallNetherDungeons.bannerMaxCount = configFabric.betterDungeons.smallNetherDungeon.bannerMaxCount;
    }
}
