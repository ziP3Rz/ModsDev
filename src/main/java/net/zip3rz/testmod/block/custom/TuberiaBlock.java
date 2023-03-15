package net.zip3rz.testmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


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
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public static final IntegerProperty NEIGBOURS = IntegerProperty.create("number_neighbours", 0, 6);

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
        BlockGetter blockgetter = placeContext.getLevel();
        BlockPos blockpos = placeContext.getClickedPos();
        BlockPos[] blockPositions = {
                blockpos.north(),
                blockpos.east(),
                blockpos.south(),
                blockpos.west(),
                blockpos.below(),
                blockpos.above()
        };

        BlockState[] blockStates = new BlockState[blockPositions.length];
        int count = 0;
        for (int i = 0; i < blockPositions.length; i++) {
            blockStates[i] = blockgetter.getBlockState(blockPositions[i]);
            count += Boolean.valueOf(blockStates[i].is(this)) ? 1 : 0;
        }

        return this.defaultBlockState()
                .setValue(NORTH, Boolean.valueOf(blockStates[0].is(this)))
                .setValue(EAST, Boolean.valueOf(blockStates[1].is(this)))
                .setValue(SOUTH, Boolean.valueOf(blockStates[2].is(this)))
                .setValue(WEST, Boolean.valueOf(blockStates[3].is(this)))
                .setValue(DOWN, Boolean.valueOf(blockStates[4].is(this)))
                .setValue(UP, Boolean.valueOf(blockStates[5].is(this)))
                .setValue(FACING, placeContext.getClickedFace())
                .setValue(NEIGBOURS, count);
    }
    public BlockState getStateForPlacement_2(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getClickedFace());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, NORTH, EAST, WEST, SOUTH, UP, DOWN, NEIGBOURS);
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
