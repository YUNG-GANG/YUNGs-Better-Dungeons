package com.yungnickyoung.minecraft.betterdungeons.world;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

public class DungeonContext {
    private static final ThreadLocal<DungeonContext> CONTEXT = new ThreadLocal<>();

    private WeakReference<Integer> bannerCount;
    private WeakReference<Integer> chestCount;

    public DungeonContext(int bannerCount, int chestCount) {
        this.bannerCount = new WeakReference<>(bannerCount);
        this.chestCount = new WeakReference<>(chestCount);
    }

    public int getBannerCount() {
        Integer value = bannerCount.get();
        return value == null ? 0 : value;
    }

    public int getChestCount() {
        Integer value = chestCount.get();
        return value == null ? 0 : value;
    }

    public void incrementBannerCount() {
        Integer boxedVal = bannerCount.get();
        int val = boxedVal == null ? 0 : boxedVal;
        bannerCount.clear();
        bannerCount = new WeakReference<>(val + 1);
    }

    public void incrementChestCount() {
        Integer boxedVal = chestCount.get();
        int val = boxedVal == null ? 0 : boxedVal;
        chestCount.clear();
        chestCount = new WeakReference<>(val + 1);
    }

    /**
     * Consume the currently held DungeonContext.
     * A null value means the context has already been consumed.
     */
    @Nullable
    public static DungeonContext pop() {
        DungeonContext context = CONTEXT.get();
        CONTEXT.set(null);
        return context;
    }

    /**
     * Peek the currently held DungeonContext without consuming it.
     * A null value means the context has already been consumed.
     */
    @Nullable
    public static DungeonContext peek() {
        return CONTEXT.get();
    }

    /**
     * Should only be called right before structure processing,
     * so that processors can be guaranteed to retrieve the proper context.
     */
    public static void initialize() {
        CONTEXT.set(new DungeonContext(0, 0));
    }
}
