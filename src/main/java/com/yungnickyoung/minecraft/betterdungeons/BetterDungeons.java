package com.yungnickyoung.minecraft.betterdungeons;

import com.yungnickyoung.minecraft.betterdungeons.init.BDModConfig;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModStructures;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BetterDungeons.MOD_ID)
public class BetterDungeons {
    public static final String MOD_ID = "betterdungeons";
    public static final Logger LOGGER = LogManager.getLogger();

    // Global var for placing debug blocks when generating spider dungeons
    public static final boolean DEBUG_MODE = false;

    public BetterDungeons() {
        init();
    }

    private void init() {
        BDModConfig.init();
        BDModProcessors.init();
        BDModStructures.init();
    }
}
