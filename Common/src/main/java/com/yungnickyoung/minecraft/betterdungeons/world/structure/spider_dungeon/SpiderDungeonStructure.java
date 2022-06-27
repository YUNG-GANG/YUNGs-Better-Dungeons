package com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.betterdungeons.module.StructureTypeModule;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonBigTunnelPiece;
import com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece.SpiderDungeonPiece;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class SpiderDungeonStructure extends Structure {
    public static final Codec<SpiderDungeonStructure> CODEC = RecordCodecBuilder.create((builder) -> builder
                    .group(
                            settingsCodec(builder),
                            HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight))
                    .apply(builder, SpiderDungeonStructure::new));
    private final HeightProvider startHeight;

    public SpiderDungeonStructure(StructureSettings structureSettings, HeightProvider startHeight) {
        super(structureSettings);
        this.startHeight = startHeight;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        int startX = context.chunkPos().getMiddleBlockX();
        int startZ = context.chunkPos().getMiddleBlockZ();
        int startY = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos startPos = new BlockPos(startX, startY, startZ);

        // Spider dungeons use traditional code-based structure gen instead of Jigsaw
        SpiderDungeonPiece startPiece = new SpiderDungeonBigTunnelPiece(startPos);
        StructurePiecesBuilder structurePiecesBuilder = new StructurePiecesBuilder();
        structurePiecesBuilder.addPiece(startPiece);

        // Build room component. This also populates the children list, effectively building the entire mineshaft.
        // Note that no blocks are actually placed yet.
        startPiece.addChildren(startPiece, structurePiecesBuilder, context.random());
        return Optional.of(new GenerationStub(startPos, Either.right(structurePiecesBuilder)));
    }

    @Override
    public StructureType<?> type() {
        return StructureTypeModule.SPIDER_DUNGEON;
    }
}
