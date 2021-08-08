package com.yungnickyoung.minecraft.betterdungeons.compat;

import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import vazkii.quark.api.event.ModuleStateChangedEvent;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.content.world.module.underground.SpiderNestUndergroundBiomeModule;

public class QuarkCompat {
    public static boolean enabled = false;

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(QuarkCompat::updateModuleStatus);
        MinecraftForge.EVENT_BUS.addListener(QuarkCompat::worldLoad);
    }

    public static BlockState getCobbedstone() {
        return SpiderNestUndergroundBiomeModule.cobbedstone.getBlock().getDefaultState();
    }

    private static void updateModuleStatus(final ModuleStateChangedEvent event) {
        if (event.eventName.equals("spider_nest_underground_biome")) {
            enabled = event.enabled && BDConfig.spiderDungeons.useQuarkCobbedstone.get();
        }
    }

    private static void worldLoad(final WorldEvent.Load event) {
        for (ModuleCategory category : ModuleCategory.values()) {
            for (QuarkModule module : category.getOwnedModules()) {
                if (module.lowercaseName.equals("spider_nest_underground_biome")) {
                    enabled = module.enabled && BDConfig.spiderDungeons.useQuarkCobbedstone.get();
                    return;
                }
            }
        }
    }
}
