package com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModStructurePieces;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

public class SpiderDungeonEggRoomPiece extends SpiderDungeonPiece {
    private final BlockPos startPos;
    private float xRadius = 0f;
    private float yRadius = 0f;
    private float zRadius = 0f;

    private static final float X_MINRADIUS = 2, X_MAXRADIUS = 3,
                               Y_MINRADIUS = 2, Y_MAXRADIUS = 3,
                               Z_MINRADIUS = 2, Z_MAXRADIUS = 3;

    private static final BlockSetSelector WOOL_SELECTOR = BlockSetSelector.from(Blocks.WHITE_WOOL.getDefaultState());
    private static final BlockSetSelector COBWEB_SELECTOR = BlockSetSelector.from(Blocks.COBWEB.getDefaultState());

    public SpiderDungeonEggRoomPiece(BlockPos startPos, int pieceChainLength) {
        super(BDModStructurePieces.SPIDER_DUNGEON_EGG_ROOM_PIECE, pieceChainLength);
        this.boundingBox = new MutableBoundingBox(startPos.getX() - 64, 1, startPos.getZ() - 64, startPos.getX() + 64, 256, startPos.getZ() + 64);
        this.startPos = new BlockPos(startPos);
    }

    public SpiderDungeonEggRoomPiece(TemplateManager templateManager, CompoundNBT compoundNBT) {
        super(BDModStructurePieces.SPIDER_DUNGEON_EGG_ROOM_PIECE, compoundNBT);
        int[] start = compoundNBT.getIntArray("startPos");
        this.startPos = new BlockPos(start[0], start[1], start[2]);
        this.xRadius = compoundNBT.getFloat("xRadius");
        this.yRadius = compoundNBT.getFloat("yRadius");
        this.zRadius = compoundNBT.getFloat("zRadius");
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readAdditional(CompoundNBT tagCompound) {
        tagCompound.putIntArray("startPos", new int[]{startPos.getX(), startPos.getY(), startPos.getZ()});
        tagCompound.putFloat("xRadius", xRadius);
        tagCompound.putFloat("yRadius", yRadius);
        tagCompound.putFloat("zRadius", zRadius);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void buildComponent(StructurePiece piece, List<StructurePiece> pieceList, Random rand) {
        this.xRadius = rand.nextFloat() * (X_MAXRADIUS - X_MINRADIUS) + X_MINRADIUS;
        this.yRadius = rand.nextFloat() * (Y_MAXRADIUS - Y_MINRADIUS) + Y_MINRADIUS;
        this.zRadius = rand.nextFloat() * (Z_MAXRADIUS - Z_MINRADIUS) + Z_MINRADIUS;

        // Update bounding box
        this.boundingBox.minX = this.startPos.getX() - (int) this.xRadius - 4;
        this.boundingBox.maxX = this.startPos.getX() + (int) this.xRadius + 4;
        this.boundingBox.minY = this.startPos.getY() - (int) this.yRadius - 4;
        this.boundingBox.maxY = this.startPos.getY() + (int) this.yRadius + 4;
        this.boundingBox.minZ = this.startPos.getZ() - (int) this.zRadius - 4;
        this.boundingBox.maxZ = this.startPos.getZ() + (int) this.zRadius + 4;
    }

    /**
     * Generate.
     */
    @Override
    @ParametersAreNonnullByDefault
    public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random random, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        SharedSeedRandom decoRand = new SharedSeedRandom(); // Rand for decoration. It's not as important for this to be deterministic.
        decoRand.setDecorationSeed(world.getSeed(), startPos.getX(), startPos.getZ());

        // Temporary chunk-local carving mask to prevent overwriting carved blocks and add decorations
        BitSet carvingMask = new BitSet(65536);

        // Surface
        int[] surface = new int[256];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                mutable.setPos(chunkPos.getXStart() + x, 1, chunkPos.getZStart() + z);
                surface[x * 16 + z] = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, mutable).getY();
            }
        }

        // ---- Begin generating nest ---- //
        float caveStartX = startPos.getX(),
              caveStartY = startPos.getY(),
              caveStartZ = startPos.getZ();

        // Min and max values we need to consider for carving
        int minX = MathHelper.floor(caveStartX - xRadius) - chunkPos.x * 16 - 1;
        int maxX = MathHelper.floor(caveStartX + xRadius) - chunkPos.x * 16 + 1;
        int minY = MathHelper.clamp(MathHelper.floor(caveStartY - yRadius) - 1, 0, 255);
        int maxY = MathHelper.clamp(MathHelper.floor(caveStartY + yRadius) + 1, 0, 255);
        int minZ = MathHelper.floor(caveStartZ - zRadius) - chunkPos.z * 16 - 1;
        int maxZ = MathHelper.floor(caveStartZ + zRadius) - chunkPos.z * 16 + 1;

        // Clamp min/max values to ensure the coordinates are chunk-local
        minX = MathHelper.clamp(minX, 0, 15);
        maxX = MathHelper.clamp(maxX, 0, 15);
        minZ = MathHelper.clamp(minZ, 0, 15);
        maxZ = MathHelper.clamp(maxZ, 0, 15);

        // Carve out room and surround with cobblestone shell
        for (float x = minX; x <= maxX; x++) {
            // Get global coordinate
            int globalX = (int)x + chunkPos.x * 16;

            // No need to consider blocks outside this chunk
            if (globalX < chunkPos.getXStart() || globalX > chunkPos.getXEnd()) continue;

            // Distance along the rotated x-axis from the center of this ellipsoid.
            // You can think of this value as (x/a), where a is the length of the ellipsoid's radius in the rotated x-direction.
            float radialXDist = (globalX - caveStartX + .5f) / xRadius;

            for (float z = minZ; z <= maxZ; z++) {
                // Get global coordinate
                int globalZ = (int)z + chunkPos.z * 16;

                // No need to consider blocks outside this chunk
                if (globalZ < chunkPos.getZStart() || globalZ > chunkPos.getZEnd()) continue;

                // Distance along the rotated z-axis from the center of this ellipsoid.
                // You can think of this value as (z/b), where b is the length of the ellipsoid's radius in the rotated z-direction.
                float radialZDist = (globalZ - caveStartZ + .5f) / zRadius;

                for (float y = minY; y <= maxY; y++) {
                    int globalY = (int)y; // y-coord does not require any conversion

                    // Don't go above surface
                    if (globalY > surface[((int)(x) % 16) * 16 + ((int)(z) % 16)]) break;

                    // Distance along the y-axis from the center of this ellipsoid.
                    // You can think of this value as (y/c), where c is the length of the ellipsoid's radius in the y-direction.
                    float radialYDist = (y - caveStartY - .5f) / yRadius;

                    // Calculate the carving mask for this block
                    int mask = (int)x | (int)z << 4 | ((int)(y)) << 8;

                    // Carve out blocks within the ellipsoid. Blocks immediately outside the ellipsoid will be turned into a cobblestone shell.
                    float radialDist = radialXDist * radialXDist + radialYDist * radialYDist + radialZDist * radialZDist;
                    if (radialDist < 1.0) {
                        if (!carvingMask.get(mask)) {
                            if (!BLOCK_BLACKLIST.contains(this.getBlockStateFromPos(world, globalX, globalY, globalZ, box).getBlock())) {
                                this.setBlockState(world, Blocks.CAVE_AIR.getDefaultState(), globalX, globalY, globalZ, box);
                                carvingMask.set(mask);
                            }
                        }
                    } else {
                        // Calculate radial distances for each of the three axes (same logic as above), but for a slightly enlarged ellipsoid.
                        // This will be used to give the cave an outer cobblestone shell.
                        float radialXDistShell = (globalX - caveStartX + .5f) / (xRadius + 1.2f);
                        float radialYDistShell = (y - caveStartY - .5f) / (yRadius + 1.2f);
                        float radialZDistShell = (globalZ - caveStartZ + .5f) / (zRadius + 1.2f);
                        float radialDistShell = radialXDistShell * radialXDistShell + radialYDistShell * radialYDistShell + radialZDistShell * radialZDistShell;
                        if (radialDistShell < 1.0 && !carvingMask.get(mask)) {
                            BlockState state = this.getBlockStateFromPos(world, globalX, globalY, globalZ, box);
                            // Make sure block is not blacklisted AND not air.
                            // The check for air ensures the shells will not block off the connecting tunnels,
                            // but as a result they could get destroyed by cave gen
                            if (!BLOCK_BLACKLIST.contains(state.getBlock()) && state.getMaterial() != Material.AIR) {
                                if (state.isAir() || state.getFluidState().getFluid() != Fluids.EMPTY || decoRand.nextFloat() < .8f) {
                                    this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), globalX, globalY, globalZ, box);
                                }
                            }
                        }
                    }
                }
            }
        }

        BlockPos chestPos = new BlockPos((int) caveStartX, (int) caveStartY - (int) yRadius + 1, (int) caveStartZ);

        // Place wool egg
        this.placeSphereRandomized(world, box, chestPos, 2, decoRand, .5f, WOOL_SELECTOR, false);

        // Guarantee wool immediately around chest
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), chestPos.getX() + 1, chestPos.getY(), chestPos.getZ(), box);
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), chestPos.getX() - 1, chestPos.getY(), chestPos.getZ(), box);
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), chestPos.getX(), chestPos.getY(), chestPos.getZ() + 1, box);
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), chestPos.getX(), chestPos.getY(), chestPos.getZ() - 1, box);
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), chestPos.getX(), chestPos.getY() - 1, chestPos.getZ(), box);
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), chestPos.getX(), chestPos.getY() + 1, chestPos.getZ(), box);

        // Surround egg with more cobweb
        this.placeSphereRandomized(world, box, chestPos.getX(), chestPos.getY(), chestPos.getZ(), 2, decoRand, .4f, COBWEB_SELECTOR, true);

        // Place chest or spawner
        if (random.nextFloat() < .75f) {
            this.generateChest(world, box, random, chestPos.getX(), chestPos.getY(), chestPos.getZ(), LootTables.CHESTS_SIMPLE_DUNGEON); // TODO - custom loot
        } else {
            if (box.isVecInside(chestPos)) {
                this.setBlockState(world, Blocks.SPAWNER.getDefaultState(), chestPos.getX(), chestPos.getY(), chestPos.getZ(), box);
                TileEntity spawnerTileEntity = world.getTileEntity(chestPos);
                if (spawnerTileEntity instanceof MobSpawnerTileEntity) {
                    ((MobSpawnerTileEntity) spawnerTileEntity).getSpawnerBaseLogic().setEntityType(EntityType.SPIDER);
                } else {
                    BetterDungeons.LOGGER.warn("Expected spider spawner entity at {}, but found none!", chestPos);
                }
            }
        }

        decorateCave(world, decoRand, chunkPos, box, carvingMask);

        return true;
    }
}
