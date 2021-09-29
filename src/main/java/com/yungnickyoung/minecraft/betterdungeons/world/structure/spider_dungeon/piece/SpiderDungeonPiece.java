package com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece;

import com.google.common.collect.Sets;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;

import java.util.BitSet;
import java.util.Random;
import java.util.Set;

public abstract class SpiderDungeonPiece extends StructurePiece {
    protected static final Set<Block> BLOCK_BLACKLIST = Sets.newHashSet(Blocks.DIAMOND_BLOCK, Blocks.WHITE_WOOL, Blocks.SPAWNER, Blocks.CHEST, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.OAK_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.GRASS, Blocks.TALL_GRASS);

    protected SpiderDungeonPiece(StructurePieceType structurePieceTypeIn, int chainLength, BlockBox box) {
        super(structurePieceTypeIn, chainLength, box);
    }

    public SpiderDungeonPiece(StructurePieceType structurePierceTypeIn, NbtCompound nbt) {
        super(structurePierceTypeIn, nbt);
    }

    protected void decorateCave(StructureWorldAccess world, Random decoRand, ChunkPos chunkPos, BlockBox box, BitSet carvingMask) {
        carvingMask.stream().forEach(mask -> {
            if (decoRand.nextFloat() < .15f) { // Random chance of cobwebs along cave rim
                // Grab positional info from mask
                int x = mask & 0xF;
                int z = mask >> 4 & 0xF;
                int y = (mask >> 8) + world.getBottomY();
                int globalX = x + chunkPos.x * 16;
                int globalZ = z + chunkPos.z * 16;

                // Ensure cobweb is only placed alongside a wall
                if (this.getBlockAt(world, globalX - 1, y, globalZ, box).isOpaque()
                    || this.getBlockAt(world, globalX + 1, y, globalZ, box).isOpaque()
                    || this.getBlockAt(world, globalX, y, globalZ - 1, box).isOpaque()
                    || this.getBlockAt(world, globalX, y, globalZ + 1, box).isOpaque()
                    || this.getBlockAt(world, globalX, y - 1, globalZ, box).isOpaque()
                    || this.getBlockAt(world, globalX, y + 1, globalZ, box).isOpaque()
                ) {
                    if (!BLOCK_BLACKLIST.contains(this.getBlockAt(world, globalX, y, globalZ, box).getBlock())) {
                        this.addBlock(world, Blocks.COBWEB.getDefaultState(), globalX, y, globalZ, box);
                    }
                }
            }
        });
    }

    protected void placeSphereRandomized(StructureWorldAccess world, BlockBox box, BlockPos center, float radius, Random rand, float chance, BlockSetSelector blockSelector, boolean replaceOnlyAir) {
        for (float x = -radius; x <= radius; x++) {
            for (float z = -radius; z <= radius; z++) {
                for (float y = -radius; y <= radius; y++) {
                    if (x * x + y * y + z * z < radius * radius) {
                        if (!replaceOnlyAir || this.getBlockAt(world, (int) x + center.getX(), (int) y + center.getY(), (int) z + center.getZ(), box).isAir()) {
                            if (rand.nextFloat() < chance) {
                                this.addBlock(world, blockSelector.get(rand), (int) x + center.getX(), (int) y + center.getY(), (int) z + center.getZ(), box);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void placeSphereRandomized(StructureWorldAccess world, BlockBox box, int centerX, int centerY, int centerZ, float radius, Random rand, float chance, BlockSetSelector blockSelector, boolean replaceOnlyAir) {
        placeSphereRandomized(world, box, new BlockPos(centerX, centerY, centerZ), radius, rand, chance, blockSelector, replaceOnlyAir);
    }

    protected void setBlockState(StructureWorldAccess worldIn, Random random, BlockSetSelector selector, int x, int y, int z, BlockBox boundingboxIn) {
        BlockState blockState = selector.get(random);
        this.addBlock(worldIn, blockState, x, y, z, boundingboxIn);
    }

    protected static BlockBox getInitialBlockBox(BlockPos startPos) {
        return new BlockBox(startPos).expand(64);
    }
}
