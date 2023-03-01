package com.yungnickyoung.minecraft.betterdungeons;

import com.yungnickyoung.minecraft.betterdungeons.module.ConfigModule;
import com.yungnickyoung.minecraft.betterdungeons.services.Services;
import com.yungnickyoung.minecraft.yungsapi.api.YungAutoRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BetterDungeonsCommon {
    public static final String MOD_ID = "betterdungeons";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final ConfigModule CONFIG = new ConfigModule();

    /** Global var for placing debug blocks when generating spider dungeons **/
    public static final boolean DEBUG_MODE = false;

    public static void init() {
        YungAutoRegister.scanPackageForAnnotations("com.yungnickyoung.minecraft.betterdungeons.module");
        Services.MODULES.loadModules();
    }
}
