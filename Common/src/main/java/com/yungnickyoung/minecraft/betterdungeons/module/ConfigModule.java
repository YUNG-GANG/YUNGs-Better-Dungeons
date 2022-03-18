package com.yungnickyoung.minecraft.betterdungeons.module;

public class ConfigModule {
    public final General general = new General();
    public final SpiderDungeon spiderDungeons = new SpiderDungeon();
    public final SkeletonDungeon skeletonDungeons = new SkeletonDungeon();
    public final ZombieDungeon zombieDungeons = new ZombieDungeon();
    public final SmallDungeon smallDungeons = new SmallDungeon();

    public static class General {
        public boolean enableHeads = true;
        public boolean removeVanillaDungeons = true;
        public boolean enableNetherBlocks = true;
    }

    public static class SpiderDungeon {
        public int spiderDungeonStartMinY = 70;
        public int spiderDungeonStartMaxY = 71;
    }

    public static class SkeletonDungeon {
        public int skeletonDungeonStartMinY = -50;
        public int skeletonDungeonStartMaxY = -30;
    }

    public static class ZombieDungeon {
        public int zombieDungeonStartMinY = 50;
        public int zombieDungeonStartMaxY = 51;
        public int zombieDungeonMaxSurfaceStaircaseLength = 20;
    }

     public static class SmallDungeon {
         public int smallDungeonMinY = -50;
         public int smallDungeonMaxY = 50;
         public int bannerMaxCount = 2;
         public int chestMinCount = 1;
         public int chestMaxCount = 2;
         public boolean enableOreProps = true;
     }
}
