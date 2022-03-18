package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class StructureProcessorModuleFabric {
    public static void init() {
        register("waterlogged_processor", StructureProcessorModule.WATERLOGGED_PROCESSOR);
        register("mob_spawner_processor", StructureProcessorModule.MOB_SPAWNER_PROCESSOR);
        register("head_processor", StructureProcessorModule.HEAD_PROCESSOR);
        register("nether_block_processor", StructureProcessorModule.NETHER_BLOCK_PROCESSOR);
        register("candle_processor", StructureProcessorModule.CANDLE_PROCESSOR);

        // Small dungeons
        register("small_dungeon_ceiling_prop_processor", StructureProcessorModule.SMALL_DUNGEON_CEILING_PROP_PROCESSOR);
        register("small_dungeon_ceiling_lamp_processor", StructureProcessorModule.SMALL_DUNGEON_CEILING_LAMP_PROCESSOR);
        register("small_dungeon_banner_processor", StructureProcessorModule.SMALL_DUNGEON_BANNER_PROCESSOR);
        register("small_dungeon_chest_processor", StructureProcessorModule.SMALL_DUNGEON_CHEST_PROCESSOR);
        register("small_dungeon_cobblestone_processor", StructureProcessorModule.SMALL_DUNGEON_COBBLE_PROCESSOR);
        register("small_dungeon_leg_processor", StructureProcessorModule.SMALL_DUNGEON_LEG_PROCESSOR);
        register("small_dungeon_ceiling_processor", StructureProcessorModule.SMALL_DUNGEON_CEILING_PROCESSOR);
        register("small_dungeon_ore_processor", StructureProcessorModule.SMALL_DUNGEON_ORE_PROCESSOR);

        // Skeleton dungeons
        register("skeleton_dungeon_ruined_stone_bricks_processor", StructureProcessorModule.SKELETON_DUNGEON_RUINED_STONE_BRICKS_PROCESSOR);
        register("skeleton_mob_spawner_processor", StructureProcessorModule.SKELETON_MOB_SPAWNER_PROCESSOR);
        register("skeleton_dungeon_leg_processor", StructureProcessorModule.SKELETON_DUNGEON_LEG_PROCESSOR);

        // Zombie dungeons
        register("zombie_dungeon_cubby_processor", StructureProcessorModule.ZOMBIE_DUNGEON_CUBBY_PROCESSOR);
        register("zombie_dungeon_stair_processor", StructureProcessorModule.ZOMBIE_DUNGEON_STAIR_PROCESSOR);
        register("zombie_mob_spawner_processor", StructureProcessorModule.ZOMBIE_MOB_SPAWNER_PROCESSOR);
        register("zombie_tombstone_spawner_processor", StructureProcessorModule.ZOMBIE_TOMBSTONE_SPAWNER_PROCESSOR);
        register("zombie_main_stairs_processor", StructureProcessorModule.ZOMBIE_MAIN_STAIRS_PROCESSOR);
        register("zombie_rot_processor", StructureProcessorModule.ZOMBIE_ROT_PROCESSOR);
        register("zombie_dungeon_leg_processor", StructureProcessorModule.ZOMBIE_DUNGEON_LEG_PROCESSOR);
        register("zombie_dungeon_flower_pot_processor", StructureProcessorModule.ZOMBIE_DUNGEON_FLOWER_POT_PROCESSOR);
    }

    private static void register(String name, StructureProcessorType<?> processorType) {
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, name), processorType);
    }
}
