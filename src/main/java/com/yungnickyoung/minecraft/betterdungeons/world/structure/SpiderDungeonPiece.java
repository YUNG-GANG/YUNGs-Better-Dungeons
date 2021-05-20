package com.yungnickyoung.minecraft.betterdungeons.world.structure;

import com.yungnickyoung.minecraft.betterdungeons.init.ModStructurePieces;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
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

public class SpiderDungeonPiece extends StructurePiece {
    private BlockPos cornerPos;
    private BlockPos surfaceStartPos;

    public SpiderDungeonPiece(BlockPos centerPos) {
        super(ModStructurePieces.SPIDER_DUNGEON_PIECE, 0);
        this.boundingBox = new MutableBoundingBox(centerPos.getX() - 64, 1, centerPos.getZ() - 64, centerPos.getX() + 64, 256, centerPos.getZ() + 64);
        this.cornerPos = new BlockPos(centerPos.getX() - 64, 1, centerPos.getZ() - 64);
        this.surfaceStartPos = new BlockPos(centerPos.getX(), 80, centerPos.getZ());
    }

    public SpiderDungeonPiece(TemplateManager templateManager, CompoundNBT compoundNBT) {
        super(ModStructurePieces.SPIDER_DUNGEON_PIECE, compoundNBT);
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
//        if (surfaceStartPos.getY() == 0) surfaceStartPos.setPos(world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, this.surfaceStartPos));
        int[] surface = new int[256];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                mutable.setPos(chunkPos.getXStart() + x, 1, chunkPos.getZStart() + z);
                surface[x * 16 + z] = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, mutable).getY();
            }
        }

        // Length and radii determine the general shape of the cave
        int length = 60;
        float xMinRadius = 2;
        float xMaxRadius = 4;
        float yMinRadius = 1;
        float yMaxRadius = 4;
        float zMinRadius = 2;
        float zMaxRadius = 4;

        // Initial pitch and yaw values determine the direction the cave will move towards
        float yaw = featureRand.nextFloat() * ((float) Math.PI * 2F);
        float pitch = MathHelper.clamp(featureRand.nextFloat() * ((float) -Math.PI), -2.8f, -.4f);

        float caveStartX = surfaceStartPos.getX();
        float caveStartY = surfaceStartPos.getY();
        float caveStartZ = surfaceStartPos.getZ();

        float pitchModifier = 0f;
        float yawModifier = 0f;

        for (int i = 0; i < length; i++) {
            float pitchY = MathHelper.sin(pitch);
            float pitchXZ = MathHelper.cos(pitch); // allows for steep drops

            // Center of this sphere
            caveStartX += MathHelper.cos(yaw) * pitchXZ;
            caveStartY += MathHelper.sin(pitchY);
            caveStartZ += MathHelper.sin(yaw) * pitchXZ;

            // Tweak pitch and yaw for next iteration
//            pitch = pitch < -Math.PI/2 ? pitch + pitchModifier * 0.01f : pitch - pitchModifier * 0.01f; // makes caves always turn down
            yaw += yawModifier * 0.1f;

            pitchModifier = pitchModifier * 0.9F;
            yawModifier = yawModifier * 0.75F;

            pitchModifier += featureRand.nextFloat() * featureRand.nextFloat();
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
            this.setBlockState(world, Blocks.DIAMOND_BLOCK.getDefaultState(), (int) caveStartX, (int) caveStartY, (int) caveStartZ, box);

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
                        if (!carvingMask.get(mask) && radialXDist * radialXDist + radialYDist * radialYDist + radialZDist * radialZDist < 1.0) {
                            if (this.getBlockStateFromPos(world, globalX, globalY, globalZ, box).getBlock() != Blocks.DIAMOND_BLOCK.getBlock()) {
                                this.setBlockState(world, Blocks.CAVE_AIR.getDefaultState(), globalX, globalY, globalZ, box);
                                carvingMask.set(mask);
                            }
                        } else {
                            // Calculate radial distances for each of the three axes (same logic as above), but for a slightly enlarged ellipsoid.
                            // This will be used to give the cave an outer cobblestone shell.
                            float radialXDistShell = (globalX - caveStartX + .5f) / (xRadius + 1.2f);
                            float radialYDistShell = (y - caveStartY - .5f) / (yRadius + 1.2f);
                            float radialZDistShell = (globalZ - caveStartZ + .5f) / (zRadius + 1.2f);
                            if (!carvingMask.get(mask) && radialXDistShell * radialXDistShell + radialYDistShell * radialYDistShell + radialZDistShell * radialZDistShell < 1.0) {
                                if (this.getBlockStateFromPos(world, globalX, globalY, globalZ, box).getBlock() != Blocks.DIAMOND_BLOCK.getBlock()) {
                                    if (this.getBlockStateFromPos(world, globalX, globalY, globalZ, box).isAir() || this.getBlockStateFromPos(world, globalX, globalY, globalZ, box).getMaterial().isLiquid() || decoRand.nextFloat() < .5f) {
                                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), globalX, globalY, globalZ, box);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
