package com.yungnickyoung.minecraft.betterdungeons.config.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class BDModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(BDConfig.class, parent).get();
    }
}
