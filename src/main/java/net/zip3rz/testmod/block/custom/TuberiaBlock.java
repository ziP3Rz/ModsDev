package net.zip3rz.testmod.block.custom;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


// TODO: Las tuberias puede ser BaseEntityBlock (pues va a tener un menú provider)=
/* CLASES POSIBLES PARA HEREDAR:
    - RotatedPillarBlock extends Block:
    Posiciona el bloque en función de la cara seleccionada distinguiendo entre axis=x,y,z
    - abstract class BaseEntityBlock extends Block implements EntityBlock:
    Añade un menú a los bloques
    - public class PipeBlock extends Block :
    Voxelshapes en funcion de vecinos
 */
public class TuberiaBlock extends Block{
    private static final Direction[] DIRECTIONS = Direction.values();
    Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.DOWN, Direction.UP};
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION =
            Maps.newEnumMap(ImmutableMap.of(
                    Direction.NORTH, NORTH,
                    Direction.EAST, EAST,
                    Direction.SOUTH, SOUTH,
                    Direction.WEST, WEST,
                    Direction.UP, UP,
                    Direction.DOWN, DOWN
            ));
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final IntegerProperty NEIGHBOURS = IntegerProperty.create("number_neighbours", 0, 6);

    private static final VoxelShape SHAPE =  Block.box(4, 4, 0, 12, 12, 16);


    public TuberiaBlock(Properties properties) {
        super(properties);
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    /**
     * @param placeContext El contexto donde se sitúa el bloque.
     * Setear las variables del blockstate: lógica para selección de los modelo.
     * @return BlockState
      */

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        Level blockgetter = placeContext.getLevel(); //Comprobar si está bien el tipo Level
        BlockPos blockpos = placeContext.getClickedPos();
        BlockState state = blockgetter.getBlockState(blockpos);
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.DOWN, Direction.UP};
        BlockPos[] blockPositions = {
                blockpos.north(),
                blockpos.east(),
                blockpos.south(),
                blockpos.west(),
                blockpos.below(),
                blockpos.above()
        };

        CollectingNeighborUpdater collectingNeighborUpdater = new CollectingNeighborUpdater(blockgetter, 6);
        BlockState[] blockStates = new BlockState[blockPositions.length];
        int count = 0;
        for (int i = 0; i < blockPositions.length; i++) {
            blockStates[i] = blockgetter.getBlockState(blockPositions[i]);
            if (blockStates[i].is(this)) {
                count++;
                blockStates[i].setValue(PROPERTY_BY_DIRECTION.get(directions[i]), blockStates[i].is(this));
                // blockgetter.markAndNotifyBlock(blockPositions[i], blockgetter.getChunkAt(blockPositions[i]), blockStates[i], blockStates[i], 3, 1);
            }
            // collectingNeighborUpdater.neighborChanged(blockpos, blockgetter.getBlockState(blockpos).getBlock(), blockPositions[i]);
            // collectingNeighborUpdater.shapeUpdate(directions[i], state, blockpos, blockPositions[i], 3, 0);
            // state.updateNeighbourShapes();
            // blockgetter.markAndNotifyBlock(blockpos, blockgetter.getChunkAt(blockpos), state, state, 3, 1);
        }
        // blockgetter.neighborShapeChanged(directions[i], );
        state.updateNeighbourShapes(blockgetter, blockpos, 3);

        return this.defaultBlockState()
                .setValue(NORTH, Boolean.valueOf(blockStates[0].is(this)))
                .setValue(EAST, blockStates[1].is(this))
                .setValue(SOUTH, blockStates[2].is(this))
                .setValue(WEST, blockStates[3].is(this))
                .setValue(DOWN, blockStates[4].is(this))
                .setValue(UP, blockStates[5].is(this))
                .setValue(FACING, placeContext.getClickedFace())
                .setValue(NEIGHBOURS, count);
    }

    public String getPipeShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.DOWN, Direction.UP};
        //  HashMap<HashMap<BooleanProperty, Boolean>, BlockState> neighb

        HashMap<Direction, Boolean> neighbourPipeAtDirection = new HashMap<>();
        // Udar BlockPos.MutableBlockPos blockpos$mutableblockpos.setWithOffset(originalPosition, direction)
        BlockPos[] blockNeighbourPositions = {
                blockPos.north(),
                blockPos.east(),
                blockPos.south(),
                blockPos.west(),
                blockPos.below(),
                blockPos.above()
        };
        BlockState[] blockStates = new BlockState[blockNeighbourPositions.length];
        int number_neighbours = 0;
        for (int i = 0; i < blockNeighbourPositions.length; i++) {
            blockStates[i] = blockGetter.getBlockState(blockNeighbourPositions[i]);

            neighbourPipeAtDirection.put(directions[i], blockStates[i].is(this));


            if (blockStates[i].is(this)) {
                number_neighbours++;
                blockStates[i].setValue(PROPERTY_BY_DIRECTION.get(directions[i]), blockStates[i].is(this));
            }
        }

        switch (number_neighbours) {
            case 0:
                return "tuberia_horizontal";
            case 1:
                if (neighbourPipeAtDirection.get(Direction.UP) || neighbourPipeAtDirection.get(Direction.DOWN)) {
                    return "tuberia_vertical";
                }
                return "tuberia_horizontal";
            case 2:
                if (neighbourPipeAtDirection.get(Direction.UP) && neighbourPipeAtDirection.get(Direction.DOWN)) {
                    return "tuberia_vertical";
                }
                else if ((neighbourPipeAtDirection.get(Direction.EAST) && neighbourPipeAtDirection.get(Direction.WEST))
                || (neighbourPipeAtDirection.get(Direction.NORTH) && neighbourPipeAtDirection.get(Direction.SOUTH))) {
                    return "tuberia_horizontal";
                }
                else {
                    return "tuberia_esquina";
                }
            default:
                return "tuberia_horizontal";
        }
    }
    // https://gist.github.com/Commoble/6d0be224b46a1f9064e6e3d6b14a55b7

    public BlockState getStateForPlacement_2(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getClickedFace());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, NORTH, EAST, WEST, SOUTH, UP, DOWN, NEIGHBOURS);
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }




    /* public boolean connectsTo(BlockState otherBlockState,  Direction p_53332_) {
        return otherBlockState.getBlock() instanceof TuberiaBlock;
    }
    */


    /*@Override
    protected static boolean shouldConnectTo(BlockState pState, @Nullable Direction pDirection) {
        pState.
        if (pState.is(Blocks.REDSTONE_WIRE)) {
            return true;
        } else if (pState.is(Blocks.REPEATER)) {
            Direction direction = pState.getValue(RepeaterBlock.FACING);
            return direction == pDirection || direction.getOpposite() == pDirection;
        } else if (pState.is(Blocks.OBSERVER)) {
            return pDirection == pState.getValue(ObserverBlock.FACING);
        } else {
            return pState.isSignalSource() && pDirection != null;
        }
        */


    /*protected static final Map<Direction, VoxelShape> SHAPES = new HashMap<Direction, VoxelShape>();

    protected static void calculateShapes(Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[] { shape, VoxelShape.empty() };

        int times = (to.getStepZ() - Direction.NORTH.getStepZ() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1],
                    VoxelShapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        SHAPES.put(to, buffer[0]);
    }

    protected void runCalculation(VoxelShape shape) {
        for (Direction direction : Direction.values()) {
            calculateShapes(direction, shape);
        }
    }*/


}
