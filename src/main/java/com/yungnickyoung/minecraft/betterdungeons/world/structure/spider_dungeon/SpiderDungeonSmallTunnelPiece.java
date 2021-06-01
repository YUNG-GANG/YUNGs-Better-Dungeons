package com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon;

import com.yungnickyoung.minecraft.betterdungeons.init.BDModStructurePieces;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.nbt.ListNBT;
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

public class SpiderDungeonSmallTunnelPiece extends SpiderDungeonPiece {
    private final BlockPos startPos;
    private BlockPos endPos;
    private float pitch = 0f;
    private final float[] yaws = new float[LENGTH];

    private static final int LENGTH = 30;
    private static final float X_MINRADIUS = 1, X_MAXRADIUS = 1.5f,
                               Y_MINRADIUS = 2, Y_MAXRADIUS = 2,
                               Z_MINRADIUS = 1, Z_MAXRADIUS = 1.5f;

    public SpiderDungeonSmallTunnelPiece(BlockPos startPos, float initialYaw, int pieceChainLength) {
        super(BDModStructurePieces.SPIDER_DUNGEON_SMALL_TUNNEL_PIECE, pieceChainLength);
        this.boundingBox = new MutableBoundingBox(startPos.getX() - 64, 1, startPos.getZ() - 64, startPos.getX() + 64, 256, startPos.getZ() + 64);
        this.startPos = new BlockPos(startPos);
        this.endPos = new BlockPos(startPos);
        this.yaws[0] = initialYaw;
    }

    public SpiderDungeonSmallTunnelPiece(TemplateManager templateManager, CompoundNBT compoundNBT) {
        super(BDModStructurePieces.SPIDER_DUNGEON_SMALL_TUNNEL_PIECE, compoundNBT);
        int[] start = compoundNBT.getIntArray("startPos");
        int[] end = compoundNBT.getIntArray("endPos");
        this.startPos = new BlockPos(start[0], start[1], start[2]);
        this.endPos = new BlockPos(end[0], end[1], end[2]);
        this.pitch = compoundNBT.getFloat("pitch");
        ListNBT yawListNBT = compoundNBT.getList("yawList", 5);
        for (int i = 0; i < LENGTH; i++) {
            this.yaws[i] = yawListNBT.getFloat(i);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readAdditional(CompoundNBT tagCompound) {
        tagCompound.putIntArray("startPos", new int[]{startPos.getX(), startPos.getY(), startPos.getZ()});
        tagCompound.putIntArray("endPos", new int[]{endPos.getX(), endPos.getY(), endPos.getZ()});
        tagCompound.putFloat("pitch", pitch);
        ListNBT yawListNBT = new ListNBT();
        for (int i = 0; i < LENGTH; i++) {
            yawListNBT.add(FloatNBT.valueOf(yaws[i]));
        }
        tagCompound.put("yawList", yawListNBT);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void buildComponent(StructurePiece piece, List<StructurePiece> pieceList, Random rand) {
        // Determine pitch
        this.pitch = rand.nextFloat() * (float) Math.PI / 3f - ((float) Math.PI / 6f);
        float pitchY = MathHelper.sin(this.pitch);
        float pitchXZ = MathHelper.cos(this.pitch); // Allows for steep drops

        // Center position of tunnel
        float caveStartX = startPos.getX(),
              caveStartY = startPos.getY(),
              caveStartZ = startPos.getZ();

        caveStartX += MathHelper.cos(this.yaws[0]) * pitchXZ;
        caveStartY += MathHelper.sin(pitchY);
        caveStartZ += MathHelper.sin(this.yaws[0]) * pitchXZ;

        float yawModifier = 0f;

        for (int i = 1; i < LENGTH; i++) {
            // Tweak yaw for next iteration
            yawModifier = yawModifier * 0.75F;
            yawModifier += rand.nextFloat() * rand.nextFloat();
            this.yaws[i] = this.yaws[i - 1] + yawModifier * 0.02f;

            // Center of this sphere
            caveStartX += MathHelper.cos(this.yaws[i]) * pitchXZ;
            caveStartY += MathHelper.sin(pitchY);
            caveStartZ += MathHelper.sin(this.yaws[i]) * pitchXZ;
        }

        this.endPos = new BlockPos(caveStartX, caveStartY, caveStartZ);

        if (rand.nextFloat() < 0.8f) {
            StructurePiece eggRoom = new SpiderDungeonEggRoomPiece(endPos, this.componentType + 1);
            pieceList.add(eggRoom);
            eggRoom.buildComponent(eggRoom, pieceList, rand);
        }
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

        // ---- Begin generating small tunnel ---- //
        float caveStartX = startPos.getX(),
              caveStartY = startPos.getY(),
              caveStartZ = startPos.getZ();

        for (int i = 0; i < LENGTH; i++) {
            float pitchY = MathHelper.sin(this.pitch);
            float pitchXZ = MathHelper.cos(this.pitch); // allows for steep drops
            float yaw = this.yaws[i];

            // Center of this sphere
            caveStartX += MathHelper.cos(yaw) * pitchXZ;
            caveStartY += MathHelper.sin(pitchY);
            caveStartZ += MathHelper.sin(yaw) * pitchXZ;

            // Vary the size of the tunnel such that it is widest in the middle and smallest at the end
            float xRadius = MathHelper.lerp(MathHelper.sin((float)(i) * (float) Math.PI / LENGTH), X_MINRADIUS, X_MAXRADIUS);
            float yRadius = MathHelper.lerp(MathHelper.sin((float)(i) * (float) Math.PI / LENGTH), Y_MINRADIUS, Y_MAXRADIUS);
            float zRadius = MathHelper.lerp(MathHelper.sin((float)(i) * (float) Math.PI / LENGTH), Z_MINRADIUS, Z_MAXRADIUS);

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

        decorateCave(world, decoRand, chunkPos, box, carvingMask);

        return true;
    }
}
