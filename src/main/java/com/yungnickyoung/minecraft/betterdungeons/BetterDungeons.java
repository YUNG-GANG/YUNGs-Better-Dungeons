package com.yungnickyoung.minecraft.betterdungeons;

import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import com.yungnickyoung.minecraft.betterdungeons.init.*;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BetterDungeons implements ModInitializer {
    public static final String MOD_ID = "betterdungeons";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    /** Better Dungeons config. Uses AutoConfig. **/
    public static BDConfig CONFIG;

    /** Global var for placing debug blocks when generating spider dungeons **/
    public static final boolean DEBUG_MODE = false;

    @Override
    public void onInitialize() {
        BDModConfig.init();
        BDModProcessors.init();
        BDModStructureFeatures.init();
        BDModConfiguredStructures.init();
        BDModStructurePieces.init();
    }
}
