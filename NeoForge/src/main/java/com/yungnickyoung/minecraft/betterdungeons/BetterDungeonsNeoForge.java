package com.yungnickyoung.minecraft.betterdungeons;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(BetterDungeonsCommon.MOD_ID)
public class BetterDungeonsNeoForge {
    public static IEventBus loadingContextEventBus;

    public BetterDungeonsNeoForge(IEventBus eventBus) {
        BetterDungeonsNeoForge.loadingContextEventBus = eventBus;

        BetterDungeonsCommon.init();
    }
}
