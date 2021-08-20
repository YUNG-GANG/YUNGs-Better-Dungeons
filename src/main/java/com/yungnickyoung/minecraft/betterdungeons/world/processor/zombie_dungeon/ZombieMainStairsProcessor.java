package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.BetterDungeons;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.Material;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;
import java.util.Set;

/**
 * Dynamically generates the main staircase when applicable.
 */
public class ZombieMainStairsProcessor extends StructureProcessor {
    public static final ZombieMainStairsProcessor INSTANCE = new ZombieMainStairsProcessor();
    public static final Codec<ZombieMainStairsProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockSetSelector STAIR_SELECTOR = new BlockSetSelector(Blocks.COBBLESTONE_STAIRS.getDefaultState())
        .addBlock(Blocks.MOSSY_COBBLESTONE_STAIRS.getDefaultState(), 0.4f)
        .addBlock(Blocks.COBBLESTONE_SLAB.getDefaultState(), 0.1f)
        .addBlock(Blocks.MOSSY_COBBLESTONE_SLAB.getDefaultState(), 0.1f)
        .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.1f)
        .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
        .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0.1f);

    private static final BlockSetSelector COBBLE_SELECTOR = new BlockSetSelector(Blocks.COBBLESTONE.getDefaultState())
        .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0.3f);

    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Structure.StructureBlockInfo blockInfoLocal, Structure.StructureBlockInfo blockInfoGlobal, StructurePlacementData structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.WARPED_STAIRS) { // Warped stairs are the marker for the main staircase
            BlockPos.Mutable temp = blockInfoGlobal.pos.mutableCopy();
            Direction facing;

            switch (structurePlacementData.getRotation()) {
                case CLOCKWISE_90:
                    facing = Direction.EAST;
                    break;
                case COUNTERCLOCKWISE_90:
                    facing = Direction.WEST;
                    break;
                case CLOCKWISE_180:
                    facing = Direction.SOUTH;
                    break;
                default:
                    facing = Direction.NORTH;
            }

            // Check if the surface is close enough to warrant a staircase
            int maxLength = BetterDungeons.CONFIG.betterDungeons.zombieDungeon.zombieDungeonMaxSurfaceStaircaseLength; // Max distance our staircase can go horizontally

            // The highest allowable position at the end of the staircase
            BlockPos maxSurfacePos = blockInfoGlobal.pos.offset(facing, maxLength).offset(Direction.UP, maxLength);

            // Get the surface height at the end of the staircase
            temp.move(facing, maxLength);
            int surfaceHeight = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, temp.getX(), temp.getZ());

            // Don't spawn staircase if we won't penetrate the surface
            if (surfaceHeight >= maxSurfacePos.getY()) {
                blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.tag);
                return blockInfoGlobal;
            }

            // Begin spawning staircase
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);

            BlockPos.Mutable leftPos = new BlockPos(blockInfoGlobal.pos.offset(facing.rotateYCounterclockwise())).mutableCopy();
            BlockPos.Mutable middlePos = new BlockPos(blockInfoGlobal.pos).mutableCopy();
            BlockPos.Mutable rightPos = new BlockPos(blockInfoGlobal.pos.offset(facing.rotateYClockwise())).mutableCopy();
            BlockState tempBlock;

            for (int i = 0; i < maxLength; i++) {
                int middleSurfaceHeight = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, middlePos.getX(), middlePos.getZ());

                // Stop if we've broken the surface
                if (middleSurfaceHeight < middlePos.getY()) {
                    break;
                }

                // Set left stair
                tempBlock = STAIR_SELECTOR.get(random);
                if (!world.getBlockState(leftPos).isAir()) {
                    if (world.getBlockState(leftPos.offset(facing)).getMaterial().isLiquid()) {
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                    } else {
                        this.setBlockState(world, tempBlock, leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                    }
                }

                // Set middle stair
                tempBlock = STAIR_SELECTOR.get(random);
                if (!world.getBlockState(middlePos).isAir()) {
                    if (world.getBlockState(middlePos.offset(facing)).getMaterial().isLiquid()) {
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), middlePos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                    } else {
                        this.setBlockState(world, tempBlock, middlePos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                    }
                }

                // Set right stair
                tempBlock = STAIR_SELECTOR.get(random);
                if (!world.getBlockState(rightPos).isAir()) {
                    if (world.getBlockState(rightPos.offset(facing)).getMaterial().isLiquid()) {
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                    } else {
                        this.setBlockState(world, tempBlock, rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                    }
                }

                // Place air above stairs
                for (int y = middlePos.getY() + 1; y <= middlePos.getY() + 3; y++) {
                    // Left stairs
                    temp.set(leftPos.getX(), y, leftPos.getZ());
                    this.setBlockState(world, Blocks.CAVE_AIR.getDefaultState(), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                    // Middle stairs
                    temp.set(middlePos.getX(), y, middlePos.getZ());
                    this.setBlockState(world, Blocks.CAVE_AIR.getDefaultState(), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                    // Left stairs
                    temp.set(rightPos.getX(), y, rightPos.getZ());
                    this.setBlockState(world, Blocks.CAVE_AIR.getDefaultState(), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                }

                // Chance of replacing a given block with cobblestone. Increases the further down we are.
                float cobbleChance = (maxLength - i) / (float) maxLength;
                cobbleChance = Math.max(cobbleChance, 0.25f); // Minimum 50% replacement rate at surface

                // Replaceable blocks
                Set<BlockState> replaceableBlocks = Sets.newHashSet(
                    world.getBiome(leftPos).getGenerationSettings().getSurfaceConfig().getTopMaterial(),
                    world.getBiome(leftPos).getGenerationSettings().getSurfaceConfig().getUnderMaterial(),
                    world.getBiome(middlePos).getGenerationSettings().getSurfaceConfig().getTopMaterial(),
                    world.getBiome(middlePos).getGenerationSettings().getSurfaceConfig().getUnderMaterial(),
                    world.getBiome(rightPos).getGenerationSettings().getSurfaceConfig().getTopMaterial(),
                    world.getBiome(rightPos).getGenerationSettings().getSurfaceConfig().getUnderMaterial(),
                    world.getBiome(leftPos.offset(facing.rotateYCounterclockwise())).getGenerationSettings().getSurfaceConfig().getTopMaterial(),
                    world.getBiome(leftPos.offset(facing.rotateYCounterclockwise())).getGenerationSettings().getSurfaceConfig().getUnderMaterial(),
                    world.getBiome(rightPos.offset(facing.rotateYClockwise())).getGenerationSettings().getSurfaceConfig().getTopMaterial(),
                    world.getBiome(rightPos.offset(facing.rotateYClockwise())).getGenerationSettings().getSurfaceConfig().getUnderMaterial()
                );

                // Replaceable materials
                Set<Material> replaceableMaterials = Sets.newHashSet(
                    Material.STONE, Material.SOLID_ORGANIC, Material.SOIL, Material.SOLID_ORGANIC, Material.SNOW_BLOCK, Material.ICE
                );

                // Place cobble above air
                temp.set(leftPos.getX(), leftPos.getY() + 4, leftPos.getZ());
                tempBlock = world.getBlockState(temp);
                if (tempBlock.getMaterial().isLiquid() || (random.nextFloat() < cobbleChance && (replaceableMaterials.contains(tempBlock.getMaterial()) || replaceableBlocks.contains(tempBlock))))
                    this.setBlockState(world, COBBLE_SELECTOR.get(random), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                temp.set(middlePos.getX(), middlePos.getY() + 4, middlePos.getZ());
                tempBlock = world.getBlockState(temp);
                if (tempBlock.getMaterial().isLiquid() || (random.nextFloat() < cobbleChance && (replaceableMaterials.contains(tempBlock.getMaterial()) || replaceableBlocks.contains(tempBlock))))
                    this.setBlockState(world, COBBLE_SELECTOR.get(random), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                temp.set(rightPos.getX(), rightPos.getY() + 4, rightPos.getZ());
                tempBlock = world.getBlockState(temp);
                if (tempBlock.getMaterial().isLiquid() || (random.nextFloat() < cobbleChance && (replaceableMaterials.contains(tempBlock.getMaterial()) || replaceableBlocks.contains(tempBlock))))
                    this.setBlockState(world, COBBLE_SELECTOR.get(random), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                // Place cobble in left wall
                temp.set(leftPos.offset(facing.rotateYCounterclockwise()));
                for (int y = 0; y <= 4; y++) {
                    tempBlock = world.getBlockState(temp);
                    if (tempBlock.getMaterial().isLiquid() || (random.nextFloat() < cobbleChance && (replaceableMaterials.contains(tempBlock.getMaterial()) || replaceableBlocks.contains(tempBlock))))
                        this.setBlockState(world, COBBLE_SELECTOR.get(random), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                    temp.move(Direction.UP);
                }

                // Place cobble in right wall
                temp.set(rightPos.offset(facing.rotateYClockwise()));
                for (int y = 0; y <= 4; y++) {
                    tempBlock = world.getBlockState(temp);
                    if (tempBlock.getMaterial().isLiquid() || (random.nextFloat() < cobbleChance && (replaceableMaterials.contains(tempBlock.getMaterial()) || replaceableBlocks.contains(tempBlock))))
                        this.setBlockState(world, COBBLE_SELECTOR.get(random), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                    temp.move(Direction.UP);
                }

                // Update mutables
                leftPos.move(facing).move(Direction.UP);
                middlePos.move(facing).move(Direction.UP);
                rightPos.move(facing).move(Direction.UP);
            }

            // Begin placing opening at surface
            leftPos.move(facing.getOpposite()).move(Direction.DOWN);
            middlePos.move(facing.getOpposite()).move(Direction.DOWN);
            rightPos.move(facing.getOpposite()).move(Direction.DOWN);
            BlockSetSelector tombSelector = new BlockSetSelector(Blocks.COBBLESTONE.getDefaultState())
                .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), .3f)
                .addBlock(world.getBiome(middlePos).getGenerationSettings().getSurfaceConfig().getUnderMaterial(), .3f);

            // Slabs in doorway
            this.setBlockState(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), leftPos.offset(Direction.UP, 2), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockState(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), middlePos.offset(Direction.UP, 2), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockState(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), rightPos.offset(Direction.UP, 2), structurePlacementData.getMirror(), structurePlacementData.getRotation());

            // Smooth stone atop doorway
            this.setBlockState(world, Blocks.SMOOTH_STONE.getDefaultState(), leftPos.offset(Direction.UP, 3), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockState(world, Blocks.SMOOTH_STONE.getDefaultState(), middlePos.offset(Direction.UP, 3), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockState(world, Blocks.SMOOTH_STONE.getDefaultState(), rightPos.offset(Direction.UP, 3), structurePlacementData.getMirror(), structurePlacementData.getRotation());

            // Chance of hanging soul lantern
            BlockState lanternBlock = BetterDungeons.CONFIG.betterDungeons.general.enableNetherBlocks
                ? Blocks.SOUL_LANTERN.getDefaultState().with(LanternBlock.HANGING, true)
                : Blocks.LANTERN.getDefaultState().with(LanternBlock.HANGING, true);
            if (random.nextFloat() < .25f)
                this.setBlockState(world, lanternBlock, leftPos.offset(Direction.UP, 1), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            else if (random.nextFloat() < .25f)
                this.setBlockState(world, lanternBlock, rightPos.offset(Direction.UP, 1), structurePlacementData.getMirror(), structurePlacementData.getRotation());

            // Andesite corners
            leftPos.move(facing.rotateYCounterclockwise()); // corner
            rightPos.move(facing.rotateYClockwise()); // corner

            // First, place col below andesite to ensure it isn't floating
            this.setColumn(world, tombSelector, leftPos.offset(Direction.DOWN), random);
            this.setColumn(world, tombSelector, rightPos.offset(Direction.DOWN), random);

            this.setBlockState(world, Blocks.POLISHED_ANDESITE.getDefaultState(), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockState(world, Blocks.POLISHED_ANDESITE.getDefaultState(), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockState(world, Blocks.POLISHED_ANDESITE.getDefaultState(), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockState(world, Blocks.POLISHED_ANDESITE.getDefaultState(), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockState(world, Blocks.SMOOTH_STONE.getDefaultState(), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockState(world, Blocks.SMOOTH_STONE.getDefaultState(), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockStateRandom(world, Blocks.SMOOTH_STONE.getDefaultState(), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);
            this.setBlockStateRandom(world, Blocks.SMOOTH_STONE.getDefaultState(), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);

            // Cobble walls
            // Layer 1
            leftPos.move(Direction.DOWN).move(Direction.DOWN).move(Direction.DOWN).move(facing.getOpposite());
            rightPos.move(Direction.DOWN).move(Direction.DOWN).move(Direction.DOWN).move(facing.getOpposite());

            // Ensure walls aren't floating
            this.setColumn(world, tombSelector, leftPos.offset(Direction.DOWN), random);
            this.setColumn(world, tombSelector, rightPos.offset(Direction.DOWN), random);

            this.setBlockState(world, tombSelector.get(random), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockState(world, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockState(world, tombSelector.get(random), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockState(world, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockStateRandom(world, tombSelector.get(random), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);
            this.setBlockStateRandom(world, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);

            // Layer 2
            leftPos.move(Direction.DOWN).move(Direction.DOWN).move(facing.getOpposite());
            rightPos.move(Direction.DOWN).move(Direction.DOWN).move(facing.getOpposite());

            // Ensure walls aren't floating
            this.setColumn(world, tombSelector, leftPos.offset(Direction.DOWN), random);
            this.setColumn(world, tombSelector, rightPos.offset(Direction.DOWN), random);

            this.setBlockState(world, tombSelector.get(random), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockState(world, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockStateRandom(world, tombSelector.get(random), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);
            this.setBlockState(world, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockStateRandom(world, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);

            // Layer 3
            leftPos.move(Direction.DOWN).move(Direction.DOWN).move(facing.getOpposite());
            rightPos.move(Direction.DOWN).move(Direction.DOWN).move(facing.getOpposite());

            // Ensure walls aren't floating
            this.setColumn(world, tombSelector, leftPos.offset(Direction.DOWN), random);
            this.setColumn(world, tombSelector, rightPos.offset(Direction.DOWN), random);

            this.setBlockStateRandom(world, tombSelector.get(random), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);
            this.setBlockState(world, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockStateRandom(world, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);

            // Always replace the warped stair marker with air
            blockInfoGlobal = new Structure.StructureBlockInfo(blockInfoGlobal.pos, STAIR_SELECTOR.get(random), blockInfoGlobal.tag);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_MAIN_STAIRS_PROCESSOR;
    }

    private void setBlockState(WorldView world, BlockState blockState, BlockPos pos, BlockMirror mirror, BlockRotation rotation) {
        if (mirror != BlockMirror.NONE) {
            blockState = blockState.mirror(mirror);
        }

        if (rotation != BlockRotation.NONE) {
            blockState = blockState.rotate(rotation);
        }

        world.getChunk(pos).setBlockState(pos, blockState, false);
    }

    private void setBlockStateRandom(WorldView world, BlockState blockState, BlockPos pos, BlockMirror mirror, BlockRotation rotation, Random random, float chance) {
        if (random.nextFloat() < chance) setBlockState(world, blockState, pos, mirror, rotation);
    }

    private void setColumn(WorldView world, BlockSetSelector selector, BlockPos pos, Random random) {
        ChunkPos currentChunkPos = new ChunkPos(pos);
        Chunk currentChunk = world.getChunk(currentChunkPos.x, currentChunkPos.z);

        // Generate vertical pillar down
        BlockPos.Mutable mutable = pos.mutableCopy();
        BlockState currBlock = world.getBlockState(mutable);
        while (mutable.getY() > 0 && (currBlock.getMaterial() == Material.AIR || currBlock.getMaterial() == Material.WATER || currBlock.getMaterial() == Material.LAVA)) {
            currentChunk.setBlockState(mutable, selector.get(random), false);
            mutable.move(Direction.DOWN);
            currBlock = world.getBlockState(mutable);
        }
    }
}