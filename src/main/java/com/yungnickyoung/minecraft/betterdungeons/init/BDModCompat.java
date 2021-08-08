package com.yungnickyoung.minecraft.betterdungeons.init;

import com.yungnickyoung.minecraft.betterdungeons.compat.QuarkCompat;
import net.minecraftforge.fml.ModList;

public class BDModCompat {
    public static void init() {
        if (ModList.get().isLoaded("quark")) {
            QuarkCompat.init();
        }
    }
}
