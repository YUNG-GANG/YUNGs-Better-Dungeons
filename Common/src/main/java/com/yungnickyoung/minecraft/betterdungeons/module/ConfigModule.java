package com.yungnickyoung.minecraft.betterdungeons.module;

public class ConfigModule {
    public final General general = new General();
    public final ZombieDungeon zombieDungeons = new ZombieDungeon();
    public final SmallDungeon smallDungeons = new SmallDungeon();
    public final SmallNetherDungeon smallNetherDungeons = new SmallNetherDungeon();

    public static class General {
        public boolean enableHeads = true;
        public boolean enableNetherBlocks = true;

        // As of 1.19, Forge offers Biome modification via resources, so this is only used in Fabric
        public boolean removeVanillaDungeons = true;
    }

    public static class ZombieDungeon {
        public int zombieDungeonMaxSurfaceStaircaseLength = 20;
    }

    public static class SmallDungeon {
        public int bannerMaxCount = 2;
        public int chestMinCount = 1;
        public int chestMaxCount = 2;
        public boolean enableOreProps = true;
    }

    public static class SmallNetherDungeon {
        public boolean enabled = false;
        public int bannerMaxCount = 2;
    }
}
