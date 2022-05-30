package com.Imphuls3.abigne.common.block;

import com.Imphuls3.abigne.common.block.entity.ItemTransporterBlockEntity;
import com.Imphuls3.abigne.common.block.entity.PedestalBlockEntity;
import com.Imphuls3.abigne.common.block.utils.ModWaterLoggableBlock;
import com.Imphuls3.abigne.core.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.Stream;

public class ItemTransporterBlock extends ModWaterLoggableBlock<ItemTransporterBlockEntity> {

    public static final VoxelShape SHAPE = Stream.of(
            Block.box(4, 0, 4, 12, 4, 12),
            Block.box(5, 4, 5, 11, 10, 11),
            Block.box(3, 10, 3, 13, 13, 13)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public ItemTransporterBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false));
        setBlockEntity(BlockEntityInit.TRANSPORTER);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
