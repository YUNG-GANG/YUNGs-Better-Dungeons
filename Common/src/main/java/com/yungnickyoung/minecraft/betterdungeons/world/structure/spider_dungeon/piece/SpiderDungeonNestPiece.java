package com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeonsCommon;
import com.yungnickyoung.minecraft.betterdungeons.mixin.accessor.BoundingBoxAccessor;
import com.yungnickyoung.minecraft.betterdungeons.module.StructurePieceTypeModule;
import com.yungnickyoung.minecraft.yungsapi.api.world.randomize.BlockStateRandomizer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.BitSet;

@ParametersAreNonnullByDefault
public class SpiderDungeonNestPiece extends SpiderDungeonPiece {
    private final BlockPos startPos;
    private float xRadius = 0f;
    private float yRadius = 0f;
    private float zRadius = 0f;

    private static final float X_MINRADIUS = 6, X_MAXRADIUS = 10,
                               Y_MINRADIUS = 4, Y_MAXRADIUS = 6,
                               Z_MINRADIUS = 6, Z_MAXRADIUS = 10;

    private static final BlockStateRandomizer COBWEB_SELECTOR = BlockStateRandomizer.from(Blocks.COBWEB.defaultBlockState());
    private static final BlockStateRandomizer WOOL_SELECTOR = BlockStateRandomizer.from(Blocks.WHITE_WOOL.defaultBlockState());

    public SpiderDungeonNestPiece(BlockPos startPos, int pieceChainLength) {
        super(StructurePieceTypeModule.NEST, pieceChainLength, getInitialBoundingBox(startPos));
        this.startPos = new BlockPos(startPos.getX(), startPos.getY(), startPos.getZ());
    }

    /**
     * Constructor for loading from NBT.
     */
    public SpiderDungeonNestPiece(CompoundTag compoundTag) {
        super(StructurePieceTypeModule.NEST, compoundTag);
        int[] start = compoundTag.getIntArray("startPos");
        this.startPos = new BlockPos(start[0], start[1], start[2]);
        this.xRadius = compoundTag.getFloat("xRadius");
        this.yRadius = compoundTag.getFloat("yRadius");
        this.zRadius = compoundTag.getFloat("zRadius");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        compoundTag.putIntArray("startPos", new int[]{startPos.getX(), startPos.getY(), startPos.getZ()});
        compoundTag.putFloat("xRadius", xRadius);
        compoundTag.putFloat("yRadius", yRadius);
        compoundTag.putFloat("zRadius", zRadius);
    }

    @Override
    public void addChildren(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, RandomSource randomSource) {
        this.xRadius = randomSource.nextFloat() * (X_MAXRADIUS - X_MINRADIUS) + X_MINRADIUS;
        this.yRadius = randomSource.nextFloat() * (Y_MAXRADIUS - Y_MINRADIUS) + Y_MINRADIUS;
        this.zRadius = randomSource.nextFloat() * (Z_MAXRADIUS - Z_MINRADIUS) + Z_MINRADIUS;

        // Update bounding box
        ((BoundingBoxAccessor)this.boundingBox).setMinX(this.startPos.getX() - (int) this.xRadius - 4);
        ((BoundingBoxAccessor)this.boundingBox).setMaxX(this.startPos.getX() + (int) this.xRadius + 4);
        ((BoundingBoxAccessor)this.boundingBox).setMinY(this.startPos.getY() - (int) this.yRadius - 4);
        ((BoundingBoxAccessor)this.boundingBox).setMaxY(this.startPos.getY() + (int) this.yRadius + 4);
        ((BoundingBoxAccessor)this.boundingBox).setMinZ(this.startPos.getZ() - (int) this.zRadius - 4);
        ((BoundingBoxAccessor)this.boundingBox).setMaxZ(this.startPos.getZ() + (int) this.zRadius + 4);
    }

    /**
     * Generate.
     */
    @Override
    public void postProcess(WorldGenLevel world, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        WorldgenRandom decoRand = new WorldgenRandom(new LegacyRandomSource(0)); // Rand for decoration. It's not as important for this to be deterministic.
        decoRand.setLargeFeatureSeed(world.getSeed(), startPos.getX(), startPos.getZ());

        // Temporary chunk-local carving mask to prevent overwriting carved blocks and add decorations
        int xBits = 4;
        int zBits = 4;
        int yBits = Mth.ceillog2(world.getMaxBuildHeight() - world.getMinBuildHeight());
        BitSet carvingMask = new BitSet((int) Math.pow(2, xBits + zBits + yBits));

        // Create shell selector ahead of time to avoid redundant initialization
        BlockStateRandomizer shellSelector = new BlockStateRandomizer(Blocks.COBBLESTONE.defaultBlockState());

        // Surface
        int[] surface = new int[256];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                mutable.set(chunkPos.getMinBlockX() + x, 1, chunkPos.getMinBlockZ() + z);
                surface[x * 16 + z] = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, mutable.getX(), mutable.getZ());
            }
        }

        // ---- Begin generating nest ---- //
        float caveStartX = startPos.getX(),
              caveStartY = startPos.getY(),
              caveStartZ = startPos.getZ();

        // Min and max values we need to consider for carving
        int minX = Mth.floor(caveStartX - xRadius) - chunkPos.x * 16 - 1;
        int maxX = Mth.floor(caveStartX + xRadius) - chunkPos.x * 16 + 1;
        int minY = Mth.clamp(Mth.floor(caveStartY - yRadius) - 1, world.getMinBuildHeight(), world.getMaxBuildHeight());
        int maxY = Mth.clamp(Mth.floor(caveStartY + yRadius) + 1, world.getMinBuildHeight(), world.getMaxBuildHeight());
        int minZ = Mth.floor(caveStartZ - zRadius) - chunkPos.z * 16 - 1;
        int maxZ = Mth.floor(caveStartZ + zRadius) - chunkPos.z * 16 + 1;

        // Clamp min/max values to ensure the coordinates are chunk-local
        minX = Mth.clamp(minX, 0, 15);
        maxX = Mth.clamp(maxX, 0, 15);
        minZ = Mth.clamp(minZ, 0, 15);
        maxZ = Mth.clamp(maxZ, 0, 15);

        // Carve out room and surround with cobblestone shell
        for (float x = minX; x <= maxX; x++) {
            // Get global coordinate
            int globalX = (int)x + chunkPos.x * 16;

            // No need to consider blocks outside this chunk
            if (globalX < chunkPos.getMinBlockX() || globalX > chunkPos.getMaxBlockX()) continue;

            // Distance along the rotated x-axis from the center of this ellipsoid.
            // You can think of this value as (x/a), where a is the length of the ellipsoid's radius in the rotated x-direction.
            float radialXDist = (globalX - caveStartX + .5f) / xRadius;

            for (float z = minZ; z <= maxZ; z++) {
                // Get global coordinate
                int globalZ = (int)z + chunkPos.z * 16;

                // No need to consider blocks outside this chunk
                if (globalZ < chunkPos.getMinBlockZ() || globalZ > chunkPos.getMaxBlockZ()) continue;

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
                    int mask = (int)x | (int)z << 4 | ((int)(y - world.getMinBuildHeight())) << 8;

                    // Carve out blocks within the ellipsoid. Blocks immediately outside the ellipsoid will be turned into a cobblestone shell.
                    float radialDist = radialXDist * radialXDist + radialYDist * radialYDist + radialZDist * radialZDist;
                    if (radialDist < 1.0) {
                        if (globalX == caveStartX && globalZ == caveStartZ && globalY > caveStartY) {
                            this.placeBlock(world, Blocks.WHITE_WOOL.defaultBlockState(), globalX, globalY, globalZ, box);
                        } else if (!carvingMask.get(mask)) {
                            if (!BLOCK_BLACKLIST.contains(this.getBlock(world, globalX, globalY, globalZ, box).getBlock())) {
                                this.placeBlock(world, Blocks.CAVE_AIR.defaultBlockState(), globalX, globalY, globalZ, box);
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
                        if (radialDistShell < 1.0) {
                            if (globalX == caveStartX && globalZ == caveStartZ && globalY > caveStartY) { // Guarantee wool up to ceiling
                                this.placeBlock(world, Blocks.WHITE_WOOL.defaultBlockState(), globalX, globalY, globalZ, box);
                            } else if (!carvingMask.get(mask)) { // Only place cobble shell on outer rim
                                BlockState state = this.getBlock(world, globalX, globalY, globalZ, box);
//                                if (!BLOCK_BLACKLIST.contains(state.getBlock())) { // Ignore blacklisted blocks
//                                    if (y <= minY + 3 || y >= maxY) { // Force generation of floor and ceiling.
//                                        // We use a selector with a chance of cobweb in order to expose the openings to big tunnels
//                                        this.setBlockState(world, decoRand, SHELL_SELECTOR, globalX, globalY, globalZ, box);
//                                    } else if ((state.getMaterial() != Material.AIR)) { // Block must be non-air OR part of floor or ceiling
//                                        this.setBlockState(world, Blocks.COBBLESTONE.defaultBlockState()(), globalX, globalY, globalZ, box);
//                                    }
//                                }
                                if (!BLOCK_BLACKLIST.contains(state.getBlock()) && state.isAir()) { // Ignore blacklisted blocks and air
                                    if (state.getFluidState().getType() != Fluids.EMPTY || decoRand.nextFloat() < .8f) {
                                        this.placeBlock(world, shellSelector.get(decoRand), globalX, globalY, globalZ, box);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Place wool cocoon
        this.placeSphereRandomized(world, box, (int) caveStartX, (int) caveStartY + 1, (int) caveStartZ, 2, decoRand, .5f, WOOL_SELECTOR, true);

        // Guarantee wool immediately around spawner
        this.placeBlock(world, Blocks.WHITE_WOOL.defaultBlockState(), (int) caveStartX + 1, (int) caveStartY + 1, (int) caveStartZ, box);
        this.placeBlock(world, Blocks.WHITE_WOOL.defaultBlockState(), (int) caveStartX - 1, (int) caveStartY + 1, (int) caveStartZ, box);
        this.placeBlock(world, Blocks.WHITE_WOOL.defaultBlockState(), (int) caveStartX, (int) caveStartY + 1, (int) caveStartZ + 1, box);
        this.placeBlock(world, Blocks.WHITE_WOOL.defaultBlockState(), (int) caveStartX, (int) caveStartY + 1, (int) caveStartZ - 1, box);
        this.placeBlock(world, Blocks.WHITE_WOOL.defaultBlockState(), (int) caveStartX, (int) caveStartY, (int) caveStartZ, box);

        // Surround cocoon with more cobweb
        this.placeSphereRandomized(world, box, (int) caveStartX, (int) caveStartY + 1, (int) caveStartZ, 3, decoRand, .5f, COBWEB_SELECTOR, true);

        // Place spawner
        this.placeBlock(world, Blocks.SPAWNER.defaultBlockState(), (int) caveStartX, (int) caveStartY + 1, (int) caveStartZ, box);
        if (box.isInside(startPos)) {
            BlockEntity spawnerTileEntity = world.getBlockEntity(startPos.relative(Direction.UP));
            if (spawnerTileEntity instanceof SpawnerBlockEntity) {
                ((SpawnerBlockEntity) spawnerTileEntity).setEntityId(EntityType.CAVE_SPIDER, randomSource);
            } else {
                BetterDungeonsCommon.LOGGER.warn("Expected cave spider spawner entity at {}, but found none!", startPos.relative(Direction.UP));
            }
        }

        decorateCave(world, decoRand, chunkPos, box, carvingMask);
    }
}
