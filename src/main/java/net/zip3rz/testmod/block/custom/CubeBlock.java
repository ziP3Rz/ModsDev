package net.zip3rz.testmod.block.custom;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CubeBlock extends Block {
    protected static final Direction[] UPDATE_SHAPE_ORDER = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP};
    private static final Direction[] DIRECTIONS = Direction.values();
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = ImmutableMap.copyOf(Util.make(Maps.newEnumMap(Direction.class), (map) -> {
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.EAST, EAST);
        map.put(Direction.SOUTH, SOUTH);
        map.put(Direction.WEST, WEST);
        map.put(Direction.UP, UP);
        map.put(Direction.DOWN, DOWN);
    }));
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final DirectionProperty DIRECTION = BlockStateProperties.FACING;
    public static final IntegerProperty NEIGHBOURS = IntegerProperty.create("number_neighbours", 0, 6);
    private final Block base;
    private final BlockState baseState;

    public CubeBlock(Properties properties) {
        super(properties);
        this.base = Blocks.AIR; // These are unused, fields are redirected
        this.baseState = Blocks.AIR.defaultBlockState();
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NEIGHBOURS, 0)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(EAST, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
        );
    }

    private static final VoxelShape SHAPE =  Block.box(4, 4, 0, 12, 12, 16);
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Level level = blockPlaceContext.getLevel();
        BlockPos blockPosition = blockPlaceContext.getClickedPos();
        Map<Direction, Boolean> directionStates = new HashMap<>();
        Map<Direction, Pair<BlockPos, BlockState>> neighbours = getNeighboursBlockStates(level, blockPosition);
        for (Direction direction : UPDATE_SHAPE_ORDER) {
            directionStates.put(direction, (neighbours.get(direction).getSecond().is(this)));
        }
        return defaultBlockState().setValue(NEIGHBOURS, getNumberOfCubeNeighbours(level, blockPosition))
                .setValue(WEST, directionStates.get(Direction.WEST))
                .setValue(EAST, directionStates.get(Direction.EAST))
                .setValue(NORTH, directionStates.get(Direction.NORTH))
                .setValue(SOUTH, directionStates.get(Direction.SOUTH))
                .setValue(DOWN, directionStates.get(Direction.DOWN))
                .setValue(UP, directionStates.get(Direction.UP));
                //.setValue(DIRECTION, blockPlaceContext.getClickedFace().getOpposite());
    }

    /*public Direction.Axis getAxis (BlockPlaceContext blockPlaceContext, Map<Direction, Boolean> directionStates) {
        Level level = blockPlaceContext.getLevel();
        BlockPos blockPosition = blockPlaceContext.getClickedPos();
        switch (getNumberOfCubeNeighbours(level, blockPosition)) {
            case 0:
                return Direction.Axis.X;
            case 1:
                if ( directionStates.get(Direction.UP) || directionStates.get(Direction.DOWN)){
                    return Direction.Axis.Y;
                } else {
                    if (directionStates.get(Direction.NORTH) || directionStates.get(Direction.SOUTH)){
                        return Direction.Axis.X;
                    } else {
                        return Direction.Axis.Z;
                    }
                }
        }
        return Direction.Axis.X;
    }*/

    public int getNumberOfCubeNeighbours (BlockGetter blockGetter, BlockPos blockPosition) {
        int count = 0;
        for (Map.Entry<Direction, Pair<BlockPos, BlockState>> map : getNeighboursBlockStates(blockGetter, blockPosition).entrySet()) {
            count += (map.getValue().getSecond().is(this)) ? 1 : 0;
        }
        return count;
    }

    public Map<Direction, Pair<BlockPos, BlockState>> getNeighboursBlockStates(BlockGetter blockGetter, BlockPos blockPosition) {
        return getNeighboursBlockStates(blockGetter, blockPosition, false, false);
    }

    private Map<Direction, Pair<BlockPos, BlockState>> getNeighboursBlockStates(BlockGetter blockGetter, BlockPos blockPosition, Boolean set, Boolean destroy) {
        BlockPos[] blockPositions = {
                blockPosition.west(),
                blockPosition.east(),
                blockPosition.north(),
                blockPosition.south(),
                blockPosition.below(),
                blockPosition.above()
        };
        Map<Direction, Pair<BlockPos, BlockState>> toReturn = new HashMap<>();
        for (int i = 0; i < blockPositions.length; i++) {
            if (set && blockGetter.getBlockState(blockPositions[i]).is(this)) {
                if (destroy) {
                    toReturn.put(UPDATE_SHAPE_ORDER[i], new Pair(
                            blockPositions[i],
                            blockGetter.getBlockState(blockPositions[i])
                                .setValue(NEIGHBOURS, blockGetter.getBlockState(blockPositions[i]).getValue(NEIGHBOURS) - 1 )
                                .setValue(PROPERTY_BY_DIRECTION.get(UPDATE_SHAPE_ORDER[i].getOpposite()), false ) )
                    );
                } else {
                    toReturn.put(UPDATE_SHAPE_ORDER[i], new Pair(
                            blockPositions[i],
                            blockGetter.getBlockState(blockPositions[i])
                                .setValue(NEIGHBOURS, blockGetter.getBlockState(blockPositions[i]).getValue(NEIGHBOURS) + 1 )
                                .setValue(PROPERTY_BY_DIRECTION.get(UPDATE_SHAPE_ORDER[i].getOpposite()), true )
                            ) );
                }
            } else{
                toReturn.put(UPDATE_SHAPE_ORDER[i], new Pair(
                        blockPositions[i],
                        blockGetter.getBlockState(blockPositions[i])
                ));
            }
        }
        return toReturn;
    }

    @Override
    public void onPlace(BlockState startBlockState, Level level, BlockPos blockPosition, BlockState finalBlockState, boolean p_56965_) {
        System.out.println(startBlockState);
        System.out.println(finalBlockState);
        if (!startBlockState.is(finalBlockState.getBlock())) {
            System.out.println(startBlockState);
            Map<Direction, Pair<BlockPos, BlockState>> neighbours = getNeighboursBlockStates(level, blockPosition, true, false);
            for (Direction direction : UPDATE_SHAPE_ORDER) {
                level.setBlockAndUpdate(neighbours.get(direction).getFirst(), neighbours.get(direction).getSecond());
            }
            System.out.println();
            level.setBlockAndUpdate(blockPosition, startBlockState);
        }
    }
    @Override
    public void onRemove(BlockState startBlockState, Level level, BlockPos blockPosition, BlockState finalBlockState, boolean p_51542_) {
        if (!startBlockState.is(finalBlockState.getBlock())) {
            Map<Direction, Pair<BlockPos, BlockState>> neighbours = getNeighboursBlockStates(level, blockPosition, true, true);
            for (Direction direction : UPDATE_SHAPE_ORDER) {
                level.setBlockAndUpdate(neighbours.get(direction).getFirst(), neighbours.get(direction).getSecond());
            }
            level.removeBlockEntity(blockPosition);
        }
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(NEIGHBOURS, NORTH, SOUTH, WEST, EAST, UP, DOWN);
    }


}
