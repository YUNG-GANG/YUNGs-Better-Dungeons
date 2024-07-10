package com.yungnickyoung.minecraft.betterdungeons;

import com.yungnickyoung.minecraft.betterdungeons.module.ConfigModuleNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(BetterDungeonsCommon.MOD_ID)
public class BetterDungeonsNeoForge {
    public static IEventBus loadingContextEventBus;

    public BetterDungeonsNeoForge(IEventBus eventBus, ModContainer container) {
        BetterDungeonsNeoForge.loadingContextEventBus = eventBus;

        BetterDungeonsCommon.init();
        ConfigModuleNeoForge.init(container);
    }
}
