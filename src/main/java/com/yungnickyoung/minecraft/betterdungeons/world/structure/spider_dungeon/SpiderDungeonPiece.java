package com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon;

import com.google.common.collect.Sets;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;

import java.util.BitSet;
import java.util.Random;
import java.util.Set;

public abstract class SpiderDungeonPiece extends StructurePiece {
    protected static final Set<Block> BLOCK_BLACKLIST = Sets.newHashSet(Blocks.DIAMOND_BLOCK, Blocks.WHITE_WOOL, Blocks.SPAWNER, Blocks.CHEST, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.OAK_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.GRASS, Blocks.TALL_GRASS);

    protected SpiderDungeonPiece(IStructurePieceType structurePieceTypeIn, int componentTypeIn) {
        super(structurePieceTypeIn, componentTypeIn);
    }

    public SpiderDungeonPiece(IStructurePieceType structurePierceTypeIn, CompoundNBT nbt) {
        super(structurePierceTypeIn, nbt);
    }

    protected void decorateCave(ISeedReader world, Random decoRand, ChunkPos chunkPos, MutableBoundingBox box, BitSet carvingMask) {
        carvingMask.stream().forEach(mask -> {
            if (decoRand.nextFloat() < .15f) { // Random chance of cobwebs along cave rim
                // Grab positional info from mask
                int x = mask & 0xF;
                int z = mask >> 4 & 0xF;
                int y = mask >> 8 & 0xFF;
                int globalX = x + chunkPos.x * 16;
                int globalZ = z + chunkPos.z * 16;

                // Ensure cobweb is only placed alongside a wall
                if (this.getBlockStateFromPos(world, globalX - 1, y, globalZ, box).isSolid()
                    || this.getBlockStateFromPos(world, globalX + 1, y, globalZ, box).isSolid()
                    || this.getBlockStateFromPos(world, globalX, y, globalZ - 1, box).isSolid()
                    || this.getBlockStateFromPos(world, globalX, y, globalZ + 1, box).isSolid()
                    || this.getBlockStateFromPos(world, globalX, y - 1, globalZ, box).isSolid()
                    || this.getBlockStateFromPos(world, globalX, y + 1, globalZ, box).isSolid()
                ) {
                    if (!BLOCK_BLACKLIST.contains(this.getBlockStateFromPos(world, globalX, y, globalZ, box).getBlock())) {
                        this.setBlockState(world, Blocks.COBWEB.getDefaultState(), globalX, y, globalZ, box);
                    }
                }
            }
        });
    }

    protected void placeSphereRandomized(ISeedReader world, MutableBoundingBox box, BlockPos center, float radius, Random rand, float chance, BlockSetSelector blockSelector, boolean replaceOnlyAir) {
        for (float x = -radius; x <= radius; x++) {
            for (float z = -radius; z <= radius; z++) {
                for (float y = -radius; y <= radius; y++) {
                    if (x * x + y * y + z * z < radius * radius) {
                        if (!replaceOnlyAir || this.getBlockStateFromPos(world, (int) x + center.getX(), (int) y + center.getY(), (int) z + center.getZ(), box).isAir()) {
                            if (rand.nextFloat() < chance) {
                                this.setBlockState(world, blockSelector.get(rand), (int) x + center.getX(), (int) y + center.getY(), (int) z + center.getZ(), box);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void placeSphereRandomized(ISeedReader world, MutableBoundingBox box, int centerX, int centerY, int centerZ, float radius, Random rand, float chance, BlockSetSelector blockSelector, boolean replaceOnlyAir) {
        placeSphereRandomized(world, box, new BlockPos(centerX, centerY, centerZ), radius, rand, chance, blockSelector, replaceOnlyAir);
    }
}
