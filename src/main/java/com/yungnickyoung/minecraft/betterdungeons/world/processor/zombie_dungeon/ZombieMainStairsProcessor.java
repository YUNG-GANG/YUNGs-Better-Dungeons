package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.init.BDModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

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

    @ParametersAreNonnullByDefault
    @Override
    public Template.BlockInfo process(IWorldReader world, BlockPos jigsawPiecePos, BlockPos jigsawPieceBottomCenterPos, Template.BlockInfo blockInfoLocal, Template.BlockInfo blockInfoGlobal, PlacementSettings structurePlacementData, @Nullable Template template) {
        if (blockInfoGlobal.state.getBlock() == Blocks.WARPED_STAIRS) { // Warped stairs are the marker for the main staircase
            Direction facing = blockInfoGlobal.state.get(StairsBlock.FACING);

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
                default: facing = Direction.NORTH;
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
                int worldHeight = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, middlePos).getY();

                // Stop if we've broken the surface
                if (worldHeight < middlePos.getY()) {
                    break;
                }

                // Set left stair
                tempBlock = STAIR_SELECTOR.get(random);
                if (!world.getBlockState(leftPos).isAir())
                    this.setBlockState(world, tempBlock, leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                // Set middle stair
                tempBlock = STAIR_SELECTOR.get(random);
                if (!world.getBlockState(middlePos).isAir())
                    this.setBlockState(world, tempBlock, middlePos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                // Set right stair
                tempBlock = STAIR_SELECTOR.get(random);
                if (!world.getBlockState(rightPos).isAir())
                    this.setBlockState(world, tempBlock, rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

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

                // Place cobble above air
                if (middlePos.getY() + 4 <= worldHeight) { // Don't go above world height
                    temp.setPos(leftPos.getX(), leftPos.getY() + 4, leftPos.getZ());
                    if (world.getBlockState(temp).getMaterial() == Material.EARTH || world.getBlockState(temp).getMaterial() == Material.ROCK || world.getBlockState(temp).getMaterial().isLiquid())
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                    temp.setPos(middlePos.getX(), middlePos.getY() + 4, middlePos.getZ());
                    if (world.getBlockState(temp).getMaterial() == Material.EARTH || world.getBlockState(temp).getMaterial() == Material.ROCK || world.getBlockState(temp).getMaterial().isLiquid())
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                    temp.setPos(rightPos.getX(), rightPos.getY() + 4, rightPos.getZ());
                    if (world.getBlockState(temp).getMaterial() == Material.EARTH || world.getBlockState(temp).getMaterial() == Material.ROCK || world.getBlockState(temp).getMaterial().isLiquid())
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                }

                // Place cobble in left wall
                temp.setPos(leftPos.offset(facing.rotateYCCW()));
                for (int y = 0; y <= 4; y++) {
                    // Don't go above world height
                    if (temp.getY() > worldHeight) break;

                    if (world.getBlockState(temp).getMaterial() == Material.EARTH || world.getBlockState(temp).getMaterial() == Material.ROCK || world.getBlockState(temp).getMaterial().isLiquid())
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                    temp.move(Direction.UP);
                }

                // Place cobble in right wall
                temp.setPos(rightPos.offset(facing.rotateY()));
                for (int y = 0; y <= 3; y++) {
                    // Don't go above world height
                    if (temp.getY() > worldHeight) break;

                    if (world.getBlockState(temp).getMaterial() == Material.EARTH || world.getBlockState(temp).getMaterial() == Material.ROCK || world.getBlockState(temp).getMaterial().isLiquid())
                        this.setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                    temp.move(Direction.UP);
                }

                // Update mutables
                leftPos.move(facing).move(Direction.UP);
                middlePos.move(facing).move(Direction.UP);
                rightPos.move(facing).move(Direction.UP);
            }

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
}
