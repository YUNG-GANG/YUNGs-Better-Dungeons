package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class StructureProcessorModuleForge {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(StructureProcessorModuleForge::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // General use
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "waterlogged_processor"), StructureProcessorModule.WATERLOGGED_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "mob_spawner_processor"), StructureProcessorModule.MOB_SPAWNER_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "head_processor"), StructureProcessorModule.HEAD_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "nether_block_processor"), StructureProcessorModule.NETHER_BLOCK_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "candle_processor"), StructureProcessorModule.CANDLE_PROCESSOR);

            // Small dungeons
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "small_dungeon_ceiling_prop_processor"), StructureProcessorModule.SMALL_DUNGEON_CEILING_PROP_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "small_dungeon_ceiling_lamp_processor"), StructureProcessorModule.SMALL_DUNGEON_CEILING_LAMP_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "small_dungeon_banner_processor"), StructureProcessorModule.SMALL_DUNGEON_BANNER_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "small_dungeon_chest_processor"), StructureProcessorModule.SMALL_DUNGEON_CHEST_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "small_dungeon_cobblestone_processor"), StructureProcessorModule.SMALL_DUNGEON_COBBLE_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "small_dungeon_leg_processor"), StructureProcessorModule.SMALL_DUNGEON_LEG_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "small_dungeon_ceiling_processor"), StructureProcessorModule.SMALL_DUNGEON_CEILING_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "small_dungeon_ore_processor"), StructureProcessorModule.SMALL_DUNGEON_ORE_PROCESSOR);

            // Skeleton dungeons
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "skeleton_dungeon_ruined_stone_bricks_processor"), StructureProcessorModule.SKELETON_DUNGEON_RUINED_STONE_BRICKS_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "skeleton_mob_spawner_processor"), StructureProcessorModule.SKELETON_MOB_SPAWNER_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "skeleton_dungeon_leg_processor"), StructureProcessorModule.SKELETON_DUNGEON_LEG_PROCESSOR);

            // Zombie dungeons
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "zombie_dungeon_cubby_processor"), StructureProcessorModule.ZOMBIE_DUNGEON_CUBBY_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "zombie_dungeon_stair_processor"), StructureProcessorModule.ZOMBIE_DUNGEON_STAIR_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "zombie_mob_spawner_processor"), StructureProcessorModule.ZOMBIE_MOB_SPAWNER_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "zombie_tombstone_spawner_processor"), StructureProcessorModule.ZOMBIE_TOMBSTONE_SPAWNER_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "zombie_main_stairs_processor"), StructureProcessorModule.ZOMBIE_MAIN_STAIRS_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "zombie_rot_processor"), StructureProcessorModule.ZOMBIE_ROT_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "zombie_dungeon_leg_processor"), StructureProcessorModule.ZOMBIE_DUNGEON_LEG_PROCESSOR);
            Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(BetterDungeonsCommon.MOD_ID, "zombie_dungeon_flower_pot_processor"), StructureProcessorModule.ZOMBIE_DUNGEON_FLOWER_POT_PROCESSOR);
        });
    }
}
