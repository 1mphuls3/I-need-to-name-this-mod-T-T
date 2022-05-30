package com.Imphuls3.abigne.common.block.pipe;

import com.Imphuls3.abigne.common.block.utils.ModBlock;
import com.Imphuls3.abigne.common.block.utils.ModWaterLoggableBlock;
import com.Imphuls3.abigne.core.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

public class PipeBlock extends ModWaterLoggableBlock<PipeBlockEntity> {
    private static final VoxelShape CENTER_SHAPE = Shapes.box(0.3125, 0.3125, 0.3125, 0.6875, 0.6875, 0.6875);

    private static final VoxelShape DOWN_SHAPE = Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.6875, 0.6875);
    private static final VoxelShape UP_SHAPE = Shapes.box(0.3125, 0.3125, 0.3125, 0.6875, 1, 0.6875);
    private static final VoxelShape NORTH_SHAPE = Shapes.box(0.3125, 0.3125, 0, 0.6875, 0.6875, 0.6875);
    private static final VoxelShape SOUTH_SHAPE = Shapes.box(0.3125, 0.3125, 0.3125, 0.6875, 0.6875, 1);
    private static final VoxelShape WEST_SHAPE = Shapes.box(0, 0.3125, 0.3125, 0.6875, 0.6875, 0.6875);
    private static final VoxelShape EAST_SHAPE = Shapes.box(0.3125, 0.3125, 0.3125, 1, 0.6875, 0.6875);

    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty EAST = BooleanProperty.create("east");

    private static final BooleanProperty[] CONNECTIONS = new BooleanProperty[] {
            DOWN, UP, NORTH, SOUTH, WEST, EAST
    };

    private static final VoxelShape[] SIDE_BOXES = new VoxelShape[] {
            DOWN_SHAPE, UP_SHAPE, NORTH_SHAPE, SOUTH_SHAPE, WEST_SHAPE, EAST_SHAPE
    };

    private static final VoxelShape[] shapeCache = new VoxelShape[64];

    public PipeBlock(Properties properties) {
        super(properties);
        setBlockEntity(BlockEntityInit.PIPE);
        registerDefaultState(defaultBlockState()
                .setValue(DOWN, false).setValue(UP, false)
                .setValue(NORTH, false).setValue(SOUTH, false)
                .setValue(WEST, false).setValue(EAST, false)
                .setValue(WATERLOGGED, false));
    }

    @Nonnull
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void neighborChanged(BlockState state, @Nonnull Level worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos, boolean isMoving) {
        BlockState targetState = getTargetState(worldIn, pos, state.getValue(WATERLOGGED));
        if(!targetState.equals(state))
            worldIn.setBlock(pos, targetState, 2 | 4);
    }

    @Nonnull
    @Override
    public BlockState updateShape(BlockState state, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos, @Nonnull BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, facing, facingState, level, pos, facingPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getTargetState(context.getLevel(), context.getClickedPos(), context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    private BlockState getTargetState(Level worldIn, BlockPos pos, boolean waterlog) {
        BlockState newState = defaultBlockState();
        newState = newState.setValue(WATERLOGGED, waterlog);

        for(Direction facing : Direction.values()) {
            BooleanProperty prop = CONNECTIONS[facing.ordinal()];
            PipeBlockEntity.ConnectionType type = PipeBlockEntity.getConnection(worldIn, pos, facing);

            newState = newState.setValue(prop, type.isSolid);
        }

        return newState;
    }

    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        int index = 0;
        for(Direction dir : Direction.values()) {
            int ord = dir.ordinal();
            if(state.getValue(CONNECTIONS[ord]))
                index += (1 << ord);
        }

        VoxelShape cached = shapeCache[index];
        if(cached == null) {
            VoxelShape currShape = CENTER_SHAPE;

            for(Direction dir : Direction.values()) {
                boolean connected = isConnected(state, dir);
                if(connected)
                    currShape = Shapes.or(currShape, SIDE_BOXES[dir.ordinal()]);
            }

            shapeCache[index] = currShape;
            cached = currShape;
        }

        return cached;
    }

    public static boolean isConnected(BlockState state, Direction side) {
        BooleanProperty prop = CONNECTIONS[side.ordinal()];
        return state.getValue(prop);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, SOUTH, WEST, EAST, WATERLOGGED);
    }

    @Override
    public void onRemove(@Nonnull BlockState state, Level worldIn, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new PipeBlockEntity(pos, state);
    }
}
