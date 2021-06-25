package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.Set;

/**
 * Dynamically generates the main staircase when applicable.
 */
@MethodsReturnNonnullByDefault
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

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.getBlock() == Blocks.WARPED_STAIRS) { // Warped stairs are the marker for the main staircase
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
            int maxLength = 20; // Max distance our staircase can go horizontally

            // The highest allowable position at the end of the staircase
            BlockPos maxSurfacePos = blockInfoGlobal.pos.offset(facing, maxLength).offset(Direction.UP, maxLength);

            // Get the surface height at the end of the staircase
            int surfaceHeight = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, blockInfoGlobal.pos.offset(facing, maxLength)).getY();

            // Don't spawn staircase if we won't penetrate the surface
            if (surfaceHeight >= maxSurfacePos.getY()) {
                blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.getDefaultState(), blockInfoGlobal.nbt);
                return blockInfoGlobal;
            }

            // Begin spawning staircase
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);

            BlockPos.Mutable leftPos = new BlockPos(blockInfoGlobal.pos.offset(facing.rotateYCCW())).toMutable();
            BlockPos.Mutable middlePos = new BlockPos(blockInfoGlobal.pos).toMutable();
            BlockPos.Mutable rightPos = new BlockPos(blockInfoGlobal.pos.offset(facing.rotateY())).toMutable();
            BlockPos.Mutable temp = new BlockPos.Mutable();
            BlockState tempBlock;

            for (int i = 0; i < maxLength; i++) {
                int middleSurfaceHeight = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, middlePos).getY();

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
                    temp.setPos(leftPos.getX(), y, leftPos.getZ());
                    this.setBlockState(world, Blocks.CAVE_AIR.getDefaultState(), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                    // Middle stairs
                    temp.setPos(middlePos.getX(), y, middlePos.getZ());
                    this.setBlockState(world, Blocks.CAVE_AIR.getDefaultState(), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                    // Left stairs
                    temp.setPos(rightPos.getX(), y, rightPos.getZ());
                    this.setBlockState(world, Blocks.CAVE_AIR.getDefaultState(), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                }

                // Chance of replacing a given block with cobblestone. Increases the further down we are.
                float cobbleChance = (maxLength - i) / (float) maxLength;
                cobbleChance = Math.max(cobbleChance, 0.25f); // Minimum 50% replacement rate at surface

                // Replaceable blocks
                Set<BlockState> replaceableBlocks = Sets.newHashSet(
                    world.getBiome(leftPos).getGenerationSettings().getSurfaceBuilderConfig().getTop(),
                    world.getBiome(leftPos).getGenerationSettings().getSurfaceBuilderConfig().getUnder(),
                    world.getBiome(middlePos).getGenerationSettings().getSurfaceBuilderConfig().getTop(),
                    world.getBiome(middlePos).getGenerationSettings().getSurfaceBuilderConfig().getUnder(),
                    world.getBiome(rightPos).getGenerationSettings().getSurfaceBuilderConfig().getTop(),
                    world.getBiome(rightPos).getGenerationSettings().getSurfaceBuilderConfig().getUnder(),
                    world.getBiome(leftPos.offset(facing.rotateYCCW())).getGenerationSettings().getSurfaceBuilderConfig().getTop(),
                    world.getBiome(leftPos.offset(facing.rotateYCCW())).getGenerationSettings().getSurfaceBuilderConfig().getUnder(),
                    world.getBiome(rightPos.offset(facing.rotateY())).getGenerationSettings().getSurfaceBuilderConfig().getTop(),
                    world.getBiome(rightPos.offset(facing.rotateY())).getGenerationSettings().getSurfaceBuilderConfig().getUnder()
                );

                // Replaceable materials
                Set<Material> replaceableMaterials = Sets.newHashSet(
                    Material.ROCK, Material.ORGANIC, Material.EARTH, Material.SAND, Material.SNOW, Material.ICE
                );

                // Place cobble above air
                temp.setPos(leftPos.getX(), leftPos.getY() + 4, leftPos.getZ());
                tempBlock = world.getBlockState(temp);
                if (tempBlock.getMaterial().isLiquid() || (random.nextFloat() < cobbleChance && (replaceableMaterials.contains(tempBlock.getMaterial()) || replaceableBlocks.contains(tempBlock))))
                    this.setBlockState(world, COBBLE_SELECTOR.get(random), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                temp.setPos(middlePos.getX(), middlePos.getY() + 4, middlePos.getZ());
                tempBlock = world.getBlockState(temp);
                if (tempBlock.getMaterial().isLiquid() || (random.nextFloat() < cobbleChance && (replaceableMaterials.contains(tempBlock.getMaterial()) || replaceableBlocks.contains(tempBlock))))
                    this.setBlockState(world, COBBLE_SELECTOR.get(random), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                temp.setPos(rightPos.getX(), rightPos.getY() + 4, rightPos.getZ());
                tempBlock = world.getBlockState(temp);
                if (tempBlock.getMaterial().isLiquid() || (random.nextFloat() < cobbleChance && (replaceableMaterials.contains(tempBlock.getMaterial()) || replaceableBlocks.contains(tempBlock))))
                    this.setBlockState(world, COBBLE_SELECTOR.get(random), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                // Place cobble in left wall
                temp.setPos(leftPos.offset(facing.rotateYCCW()));
                for (int y = 0; y <= 4; y++) {
                    tempBlock = world.getBlockState(temp);
                    if (tempBlock.getMaterial().isLiquid() || (random.nextFloat() < cobbleChance && (replaceableMaterials.contains(tempBlock.getMaterial()) || replaceableBlocks.contains(tempBlock))))
                        this.setBlockState(world, COBBLE_SELECTOR.get(random), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                    temp.move(Direction.UP);
                }

                // Place cobble in right wall
                temp.setPos(rightPos.offset(facing.rotateY()));
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
                .addBlock(world.getBiome(middlePos).getGenerationSettings().getSurfaceBuilderConfig().getUnder(), .3f);

            // Slabs in doorway
            this.setBlockState(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), leftPos.offset(Direction.UP, 2), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockState(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), middlePos.offset(Direction.UP, 2), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockState(world, Blocks.SMOOTH_STONE_SLAB.getDefaultState(), rightPos.offset(Direction.UP, 2), structurePlacementData.getMirror(), structurePlacementData.getRotation());

            // Smooth stone atop doorway
            this.setBlockState(world, Blocks.SMOOTH_STONE.getDefaultState(), leftPos.offset(Direction.UP, 3), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockState(world, Blocks.SMOOTH_STONE.getDefaultState(), middlePos.offset(Direction.UP, 3), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockState(world, Blocks.SMOOTH_STONE.getDefaultState(), rightPos.offset(Direction.UP, 3), structurePlacementData.getMirror(), structurePlacementData.getRotation());

            // Chance of hanging soul lantern
            if (random.nextFloat() < .25f)
                this.setBlockState(world, Blocks.SOUL_LANTERN.getDefaultState().with(LanternBlock.HANGING, true), leftPos.offset(Direction.UP, 1), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            else if (random.nextFloat() < .25f)
                this.setBlockState(world, Blocks.SOUL_LANTERN.getDefaultState().with(LanternBlock.HANGING, true), rightPos.offset(Direction.UP, 1), structurePlacementData.getMirror(), structurePlacementData.getRotation());

            // Andesite corners
            leftPos.move(facing.rotateYCCW()); // corner
            rightPos.move(facing.rotateY()); // corner

            // First, place col below andesite to ensure it isn't floating
            this.setColumn(world, tombSelector, leftPos.offset(Direction.DOWN), structurePlacementData.getMirror(), structurePlacementData.getRotation(), random);
            this.setColumn(world, tombSelector, rightPos.offset(Direction.DOWN), structurePlacementData.getMirror(), structurePlacementData.getRotation(), random);

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
            this.setColumn(world, tombSelector, leftPos.offset(Direction.DOWN), structurePlacementData.getMirror(), structurePlacementData.getRotation(), random);
            this.setColumn(world, tombSelector, rightPos.offset(Direction.DOWN), structurePlacementData.getMirror(), structurePlacementData.getRotation(), random);

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
            this.setColumn(world, tombSelector, leftPos.offset(Direction.DOWN), structurePlacementData.getMirror(), structurePlacementData.getRotation(), random);
            this.setColumn(world, tombSelector, rightPos.offset(Direction.DOWN), structurePlacementData.getMirror(), structurePlacementData.getRotation(), random);

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
            this.setColumn(world, tombSelector, leftPos.offset(Direction.DOWN), structurePlacementData.getMirror(), structurePlacementData.getRotation(), random);
            this.setColumn(world, tombSelector, rightPos.offset(Direction.DOWN), structurePlacementData.getMirror(), structurePlacementData.getRotation(), random);

            this.setBlockStateRandom(world, tombSelector.get(random), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);
            this.setBlockState(world, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockStateRandom(world, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);

            // Always replace the warped stair marker with air
            blockInfoGlobal = new Template.BlockInfo(blockInfoGlobal.pos, STAIR_SELECTOR.get(random), blockInfoGlobal.nbt);
        }
        return blockInfoGlobal;
    }

    protected IStructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_MAIN_STAIRS_PROCESSOR;
    }

    private void setBlockState(IWorldReader world, BlockState blockState, BlockPos pos, Mirror mirror, Rotation rotation) {
        if (mirror != Mirror.NONE) {
            blockState = blockState.mirror(mirror);
        }

        if (rotation != Rotation.NONE) {
            blockState = blockState.rotate(rotation);
        }

        world.getChunk(pos).setBlockState(pos, blockState, false);
    }

    private void setBlockStateRandom(IWorldReader world, BlockState blockState, BlockPos pos, Mirror mirror, Rotation rotation, Random random, float chance) {
        if (random.nextFloat() < chance) setBlockState(world, blockState, pos, mirror, rotation);
    }

    private void setColumn(IWorldReader world, BlockSetSelector selector, BlockPos pos, Mirror mirror, Rotation rotation, Random random) {
        ChunkPos currentChunkPos = new ChunkPos(pos);
        IChunk currentChunk = world.getChunk(currentChunkPos.x, currentChunkPos.z);

        // Generate vertical pillar down
        BlockPos.Mutable mutable = pos.toMutable();
        BlockState currBlock = world.getBlockState(mutable);
        while (mutable.getY() > 0 && (currBlock.getMaterial() == Material.AIR || currBlock.getMaterial() == Material.WATER || currBlock.getMaterial() == Material.LAVA)) {
            currentChunk.setBlockState(mutable, selector.get(random), false);
            mutable.move(Direction.DOWN);
            currBlock = world.getBlockState(mutable);
        }
    }
}