package com.yungnickyoung.minecraft.betterdungeons.init;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonBigTunnelPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonEggRoomPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonNestPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonSmallTunnelPiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public class BDModStructurePieces {
    public static StructurePieceType SPIDER_DUNGEON_BIG_TUNNEL_PIECE;
    public static StructurePieceType SPIDER_DUNGEON_NEST_PIECE;
    public static StructurePieceType SPIDER_DUNGEON_SMALL_TUNNEL_PIECE;
    public static StructurePieceType SPIDER_DUNGEON_EGG_ROOM_PIECE;

    public static void init() {
        SPIDER_DUNGEON_BIG_TUNNEL_PIECE = registerPiece("spider_dungeon_big_tunnel_piece", SpiderDungeonBigTunnelPiece::new);
        SPIDER_DUNGEON_NEST_PIECE = registerPiece("spider_dungeon_nest_piece", SpiderDungeonNestPiece::new);
        SPIDER_DUNGEON_SMALL_TUNNEL_PIECE = registerPiece("spider_dungeon_small_tunnel_piece", SpiderDungeonSmallTunnelPiece::new);
        SPIDER_DUNGEON_EGG_ROOM_PIECE = registerPiece("spider_dungeon_egg_room_piece", SpiderDungeonEggRoomPiece::new);
    }

    private static StructurePieceType registerPiece(String name, StructurePieceType piece) {
        return Registry.register(Registry.STRUCTURE_PIECE, new Identifier(BetterDungeons.MOD_ID, name.toLowerCase(Locale.ROOT)), piece);
    }
}
