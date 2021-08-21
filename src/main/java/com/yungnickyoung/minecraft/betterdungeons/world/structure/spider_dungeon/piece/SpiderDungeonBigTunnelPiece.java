package com.yungnickyoung.minecraft.betterdungeons.world.structure.spider_dungeon.piece;

import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModStructurePieces;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.BitSet;
import java.util.Random;

public class SpiderDungeonBigTunnelPiece extends SpiderDungeonPiece {
    private final BlockPos startPos;
    private BlockPos endPos;
    private float pitch = 0;
    private final float[] yaws = new float[LENGTH];

    private static final int LENGTH = 30;
    private static final float X_MINRADIUS = 2, X_MAXRADIUS = 2.5f,
                               Y_MINRADIUS = 2, Y_MAXRADIUS = 2.5f,
                               Z_MINRADIUS = 2, Z_MAXRADIUS = 2.5f;

    /**
     * Constructor used when the SpiderDungeonStructure initializes generation.
     */
    public SpiderDungeonBigTunnelPiece(int startX, int startY, int startZ) { // Constructor used by starting piece
        this(new BlockPos(
            startX,
            startY,
            startZ),
            0);
    }

    /**
     * Constructor called from existing big tunnels when generating additional offshoot tunnels.
     */
    public SpiderDungeonBigTunnelPiece(BlockPos startPos, int pieceChainLength, float initialPitch, float initialYaw) {
        this(startPos, pieceChainLength);
        this.pitch = initialPitch;
        this.yaws[0] = initialYaw;
    }

    /**
     * Constructor for loading from NBT.
     */
    public SpiderDungeonBigTunnelPiece(ServerWorld world, NbtCompound nbt) {
        super(BDModStructurePieces.SPIDER_DUNGEON_BIG_TUNNEL_PIECE, nbt);
        int[] start = nbt.getIntArray("startPos");
        int[] end = nbt.getIntArray("endPos");
        this.startPos = new BlockPos(start[0], start[1], start[2]);
        this.endPos = new BlockPos(end[0], end[1], end[2]);
        this.pitch = nbt.getFloat("pitch");
        NbtList yawListNBT = nbt.getList("yawList", 5);
        for (int i = 0; i < LENGTH; i++) {
            this.yaws[i] = yawListNBT.getFloat(i);
        }
    }

    private SpiderDungeonBigTunnelPiece(BlockPos startPos, int pieceChainLength) {
        super(BDModStructurePieces.SPIDER_DUNGEON_BIG_TUNNEL_PIECE, pieceChainLength, getInitialBlockBox(startPos));
        this.startPos = new BlockPos(startPos);
        this.endPos = new BlockPos(startPos);
    }

    @Override
    protected void writeNbt(ServerWorld world, NbtCompound nbt) {
        nbt.putIntArray("startPos", new int[]{startPos.getX(), startPos.getY(), startPos.getZ()});
        nbt.putIntArray("endPos", new int[]{endPos.getX(), endPos.getY(), endPos.getZ()});
        nbt.putFloat("pitch", pitch);
        NbtList yawListNBT = new NbtList();
        for (int i = 0; i < LENGTH; i++) {
            yawListNBT.add(NbtFloat.of(yaws[i]));
        }
        nbt.put("yawList", yawListNBT);
    }

    @Override
    public void fillOpenings(StructurePiece structurePiece, StructurePiecesHolder structurePiecesHolder, Random random) {
        // Determine pitch
        if (this.pitch == 0)
            this.pitch = MathHelper.clamp(random.nextFloat() * ((float) -Math.PI), -2.6f, -.6f);

        // Don't let cave go straight down
        if (this.pitch > -2.2f && this.pitch < -1.0f) this.pitch = -2.2f;

        float pitchY = MathHelper.sin(this.pitch);
        float pitchXZ = MathHelper.cos(this.pitch); // Allows for steep drops

        // Determine yaw values
        if (this.yaws[0] == 0)
            this.yaws[0] = random.nextFloat() * ((float) Math.PI * 2F);

        float yawModifier = 0f;

        // Track min/max values for adjusting bounding box
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxZ = Integer.MIN_VALUE;

        // Center position of tunnel
        float caveStartX = startPos.getX(),
              caveStartY = startPos.getY(),
              caveStartZ = startPos.getZ();

        caveStartX += MathHelper.cos(this.yaws[0]) * pitchXZ;
        caveStartY += MathHelper.sin(pitchY);
        caveStartZ += MathHelper.sin(this.yaws[0]) * pitchXZ;

        // Check for min/max bounds
        if (caveStartX - X_MAXRADIUS - 4 < minX) minX = (int) caveStartX - (int) X_MAXRADIUS - 4;
        if (caveStartX + X_MAXRADIUS + 4 > maxX) maxX = (int) caveStartX + (int) X_MAXRADIUS + 4;
        if (caveStartY - Y_MAXRADIUS - 4 < minY) minY = (int) caveStartY - (int) Y_MAXRADIUS - 4;
        if (caveStartY + Y_MAXRADIUS + 4 > maxY) maxY = (int) caveStartY + (int) Y_MAXRADIUS + 4;
        if (caveStartZ - Z_MAXRADIUS - 4 < minZ) minZ = (int) caveStartZ - (int) Z_MAXRADIUS - 4;
        if (caveStartZ + Z_MAXRADIUS + 4 > maxZ) maxZ = (int) caveStartZ + (int) Z_MAXRADIUS + 4;

        for (int i = 1; i < LENGTH; i++) {
            // Tweak yaw for next iteration
            yawModifier = yawModifier * 0.75F;
            yawModifier += random.nextFloat() * random.nextFloat();
            this.yaws[i] = this.yaws[i - 1] + yawModifier * 0.01f;

            // Center of next sphere
            caveStartX += MathHelper.cos(this.yaws[i]) * pitchXZ;
            caveStartY += MathHelper.sin(pitchY);
            caveStartZ += MathHelper.sin(this.yaws[i]) * pitchXZ;

            // Check for min/max bounds
            if (caveStartX - X_MAXRADIUS - 4 < minX) minX = (int) caveStartX - (int) X_MAXRADIUS - 4;
            if (caveStartX + X_MAXRADIUS + 4 > maxX) maxX = (int) caveStartX + (int) X_MAXRADIUS + 4;
            if (caveStartY - Y_MAXRADIUS - 4 < minY) minY = (int) caveStartY - (int) Y_MAXRADIUS - 4;
            if (caveStartY + Y_MAXRADIUS + 4 > maxY) maxY = (int) caveStartY + (int) Y_MAXRADIUS + 4;
            if (caveStartZ - Z_MAXRADIUS - 4 < minZ) minZ = (int) caveStartZ - (int) Z_MAXRADIUS - 4;
            if (caveStartZ + Z_MAXRADIUS + 4 > maxZ) maxZ = (int) caveStartZ + (int) Z_MAXRADIUS + 4;
        }

        // Update bounding box
        this.boundingBox.minX = minX;
        this.boundingBox.maxX = maxX;
        this.boundingBox.minY = minY;
        this.boundingBox.maxY = maxY;
        this.boundingBox.minZ = minZ;
        this.boundingBox.maxZ = maxZ;

        this.endPos = new BlockPos(caveStartX, caveStartY, caveStartZ);

        // Generate additional big tunnel if there's only one so far
        if (this.chainLength == 0) {
            StructurePiece nextBigTunnelPiece = new SpiderDungeonBigTunnelPiece(endPos, this.chainLength + 1, 0, yaws[LENGTH - 1]);
            structurePiecesHolder.addPiece(nextBigTunnelPiece);
            nextBigTunnelPiece.fillOpenings(nextBigTunnelPiece, structurePiecesHolder, random);
        }

        // Generate small tunnels leaving nest
        float smallTunnelAngle = yaws[LENGTH - 1] + 0.3f; // Offset angle a bit before spawning small tunnels to avoid overlap w/ main tunnel

        // Small tunnel 1
        smallTunnelAngle += random.nextFloat() * .4f + 0.9f;
        StructurePiece smallTunnelPiece1 = new SpiderDungeonSmallTunnelPiece(endPos, smallTunnelAngle, this.chainLength + 1);
        structurePiecesHolder.addPiece(smallTunnelPiece1);
        smallTunnelPiece1.fillOpenings(smallTunnelPiece1, structurePiecesHolder, random);

        // Small tunnel 2
        smallTunnelAngle += random.nextFloat() * .4f + 0.9f;
        StructurePiece smallTunnelPiece2 = new SpiderDungeonSmallTunnelPiece(endPos, smallTunnelAngle, this.chainLength + 1);
        structurePiecesHolder.addPiece(smallTunnelPiece2);
        smallTunnelPiece2.fillOpenings(smallTunnelPiece2, structurePiecesHolder, random);


        // Small tunnel 3
        smallTunnelAngle += random.nextFloat() * .4f + 0.9f;
        StructurePiece smallTunnelPiece3 = new SpiderDungeonSmallTunnelPiece(endPos, smallTunnelAngle, this.chainLength + 1);
        structurePiecesHolder.addPiece(smallTunnelPiece3);
        smallTunnelPiece3.fillOpenings(smallTunnelPiece3, structurePiecesHolder, random);

        // Small tunnel, or chance of another big tunnel
        smallTunnelAngle += random.nextFloat() * .4f + 0.9f;
        if (random.nextFloat() < .5f) {
            StructurePiece smallTunnelPiece4 = new SpiderDungeonSmallTunnelPiece(endPos, smallTunnelAngle, this.chainLength + 1);
            structurePiecesHolder.addPiece(smallTunnelPiece4);
            smallTunnelPiece4.fillOpenings(smallTunnelPiece4, structurePiecesHolder, random);
        } else if (this.chainLength == 0) {
            StructurePiece extraBigTunnelPiece = new SpiderDungeonBigTunnelPiece(endPos, this.chainLength + 1, random.nextFloat() * (float) Math.PI / 6f - ((float) Math.PI / 6f), smallTunnelAngle);
            structurePiecesHolder.addPiece(extraBigTunnelPiece);
            extraBigTunnelPiece.fillOpenings(extraBigTunnelPiece, structurePiecesHolder, random);
        }

        // Generate nest at end of tunnel
        StructurePiece nestPiece = new SpiderDungeonNestPiece(endPos, this.chainLength + 1);
        structurePiecesHolder.addPiece(nestPiece);
        nestPiece.fillOpenings(nestPiece, structurePiecesHolder, random);
    }

    /**
     * Generate.
     */
    @Override
    public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator, Random random, BlockBox box, ChunkPos chunkPos, BlockPos blockPos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        ChunkRandom decoRand = new ChunkRandom(); // Rand for decoration. It's not as important for this to be deterministic.
        decoRand.setDecoratorSeed(world.getSeed(), startPos.getX(), startPos.getZ());

        // Temporary chunk-local carving mask to prevent overwriting carved blocks and add decorations
        BitSet carvingMask = new BitSet(65536);

        // Surface
        int[] surface = new int[256];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                mutable.set(chunkPos.getStartX() + x, 1, chunkPos.getStartZ() + z);
                surface[x * 16 + z] = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, mutable.getX(), mutable.getZ());
            }
        }

        float caveStartX = startPos.getX(),
              caveStartY = startPos.getY(),
              caveStartZ = startPos.getZ();

        // ---- Begin generating big tunnel ---- //

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
            if (BetterDungeons.DEBUG_MODE)
                this.addBlock(world, Blocks.DIAMOND_BLOCK.getDefaultState(), (int) caveStartX, (int) caveStartY, (int) caveStartZ, box);

            // -- Carve sphere -- //
            for (float x = minX; x <= maxX; x++) {
                // Get global coordinate
                int globalX = (int)x + chunkPos.x * 16;

                // No need to consider blocks outside this chunk
                if (globalX < chunkPos.getStartX() || globalX > chunkPos.getEndX()) continue;

                // Distance along the rotated x-axis from the center of this ellipsoid.
                // You can think of this value as (x/a), where a is the length of the ellipsoid's radius in the rotated x-direction.
                float radialXDist = (globalX - caveStartX + .5f) / xRadius;

                for (float z = minZ; z <= maxZ; z++) {
                    // Get global coordinate
                    int globalZ = (int)z + chunkPos.z * 16;

                    // No need to consider blocks outside this chunk
                    if (globalZ < chunkPos.getStartZ() || globalZ > chunkPos.getEndZ()) continue;

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
                            if (!BLOCK_BLACKLIST.contains(this.getBlockAt(world, globalX, globalY, globalZ, box).getBlock())) {
                                this.addBlock(world, Blocks.CAVE_AIR.getDefaultState(), globalX, globalY, globalZ, box);
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
                                BlockState state = this.getBlockAt(world, globalX, globalY, globalZ, box);
                                if (!BLOCK_BLACKLIST.contains(state.getBlock())) {
                                    if (state.isAir() || state.getFluidState().getFluid() != Fluids.EMPTY || decoRand.nextFloat() < .2f) {
                                        this.addBlock(world, Blocks.COBBLESTONE.getDefaultState(), globalX, globalY, globalZ, box);
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
