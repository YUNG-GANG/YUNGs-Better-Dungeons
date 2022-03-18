package com.yungnickyoung.minecraft.betterdungeons;

import net.fabricmc.api.ModInitializer;

public class BetterDungeonsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BetterDungeonsCommon.init();
    }
}
