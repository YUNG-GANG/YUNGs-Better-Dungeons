package com.yungnickyoung.minecraft.betterdungeons.world;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

public class DungeonContext {
    private static final ThreadLocal<DungeonContext> CONTEXT = new ThreadLocal<>();

    private final WeakReference<DungeonType> type;

    public DungeonContext(DungeonType type) {
        this.type = new WeakReference<>(type);
    }

    public DungeonType getType() {
        return type.get();
    }

    /**
     * Consume the currently held StructureGenContext.
     * A null value means the context has already been consumed.
     */
    @Nullable
    public static DungeonContext pop() {
        DungeonContext context = CONTEXT.get();
        CONTEXT.set(null);
        return context;
    }

    /**
     * Peek the currently held StructureGenContext without consuming it.
     * A null value means the context has already been consumed.
     */
    @Nullable
    public static DungeonContext peek() {
        return CONTEXT.get();
    }

    /**
     * Should only be called right before structure generation,
     * so that processors can be guaranteed to retrieve the proper context.
     */
    public static void push(DungeonType type) {
        CONTEXT.set(new DungeonContext(type));
    }
}
