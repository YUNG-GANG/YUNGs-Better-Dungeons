package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonBigTunnelPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonEggRoomPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonNestPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonSmallTunnelPiece;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.Locale;

public class StructureFeaturePieceModuleFabric {
    public static void init() {
        StructureFeaturePieceModule.SPIDER_DUNGEON_BIG_TUNNEL_PIECE   = register("spider_dungeon_big_tunnel_piece", SpiderDungeonBigTunnelPiece::new);
        StructureFeaturePieceModule.SPIDER_DUNGEON_NEST_PIECE         = register("spider_dungeon_nest_piece", SpiderDungeonNestPiece::new);
        StructureFeaturePieceModule.SPIDER_DUNGEON_SMALL_TUNNEL_PIECE = register("spider_dungeon_small_tunnel_piece", SpiderDungeonSmallTunnelPiece::new);
        StructureFeaturePieceModule.SPIDER_DUNGEON_EGG_ROOM_PIECE     = register("spider_dungeon_egg_room_piece", SpiderDungeonEggRoomPiece::new);

    }

    private static StructurePieceType register(String name, StructurePieceType.ContextlessType structurePieceType) {
        return Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(BetterDungeonsCommon.MOD_ID, name.toLowerCase(Locale.ROOT)), structurePieceType);
    }
}
