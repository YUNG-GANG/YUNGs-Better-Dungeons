package com.yungnickyoung.minecraft.betterdungeons.world;

public enum DungeonType {
    SPIDER("spider"),
    SKELETON("skeleton"),
    ZOMBIE("zombie"),
    ZOMBIFIED_PIGLIN("zombified_piglin"),
    WITHER_SKELETON("wither_skeleton"),
    BLAZE("blaze");

    private final String name;

    DungeonType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static DungeonType fromString(String name) {
        for (DungeonType type : DungeonType.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("No constant with text %s", name));
    }
}
