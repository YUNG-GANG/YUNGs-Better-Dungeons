package com.yungnickyoung.minecraft.betterdungeons.world.processor.zombie_dungeon;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.betterdungeons.config.BDConfig;
import com.yungnickyoung.minecraft.betterdungeons.module.BDModProcessors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import com.yungnickyoung.minecraft.yungsapi.world.processor.ISafeWorldModifier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.Material;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 * Dynamically generates the main staircase when applicable.
 */
public class ZombieMainStairsProcessor extends StructureProcessor implements ISafeWorldModifier {
    public static final ZombieMainStairsProcessor INSTANCE = new ZombieMainStairsProcessor();
    public static final Codec<ZombieMainStairsProcessor> CODEC = Codec.unit(() -> INSTANCE);

    private static final BlockSetSelector STAIR_SELECTOR = new BlockSetSelector(Blocks.COBBLESTONE_STAIRS.defaultBlockState())
        .addBlock(Blocks.MOSSY_COBBLESTONE_STAIRS.defaultBlockState(), 0.4f)
        .addBlock(Blocks.COBBLESTONE_SLAB.defaultBlockState(), 0.1f)
        .addBlock(Blocks.MOSSY_COBBLESTONE_SLAB.defaultBlockState(), 0.1f)
        .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.1f)
        .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.1f)
        .addBlock(Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 0.1f);

    private static final BlockSetSelector COBBLE_SELECTOR = new BlockSetSelector(Blocks.COBBLESTONE.defaultBlockState())
        .addBlock(Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 0.3f);

    private static final Set<Material> REPLACEABLE_MATERIALS = Sets.newHashSet(
            Material.STONE,
            Material.PLANT,
            Material.WATER_PLANT,
            Material.CLAY,
            Material.TOP_SNOW,
            Material.CLAY,
            Material.DIRT,
            Material.GRASS,
            Material.ICE,
            Material.ICE_SOLID,
            Material.SAND,
            Material.LEAVES,
            Material.STONE,
            Material.SNOW,
            Material.POWDER_SNOW
    );

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state.getBlock() == Blocks.WARPED_STAIRS) { // Warped stairs are the marker for the main staircase
            BlockPos.MutableBlockPos temp = blockInfoGlobal.pos.mutable();
            Direction facing = switch (structurePlacementData.getRotation()) {
                case CLOCKWISE_90 -> Direction.EAST;
                case COUNTERCLOCKWISE_90 -> Direction.WEST;
                case CLOCKWISE_180 -> Direction.SOUTH;
                default -> Direction.NORTH;
            };

            // Check if the surface is close enough to warrant a staircase
            int maxLength = BDConfig.zombieDungeons.zombieDungeonMaxSurfaceStaircaseLength.get(); // Max distance our staircase can go horizontally

            // The highest allowable position at the end of the staircase
            BlockPos maxSurfacePos = blockInfoGlobal.pos.relative(facing, maxLength).relative(Direction.UP, maxLength);

            // Get the surface height at the end of the staircase
            temp.move(facing, maxLength);
            int surfaceHeight = levelReader.getHeight(Heightmap.Types.WORLD_SURFACE_WG, temp.getX(), temp.getZ());

            // Don't spawn staircase if we won't penetrate the surface
            if (surfaceHeight >= maxSurfacePos.getY()) {
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, Blocks.CAVE_AIR.defaultBlockState(), blockInfoGlobal.nbt);
                return blockInfoGlobal;
            }

            // Begin spawning staircase
            Random random = structurePlacementData.getRandom(blockInfoGlobal.pos);

            BlockPos.MutableBlockPos leftPos = new BlockPos(blockInfoGlobal.pos.relative(facing.getCounterClockWise())).mutable();
            BlockPos.MutableBlockPos middlePos = new BlockPos(blockInfoGlobal.pos).mutable();
            BlockPos.MutableBlockPos rightPos = new BlockPos(blockInfoGlobal.pos.relative(facing.getClockWise())).mutable();
            BlockState tempBlock;
            Optional<BlockState> tempOptional;

            for (int i = 0; i < maxLength; i++) {
                int middleSurfaceHeight = levelReader.getHeight(Heightmap.Types.WORLD_SURFACE_WG, middlePos.getX(), middlePos.getZ());

                // Stop if we've broken the surface
                if (middleSurfaceHeight < middlePos.getY()) {
                    break;
                }

                // Set left stair
                tempBlock = STAIR_SELECTOR.get(random);
                if (!isBlockStateAirSafe(levelReader, leftPos)) {
                    if (isMaterialLiquidSafe(levelReader, leftPos.relative(facing))) {
                        this.setBlockStateSafeWithPlacement(levelReader, Blocks.COBBLESTONE.defaultBlockState(), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                    } else {
                        this.setBlockStateSafeWithPlacement(levelReader, tempBlock, leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                    }
                }

                // Set middle stair
                tempBlock = STAIR_SELECTOR.get(random);
                if (!isBlockStateAirSafe(levelReader, middlePos)) {
                    if (isMaterialLiquidSafe(levelReader, middlePos.relative(facing))) {
                        this.setBlockStateSafeWithPlacement(levelReader, Blocks.COBBLESTONE.defaultBlockState(), middlePos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                    } else {
                        this.setBlockStateSafeWithPlacement(levelReader, tempBlock, middlePos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                    }
                }

                // Set right stair
                tempBlock = STAIR_SELECTOR.get(random);
                if (!isBlockStateAirSafe(levelReader, rightPos)) {
                    if (isMaterialLiquidSafe(levelReader, rightPos.relative(facing))) {
                        this.setBlockStateSafeWithPlacement(levelReader, Blocks.COBBLESTONE.defaultBlockState(), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                    } else {
                        this.setBlockStateSafeWithPlacement(levelReader, tempBlock, rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                    }
                }

                // Place air above stairs
                for (int y = middlePos.getY() + 1; y <= middlePos.getY() + 3; y++) {
                    // Left stairs
                    temp.set(leftPos.getX(), y, leftPos.getZ());
                    this.setBlockStateSafeWithPlacement(levelReader, Blocks.CAVE_AIR.defaultBlockState(), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                    // Middle stairs
                    temp.set(middlePos.getX(), y, middlePos.getZ());
                    this.setBlockStateSafeWithPlacement(levelReader, Blocks.CAVE_AIR.defaultBlockState(), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                    // Left stairs
                    temp.set(rightPos.getX(), y, rightPos.getZ());
                    this.setBlockStateSafeWithPlacement(levelReader, Blocks.CAVE_AIR.defaultBlockState(), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());
                }

                // Chance of replacing a given block with cobblestone. Increases the further down we are.
                float cobbleChance = (maxLength - i) / (float) maxLength;
                cobbleChance = Math.max(cobbleChance, 0.25f); // Minimum 50% replacement rate at surface

                // Place cobble above air
                temp.set(leftPos.getX(), leftPos.getY() + 4, leftPos.getZ());
                tempOptional = getBlockStateSafe(levelReader, temp);
                if (tempOptional.isEmpty() || tempOptional.get().getMaterial().isLiquid() || (random.nextFloat() < cobbleChance && (REPLACEABLE_MATERIALS.contains(tempOptional.get().getMaterial()))))
                    this.setBlockStateSafeWithPlacement(levelReader, COBBLE_SELECTOR.get(random), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                temp.set(middlePos.getX(), middlePos.getY() + 4, middlePos.getZ());
                tempOptional = getBlockStateSafe(levelReader, temp);
                if (tempOptional.isEmpty() || tempOptional.get().getMaterial().isLiquid() || (random.nextFloat() < cobbleChance && (REPLACEABLE_MATERIALS.contains(tempOptional.get().getMaterial()))))
                    this.setBlockStateSafeWithPlacement(levelReader, COBBLE_SELECTOR.get(random), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                temp.set(rightPos.getX(), rightPos.getY() + 4, rightPos.getZ());
                tempOptional = getBlockStateSafe(levelReader, temp);
                if (tempOptional.isEmpty() || tempOptional.get().getMaterial().isLiquid() || (random.nextFloat() < cobbleChance && (REPLACEABLE_MATERIALS.contains(tempOptional.get().getMaterial()))))
                    this.setBlockStateSafeWithPlacement(levelReader, COBBLE_SELECTOR.get(random), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                // Place cobble in left wall
                temp.set(leftPos.relative(facing.getCounterClockWise()));
                for (int y = 0; y <= 4; y++) {
                    tempOptional = getBlockStateSafe(levelReader, temp);
                    if (tempOptional.isEmpty() || tempOptional.get().getMaterial().isLiquid() || (random.nextFloat() < cobbleChance && (REPLACEABLE_MATERIALS.contains(tempOptional.get().getMaterial()))))
                        this.setBlockStateSafeWithPlacement(levelReader, COBBLE_SELECTOR.get(random), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

                    temp.move(Direction.UP);
                }

                // Place cobble in right wall
                temp.set(rightPos.relative(facing.getClockWise()));
                for (int y = 0; y <= 4; y++) {
                    tempOptional = getBlockStateSafe(levelReader, temp);
                    if (tempOptional.isEmpty() || tempOptional.get().getMaterial().isLiquid() || (random.nextFloat() < cobbleChance && (REPLACEABLE_MATERIALS.contains(tempOptional.get().getMaterial()))))
                        this.setBlockStateSafeWithPlacement(levelReader, COBBLE_SELECTOR.get(random), temp, structurePlacementData.getMirror(), structurePlacementData.getRotation());

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

            BlockSetSelector tombSelector = new BlockSetSelector(Blocks.COBBLESTONE.defaultBlockState())
                .addBlock(Blocks.MOSSY_COBBLESTONE.defaultBlockState(), .4f);

            // Slabs in doorway
            this.setBlockStateSafeWithPlacement(levelReader, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), leftPos.relative(Direction.UP, 2), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockStateSafeWithPlacement(levelReader, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), middlePos.relative(Direction.UP, 2), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockStateSafeWithPlacement(levelReader, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), rightPos.relative(Direction.UP, 2), structurePlacementData.getMirror(), structurePlacementData.getRotation());

            // Smooth stone atop doorway
            this.setBlockStateSafeWithPlacement(levelReader, Blocks.SMOOTH_STONE.defaultBlockState(), leftPos.relative(Direction.UP, 3), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockStateSafeWithPlacement(levelReader, Blocks.SMOOTH_STONE.defaultBlockState(), middlePos.relative(Direction.UP, 3), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockStateSafeWithPlacement(levelReader, Blocks.SMOOTH_STONE.defaultBlockState(), rightPos.relative(Direction.UP, 3), structurePlacementData.getMirror(), structurePlacementData.getRotation());

            // Chance of hanging soul lantern
            BlockState lanternBlock = BDConfig.general.enableNetherBlocks.get()
                ? Blocks.SOUL_LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true)
                : Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true);
            if (random.nextFloat() < .25f)
                this.setBlockStateSafeWithPlacement(levelReader, lanternBlock, leftPos.relative(Direction.UP, 1), structurePlacementData.getMirror(), structurePlacementData.getRotation());
            else if (random.nextFloat() < .25f)
                this.setBlockStateSafeWithPlacement(levelReader, lanternBlock, rightPos.relative(Direction.UP, 1), structurePlacementData.getMirror(), structurePlacementData.getRotation());

            // Andesite corners
            leftPos.move(facing.getCounterClockWise()); // corner
            rightPos.move(facing.getClockWise()); // corner

            // First, place col below andesite to ensure it isn't floating
            this.setColumn(levelReader, tombSelector, leftPos.relative(Direction.DOWN), random);
            this.setColumn(levelReader, tombSelector, rightPos.relative(Direction.DOWN), random);

            this.setBlockStateSafeWithPlacement(levelReader, Blocks.POLISHED_ANDESITE.defaultBlockState(), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockStateSafeWithPlacement(levelReader, Blocks.POLISHED_ANDESITE.defaultBlockState(), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockStateSafeWithPlacement(levelReader, Blocks.POLISHED_ANDESITE.defaultBlockState(), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockStateSafeWithPlacement(levelReader, Blocks.POLISHED_ANDESITE.defaultBlockState(), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockStateSafeWithPlacement(levelReader, Blocks.SMOOTH_STONE.defaultBlockState(), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockStateSafeWithPlacement(levelReader, Blocks.SMOOTH_STONE.defaultBlockState(), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockStateRandom(levelReader, Blocks.SMOOTH_STONE.defaultBlockState(), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);
            this.setBlockStateRandom(levelReader, Blocks.SMOOTH_STONE.defaultBlockState(), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);

            // Cobble walls
            // Layer 1
            leftPos.move(Direction.DOWN).move(Direction.DOWN).move(Direction.DOWN).move(facing.getOpposite());
            rightPos.move(Direction.DOWN).move(Direction.DOWN).move(Direction.DOWN).move(facing.getOpposite());

            // Ensure walls aren't floating
            this.setColumn(levelReader, tombSelector, leftPos.relative(Direction.DOWN), random);
            this.setColumn(levelReader, tombSelector, rightPos.relative(Direction.DOWN), random);

            this.setBlockStateSafeWithPlacement(levelReader, tombSelector.get(random), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockStateSafeWithPlacement(levelReader, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockStateSafeWithPlacement(levelReader, tombSelector.get(random), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockStateSafeWithPlacement(levelReader, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockStateRandom(levelReader, tombSelector.get(random), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);
            this.setBlockStateRandom(levelReader, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);

            // Layer 2
            leftPos.move(Direction.DOWN).move(Direction.DOWN).move(facing.getOpposite());
            rightPos.move(Direction.DOWN).move(Direction.DOWN).move(facing.getOpposite());

            // Ensure walls aren't floating
            this.setColumn(levelReader, tombSelector, leftPos.relative(Direction.DOWN), random);
            this.setColumn(levelReader, tombSelector, rightPos.relative(Direction.DOWN), random);

            this.setBlockStateSafeWithPlacement(levelReader, tombSelector.get(random), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());
            this.setBlockStateSafeWithPlacement(levelReader, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockStateRandom(levelReader, tombSelector.get(random), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);
            this.setBlockStateSafeWithPlacement(levelReader, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockStateRandom(levelReader, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);

            // Layer 3
            leftPos.move(Direction.DOWN).move(Direction.DOWN).move(facing.getOpposite());
            rightPos.move(Direction.DOWN).move(Direction.DOWN).move(facing.getOpposite());

            // Ensure walls aren't floating
            this.setColumn(levelReader, tombSelector, leftPos.relative(Direction.DOWN), random);
            this.setColumn(levelReader, tombSelector, rightPos.relative(Direction.DOWN), random);

            this.setBlockStateRandom(levelReader, tombSelector.get(random), leftPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);
            this.setBlockStateSafeWithPlacement(levelReader, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation());

            leftPos.move(Direction.UP);
            rightPos.move(Direction.UP);
            this.setBlockStateRandom(levelReader, tombSelector.get(random), rightPos, structurePlacementData.getMirror(), structurePlacementData.getRotation(), random, .5f);

            // Always replace the warped stair marker with air
            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos, STAIR_SELECTOR.get(random), blockInfoGlobal.nbt);
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return BDModProcessors.ZOMBIE_MAIN_STAIRS_PROCESSOR;
    }

    private void setBlockStateSafeWithPlacement(LevelReader levelReader, BlockState blockState, BlockPos pos, Mirror mirror, Rotation rotation) {
        if (mirror != Mirror.NONE) {
            blockState = blockState.mirror(mirror);
        }

        if (rotation != Rotation.NONE) {
            blockState = blockState.rotate(rotation);
        }

        setBlockStateSafe(levelReader, pos, blockState);
    }

    private void setBlockStateRandom(LevelReader levelReader, BlockState blockState, BlockPos pos, Mirror mirror, Rotation rotation, Random random, float chance) {
        if (random.nextFloat() < chance) setBlockStateSafeWithPlacement(levelReader, blockState, pos, mirror, rotation);
    }

    private void setColumn(LevelReader levelReader, BlockSetSelector selector, BlockPos pos, Random random) {
        // Generate vertical pillar down
        BlockPos.MutableBlockPos mutable = pos.mutable();
        Optional<BlockState> currBlock = getBlockStateSafe(levelReader, mutable);
        while (mutable.getY() > levelReader.getMinBuildHeight() && (currBlock.isEmpty() || currBlock.get().getMaterial() == Material.AIR || currBlock.get().getMaterial() == Material.WATER || currBlock.get().getMaterial() == Material.LAVA)) {
            if (random.nextFloat() > 0.3) {
                setBlockStateSafe(levelReader, mutable, selector.get(random));
            }
            mutable.move(Direction.DOWN);
            currBlock = getBlockStateSafe(levelReader, mutable);
        }
    }
}