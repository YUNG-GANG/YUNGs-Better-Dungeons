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
        BetterDungeonsCommon.CONFIG.general.enableHeads = configFabric.betterDungeons.general.enableHeads;
        BetterDungeonsCommon.CONFIG.general.removeVanillaDungeons = configFabric.betterDungeons.general.removeVanillaDungeons;
        BetterDungeonsCommon.CONFIG.general.enableNetherBlocks = configFabric.betterDungeons.general.enableNetherBlocks;
        BetterDungeonsCommon.CONFIG.spiderDungeons.spiderDungeonStartMinY = configFabric.betterDungeons.spiderDungeon.spiderDungeonStartMinY;
        BetterDungeonsCommon.CONFIG.spiderDungeons.spiderDungeonStartMaxY = configFabric.betterDungeons.spiderDungeon.spiderDungeonStartMaxY;
        BetterDungeonsCommon.CONFIG.skeletonDungeons.skeletonDungeonStartMinY = configFabric.betterDungeons.skeletonDungeon.skeletonDungeonStartMinY;
        BetterDungeonsCommon.CONFIG.skeletonDungeons.skeletonDungeonStartMaxY = configFabric.betterDungeons.skeletonDungeon.skeletonDungeonStartMaxY;
        BetterDungeonsCommon.CONFIG.zombieDungeons.zombieDungeonStartMinY = configFabric.betterDungeons.zombieDungeon.zombieDungeonStartMinY;
        BetterDungeonsCommon.CONFIG.zombieDungeons.zombieDungeonStartMaxY = configFabric.betterDungeons.zombieDungeon.zombieDungeonStartMaxY;
        BetterDungeonsCommon.CONFIG.zombieDungeons.zombieDungeonMaxSurfaceStaircaseLength = configFabric.betterDungeons.zombieDungeon.zombieDungeonMaxSurfaceStaircaseLength;
        BetterDungeonsCommon.CONFIG.smallDungeons.smallDungeonMinY = configFabric.betterDungeons.smallDungeon.smallDungeonMinY;
        BetterDungeonsCommon.CONFIG.smallDungeons.smallDungeonMaxY = configFabric.betterDungeons.smallDungeon.smallDungeonMaxY;
        BetterDungeonsCommon.CONFIG.smallDungeons.bannerMaxCount = configFabric.betterDungeons.smallDungeon.bannerMaxCount;
        BetterDungeonsCommon.CONFIG.smallDungeons.chestMinCount = configFabric.betterDungeons.smallDungeon.chestMinCount;
        BetterDungeonsCommon.CONFIG.smallDungeons.chestMaxCount = configFabric.betterDungeons.smallDungeon.chestMaxCount;
        BetterDungeonsCommon.CONFIG.smallDungeons.enableOreProps = configFabric.betterDungeons.smallDungeon.enableOreProps;
    }
}
