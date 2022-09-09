package com.yungnickyoung.minecraft.betterdungeons.module;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonBigTunnelPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonEggRoomPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonNestPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonSmallTunnelPiece;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

@AutoRegister(BetterDungeonsCommon.MOD_ID)
public class StructurePieceTypeModule {
    @AutoRegister("spider_dungeon_big_tunnel_piece")
    public static StructurePieceType BIG_TUNNEL = (StructurePieceType.ContextlessType) SpiderDungeonBigTunnelPiece::new;

    @AutoRegister("spider_dungeon_nest_piece")
    public static StructurePieceType NEST = (StructurePieceType.ContextlessType) SpiderDungeonNestPiece::new;

    @AutoRegister("spider_dungeon_small_tunnel_piece")
    public static StructurePieceType SMALL_TUNNEL = (StructurePieceType.ContextlessType) SpiderDungeonSmallTunnelPiece::new;

    @AutoRegister("spider_dungeon_egg_room_piece")
    public static StructurePieceType EGG_ROOM = (StructurePieceType.ContextlessType) SpiderDungeonEggRoomPiece::new;
}
