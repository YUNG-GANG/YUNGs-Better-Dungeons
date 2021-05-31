package com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon;

import com.google.common.collect.Sets;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModStructurePieces;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
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
import java.util.Set;

public class SpiderDungeonPiece extends StructurePiece {
    private final BlockPos cornerPos;
    private final BlockPos surfaceStartPos;

    private static final Set<Block> BLOCK_BLACKLIST = Sets.newHashSet(Blocks.DIAMOND_BLOCK, Blocks.WHITE_WOOL, Blocks.SPAWNER, Blocks.CHEST);

    public SpiderDungeonPiece(BlockPos centerPos) {
        super(BDModStructurePieces.SPIDER_DUNGEON_PIECE, 0);
        this.boundingBox = new MutableBoundingBox(centerPos.getX() - 64, 1, centerPos.getZ() - 64, centerPos.getX() + 64, 256, centerPos.getZ() + 64);
        this.cornerPos = new BlockPos(centerPos.getX() - 64, 1, centerPos.getZ() - 64);
        this.surfaceStartPos = new BlockPos(centerPos.getX(), 70, centerPos.getZ());
    }

    public SpiderDungeonPiece(TemplateManager templateManager, CompoundNBT compoundNBT) {
        super(BDModStructurePieces.SPIDER_DUNGEON_PIECE, compoundNBT);
        int[] corner = compoundNBT.getIntArray("cornerPos");
        int[] surface = compoundNBT.getIntArray("surfaceStartPos");
        this.cornerPos = new BlockPos(corner[0], corner[1], corner[2]);
        this.surfaceStartPos = new BlockPos(surface[0], surface[1], surface[2]);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readAdditional(CompoundNBT tagCompound) {
        tagCompound.putIntArray("cornerPos", new int[]{cornerPos.getX(), cornerPos.getY(), cornerPos.getZ()});
        tagCompound.putIntArray("surfaceStartPos", new int[]{surfaceStartPos.getX(), surfaceStartPos.getY(), surfaceStartPos.getZ()});
    }

    @Override
    @ParametersAreNonnullByDefault
    public void buildComponent(StructurePiece componentIn, List<StructurePiece> listIn, Random rand) {
    }


    /**
     * Generate.
     */
    @Override
    @ParametersAreNonnullByDefault
    public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random random, MutableBoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        SharedSeedRandom featureRand = new SharedSeedRandom(); // Rand for large-scale feature formation. Must be deterministic.
        SharedSeedRandom decoRand = new SharedSeedRandom(); // Rand for decoration. It's not as important for this to be deterministic.
        featureRand.setLargeFeatureSeed(world.getSeed(), surfaceStartPos.getX(), surfaceStartPos.getZ());
        decoRand.setDecorationSeed(world.getSeed(), surfaceStartPos.getX(), surfaceStartPos.getZ());

        // Temporary chunk-local carving mask to prevent overwriting carved blocks
        BitSet carvingMask = new BitSet(65536);

        // Surface
        int[] surface = new int[256];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                mutable.setPos(chunkPos.getXStart() + x, 1, chunkPos.getZStart() + z);
                surface[x * 16 + z] = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, mutable).getY();
            }
        }

        // Generate first main tunnel and attached room w/ small tunnels
        CaveStart smallCaveStart = generateBigTunnel(world, featureRand, decoRand, chunkPos, box, surface, carvingMask, new BlockPos(surfaceStartPos));
        smallCaveStart.angle += 0.3f; // Offset angle a bit before spawning small tunnels to avoid overlap w/ main tunnel
        generateSmallTunnel(world, featureRand, decoRand, chunkPos, box, surface, carvingMask, smallCaveStart.startPos, smallCaveStart);
        generateSmallTunnel(world, featureRand, decoRand, chunkPos, box, surface, carvingMask, smallCaveStart.startPos, smallCaveStart);
        generateSmallTunnel(world, featureRand, decoRand, chunkPos, box, surface, carvingMask, smallCaveStart.startPos, smallCaveStart);
        if (featureRand.nextFloat() < .5f) // 4th small tunnel is not guaranteed
            generateSmallTunnel(world, featureRand, decoRand, chunkPos, box, surface, carvingMask, smallCaveStart.startPos, smallCaveStart);
        generateNest(world, featureRand, decoRand, chunkPos, box, surface, carvingMask, smallCaveStart.startPos);

        // Generate second main tunnel and attached room w/ small tunnels
        smallCaveStart = generateBigTunnel(world, featureRand, decoRand, chunkPos, box, surface, carvingMask, smallCaveStart.startPos);
        smallCaveStart.angle += 0.3f; // Offset angle a bit before spawning small tunnels to avoid overlap w/ main tunnel
        generateSmallTunnel(world, featureRand, decoRand, chunkPos, box, surface, carvingMask, smallCaveStart.startPos, smallCaveStart);
        generateSmallTunnel(world, featureRand, decoRand, chunkPos, box, surface, carvingMask, smallCaveStart.startPos, smallCaveStart);
        generateSmallTunnel(world, featureRand, decoRand, chunkPos, box, surface, carvingMask, smallCaveStart.startPos, smallCaveStart);
        if (featureRand.nextFloat() < .5f) // 4th small tunnel is not guaranteed
            generateSmallTunnel(world, featureRand, decoRand, chunkPos, box, surface, carvingMask, smallCaveStart.startPos, smallCaveStart);
        generateNest(world, featureRand, decoRand, chunkPos, box, surface, carvingMask, smallCaveStart.startPos);

        decorateCave(world, decoRand, chunkPos, box, carvingMask);

        return true;
    }

    private CaveStart generateBigTunnel(ISeedReader world, Random featureRand, Random decoRand, ChunkPos chunkPos, MutableBoundingBox box, int[] surface, BitSet carvingMask, BlockPos startPos) {
        int length = 30;

        float xMinRadius = 2, xMaxRadius = 2.5f,
              yMinRadius = 2, yMaxRadius = 2.5f,
              zMinRadius = 2, zMaxRadius = 2.5f;

        float caveStartX = startPos.getX(),
              caveStartY = startPos.getY(),
              caveStartZ = startPos.getZ();

        float pitch = MathHelper.clamp(featureRand.nextFloat() * ((float) -Math.PI), -2.6f, -.6f),
              yaw = featureRand.nextFloat() * ((float) Math.PI * 2F);

        float yawModifier = 0f;

        // Don't let cave go straight down
        if (pitch > -2.2f && pitch < -1.0f) pitch = -2.2f;

        for (int i = 0; i < length; i++) {
            float pitchY = MathHelper.sin(pitch);
            float pitchXZ = MathHelper.cos(pitch); // allows for steep drops

            // Center of this sphere
            caveStartX += MathHelper.cos(yaw) * pitchXZ;
            caveStartY += MathHelper.sin(pitchY);
            caveStartZ += MathHelper.sin(yaw) * pitchXZ;

            // Tweak yaw for next iteration
            yaw += yawModifier * 0.01f;
            yawModifier = yawModifier * 0.75F;
            yawModifier += featureRand.nextFloat() * featureRand.nextFloat();

            // Vary the size of the tunnel such that it is widest in the middle and smallest at the end
            float xRadius = MathHelper.lerp(MathHelper.sin((float)(i) * (float) Math.PI / length), xMinRadius, xMaxRadius);
            float yRadius = MathHelper.lerp(MathHelper.sin((float)(i) * (float) Math.PI / length), yMinRadius, yMaxRadius);
            float zRadius = MathHelper.lerp(MathHelper.sin((float)(i) * (float) Math.PI / length), zMinRadius, zMaxRadius);

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

            // DEBUG
//            this.setBlockState(world, Blocks.DIAMOND_BLOCK.getDefaultState(), (int) caveStartX, (int) caveStartY, (int) caveStartZ, box);

            // -- Carve sphere -- //
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
                        if (!carvingMask.get(mask) && radialDist < 1.0) {
                            if (!BLOCK_BLACKLIST.contains(this.getBlockStateFromPos(world, globalX, globalY, globalZ, box).getBlock())) {
                                this.setBlockState(world, Blocks.CAVE_AIR.getDefaultState(), globalX, globalY, globalZ, box);
                                carvingMask.set(mask);
                            }
                        } else {
                            // Calculate radial distances for each of the three axes (same logic as above), but for a slightly enlarged ellipsoid.
                            // This will be used to give the cave an outer cobblestone shell.
                            float radialXDistShell = (globalX - caveStartX + .5f) / (xRadius + 1.2f);
                            float radialYDistShell = (y - caveStartY - .5f) / (yRadius + 1.2f);
                            float radialZDistShell = (globalZ - caveStartZ + .5f) / (zRadius + 1.2f);
                            float radialDistShell = radialXDistShell * radialXDistShell + radialYDistShell * radialYDistShell + radialZDistShell * radialZDistShell;
                            if (!carvingMask.get(mask) && radialDistShell < 1.0) {
                                BlockState state = this.getBlockStateFromPos(world, globalX, globalY, globalZ, box);
                                if (!BLOCK_BLACKLIST.contains(state.getBlock())) {
                                    if (state.isAir() || state.getFluidState().getFluid() != Fluids.EMPTY || decoRand.nextFloat() < .2f) {
                                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), globalX, globalY, globalZ, box);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Return final center position (end of this tunnel)
        return new CaveStart(
            new BlockPos(caveStartX, caveStartY, caveStartZ),
            yaw
        );
    }

    private void generateSmallTunnel(ISeedReader world, Random featureRand, Random decoRand, ChunkPos chunkPos, MutableBoundingBox box, int[] surface, BitSet carvingMask, BlockPos startPos, CaveStart caveStart) {
        int length = 30;

        float xMinRadius = 1, xMaxRadius = 1.5f,
              yMinRadius = 2, yMaxRadius = 2,
              zMinRadius = 1, zMaxRadius = 1.5f;

        float caveStartX = startPos.getX(),
              caveStartY = startPos.getY(),
              caveStartZ = startPos.getZ();

        caveStart.angle += featureRand.nextFloat() * .52f + 0.9f;

        float pitch = featureRand.nextFloat() * (float) Math.PI / 3f - ((float) Math.PI / 6f),
              yaw = caveStart.angle;

        float yawModifier = 0f;

        for (int i = 0; i < length; i++) {
            float pitchY = MathHelper.sin(pitch);
            float pitchXZ = MathHelper.cos(pitch); // allows for steep drops

            // Center of this sphere
            caveStartX += MathHelper.cos(yaw) * pitchXZ;
            caveStartY += MathHelper.sin(pitchY);
            caveStartZ += MathHelper.sin(yaw) * pitchXZ;

            // Tweak yaw for next iteration
            yaw += yawModifier * 0.02f;
            yawModifier = yawModifier * 0.75F;
            yawModifier += featureRand.nextFloat() * featureRand.nextFloat();

            // Vary the size of the tunnel such that it is widest in the middle and smallest at the end
            float xRadius = MathHelper.lerp(MathHelper.sin((float)(i) * (float) Math.PI / length), xMinRadius, xMaxRadius);
            float yRadius = MathHelper.lerp(MathHelper.sin((float)(i) * (float) Math.PI / length), yMinRadius, yMaxRadius);
            float zRadius = MathHelper.lerp(MathHelper.sin((float)(i) * (float) Math.PI / length), zMinRadius, zMaxRadius);

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

            // DEBUG
//            this.setBlockState(world, Blocks.DIAMOND_BLOCK.getDefaultState(), (int) caveStartX, (int) caveStartY, (int) caveStartZ, box);

            // -- Carve sphere -- //
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
                        if (!carvingMask.get(mask) && radialDist < 1.0) {
                            if (!BLOCK_BLACKLIST.contains(this.getBlockStateFromPos(world, globalX, globalY, globalZ, box).getBlock())) {
                                this.setBlockState(world, Blocks.CAVE_AIR.getDefaultState(), globalX, globalY, globalZ, box);
                                carvingMask.set(mask);
                            }
                        } else {
                            // Calculate radial distances for each of the three axes (same logic as above), but for a slightly enlarged ellipsoid.
                            // This will be used to give the cave an outer cobblestone shell.
                            float radialXDistShell = (globalX - caveStartX + .5f) / (xRadius + 1.2f);
                            float radialYDistShell = (y - caveStartY - .5f) / (yRadius + 1.2f);
                            float radialZDistShell = (globalZ - caveStartZ + .5f) / (zRadius + 1.2f);
                            float radialDistShell = radialXDistShell * radialXDistShell + radialYDistShell * radialYDistShell + radialZDistShell * radialZDistShell;
                            if (!carvingMask.get(mask) && radialDistShell < 1.0) {
                                BlockState state = this.getBlockStateFromPos(world, globalX, globalY, globalZ, box);
                                if (!BLOCK_BLACKLIST.contains(state.getBlock())) {
                                    if (state.isAir() || state.getFluidState().getFluid() != Fluids.EMPTY || decoRand.nextFloat() < .4f) {
                                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), globalX, globalY, globalZ, box);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (featureRand.nextFloat() < .8f) // Chance of egg room
            generateEggRoom(world, featureRand, decoRand, chunkPos, box, surface, carvingMask, new BlockPos(caveStartX, caveStartY, caveStartZ));
    }

    private void generateNest(ISeedReader world, Random featureRand, Random decoRand, ChunkPos chunkPos, MutableBoundingBox box, int[] surface, BitSet carvingMask, BlockPos startPos) {
        float xMinRadius = 6, xMaxRadius = 10,
              yMinRadius = 4, yMaxRadius = 6,
              zMinRadius = 6, zMaxRadius = 10;

        float xRadius = featureRand.nextFloat() * (xMaxRadius - xMinRadius) + xMinRadius;
        float yRadius = featureRand.nextFloat() * (yMaxRadius - yMinRadius) + yMinRadius;
        float zRadius = featureRand.nextFloat() * (zMaxRadius - zMinRadius) + zMinRadius;

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
                        if (globalX == caveStartX && globalZ == caveStartZ && globalY > caveStartY) {
                            this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), globalX, globalY, globalZ, box);
                        } else if (!carvingMask.get(mask)) {
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
                        if (radialDistShell < 1.0) {
                            if (globalX == caveStartX && globalZ == caveStartZ && globalY > caveStartY) {
                                this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), globalX, globalY, globalZ, box);
                            } else if (!carvingMask.get(mask)) {
                                BlockState state = this.getBlockStateFromPos(world, globalX, globalY, globalZ, box);
                                if (!BLOCK_BLACKLIST.contains(state.getBlock())) {
                                    if (state.isAir() || state.getFluidState().getFluid() != Fluids.EMPTY || decoRand.nextFloat() < .8f) {
                                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), globalX, globalY, globalZ, box);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Place wool cocoon
        this.placeSphereRandomized(world, box, (int) caveStartX, (int) caveStartY + 1, (int) caveStartZ, 2, decoRand, .5f, BlockSetSelector.from(Blocks.WHITE_WOOL.getDefaultState()), true);

        // Guarantee wool immediately around spawner
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), (int) caveStartX + 1, (int) caveStartY + 1, (int) caveStartZ, box);
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), (int) caveStartX - 1, (int) caveStartY + 1, (int) caveStartZ, box);
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), (int) caveStartX, (int) caveStartY + 1, (int) caveStartZ + 1, box);
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), (int) caveStartX, (int) caveStartY + 1, (int) caveStartZ - 1, box);
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), (int) caveStartX, (int) caveStartY, (int) caveStartZ, box);

        // Surround cocoon with more cobweb
        this.placeSphereRandomized(world, box, (int) caveStartX, (int) caveStartY + 1, (int) caveStartZ, 3, decoRand, .5f, BlockSetSelector.from(Blocks.COBWEB.getDefaultState()), true);

        // Place spawner
        this.setBlockState(world, Blocks.SPAWNER.getDefaultState(), (int) caveStartX, (int) caveStartY + 1, (int) caveStartZ, box);
        if (box.isVecInside(startPos)) {
            TileEntity spawnerTileEntity = world.getTileEntity(startPos.offset(Direction.UP));
            if (spawnerTileEntity instanceof MobSpawnerTileEntity) {
                ((MobSpawnerTileEntity) spawnerTileEntity).getSpawnerBaseLogic().setEntityType(EntityType.CAVE_SPIDER);
            } else {
                BetterDungeons.LOGGER.warn("Expected cave spider spawner entity at {}, but found none!", startPos.offset(Direction.UP));
            }
        }
    }

    private void generateEggRoom(ISeedReader world, Random featureRand, Random decoRand, ChunkPos chunkPos, MutableBoundingBox box, int[] surface, BitSet carvingMask, BlockPos startPos) {
        float xMinRadius = 2, xMaxRadius = 3,
              yMinRadius = 2, yMaxRadius = 3f,
              zMinRadius = 2, zMaxRadius = 3;

        float xRadius = featureRand.nextFloat() * (xMaxRadius - xMinRadius) + xMinRadius;
        float yRadius = featureRand.nextFloat() * (yMaxRadius - yMinRadius) + yMinRadius;
        float zRadius = featureRand.nextFloat() * (zMaxRadius - zMinRadius) + zMinRadius;

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
                            if (!BLOCK_BLACKLIST.contains(state.getBlock())) {
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
        this.placeSphereRandomized(world, box, chestPos, 2, decoRand, .5f, BlockSetSelector.from(Blocks.WHITE_WOOL.getDefaultState()), false);

        // Guarantee wool immediately around chest
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), chestPos.getX() + 1, chestPos.getY(), chestPos.getZ(), box);
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), chestPos.getX() - 1, chestPos.getY(), chestPos.getZ(), box);
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), chestPos.getX(), chestPos.getY(), chestPos.getZ() + 1, box);
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), chestPos.getX(), chestPos.getY(), chestPos.getZ() - 1, box);
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), chestPos.getX(), chestPos.getY() - 1, chestPos.getZ(), box);
        this.setBlockState(world, Blocks.WHITE_WOOL.getDefaultState(), chestPos.getX(), chestPos.getY() + 1, chestPos.getZ(), box);

        // Surround egg with more cobweb
        this.placeSphereRandomized(world, box, chestPos.getX(), chestPos.getY(), chestPos.getZ(), 2, decoRand, .4f, BlockSetSelector.from(Blocks.COBWEB.getDefaultState()), true);

        // Place chest or spawner
        if (featureRand.nextFloat() < .75f) {
            this.generateChest(world, box, featureRand, chestPos.getX(), chestPos.getY(), chestPos.getZ(), LootTables.CHESTS_SIMPLE_DUNGEON); // TODO - custom loot?
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
    }

    private void decorateCave(ISeedReader world, Random decoRand, ChunkPos chunkPos, MutableBoundingBox box, BitSet carvingMask) {
        carvingMask.stream().forEach(mask -> {
            if (decoRand.nextFloat() < .15f) { // Random chance of cobwebs along cave rim
                // Grab positional info from mask
                int x = mask & 0xF;
                int z = mask >> 4 & 0xF;
                int y = mask >> 8 & 0xFF;
                int globalX = x + chunkPos.x * 16;
                int globalZ = z + chunkPos.z * 16;

                // Ensure it's only placed alongside a wall
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

    private static class CaveStart {
        public BlockPos startPos;
        public float angle;

        public CaveStart(BlockPos startPos, float angle) {
            this.startPos = startPos;
            this.angle = angle;
        }
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
