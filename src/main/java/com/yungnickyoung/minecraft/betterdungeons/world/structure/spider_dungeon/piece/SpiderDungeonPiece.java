package com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece;

import com.google.common.collect.Sets;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

import java.util.BitSet;
import java.util.Random;
import java.util.Set;

public abstract class SpiderDungeonPiece extends StructurePiece {
    protected static final Set<Block> BLOCK_BLACKLIST = Sets.newHashSet(Blocks.DIAMOND_BLOCK, Blocks.WHITE_WOOL, Blocks.SPAWNER, Blocks.CHEST, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.OAK_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.GRASS, Blocks.TALL_GRASS);

    protected SpiderDungeonPiece(StructurePieceType structurePieceTypeIn, int chainLength, BoundingBox box) {
        super(structurePieceTypeIn, chainLength, box);
    }

    public SpiderDungeonPiece(StructurePieceType structurePierceTypeIn, CompoundTag compoundTag) {
        super(structurePierceTypeIn, compoundTag);
    }

    protected void decorateCave(WorldGenLevel world, Random decoRand, ChunkPos chunkPos, BoundingBox box, BitSet carvingMask) {
        carvingMask.stream().forEach(mask -> {
            if (decoRand.nextFloat() < .15f) { // Random chance of cobwebs along cave rim
                // Grab positional info from mask
                int x = mask & 0xF;
                int z = mask >> 4 & 0xF;
                int y = (mask >> 8) + world.getMinBuildHeight();
                int globalX = x + chunkPos.x * 16;
                int globalZ = z + chunkPos.z * 16;

                // Ensure cobweb is only placed alongside a wall.
                // canOcclude probably isn't the best function to use here, but it gets the job done
                if (this.getBlock(world, globalX - 1, y, globalZ, box).canOcclude()
                    || this.getBlock(world, globalX + 1, y, globalZ, box).canOcclude()
                    || this.getBlock(world, globalX, y, globalZ - 1, box).canOcclude()
                    || this.getBlock(world, globalX, y, globalZ + 1, box).canOcclude()
                    || this.getBlock(world, globalX, y - 1, globalZ, box).canOcclude()
                    || this.getBlock(world, globalX, y + 1, globalZ, box).canOcclude()
                ) {
                    if (!BLOCK_BLACKLIST.contains(this.getBlock(world, globalX, y, globalZ, box).getBlock())) {
                        this.placeBlock(world, Blocks.COBWEB.defaultBlockState(), globalX, y, globalZ, box);
                    }
                }
            }
        });
    }

    protected void placeSphereRandomized(WorldGenLevel world, BoundingBox box, BlockPos center, float radius, Random rand, float chance, BlockSetSelector blockSelector, boolean replaceOnlyAir) {
        for (float x = -radius; x <= radius; x++) {
            for (float z = -radius; z <= radius; z++) {
                for (float y = -radius; y <= radius; y++) {
                    if (x * x + y * y + z * z < radius * radius) {
                        if (!replaceOnlyAir || this.getBlock(world, (int) x + center.getX(), (int) y + center.getY(), (int) z + center.getZ(), box).isAir()) {
                            if (rand.nextFloat() < chance) {
                                this.placeBlock(world, blockSelector.get(rand), (int) x + center.getX(), (int) y + center.getY(), (int) z + center.getZ(), box);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void placeSphereRandomized(WorldGenLevel world, BoundingBox box, int centerX, int centerY, int centerZ, float radius, Random rand, float chance, BlockSetSelector blockSelector, boolean replaceOnlyAir) {
        placeSphereRandomized(world, box, new BlockPos(centerX, centerY, centerZ), radius, rand, chance, blockSelector, replaceOnlyAir);
    }

    protected void setBlockState(WorldGenLevel worldIn, Random random, BlockSetSelector selector, int x, int y, int z, BoundingBox boundingboxIn) {
        BlockState blockState = selector.get(random);
        this.placeBlock(worldIn, blockState, x, y, z, boundingboxIn);
    }

    protected static BoundingBox getInitialBoundingBox(BlockPos startPos) {
        return new BoundingBox(startPos).inflatedBy(64);
    }
}
