package com.Imphuls3.abigne.common.block.utils;

import com.Imphuls3.abigne.common.block.entity.utils.ModBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ModBlock<T extends BlockEntity> extends Block implements EntityBlock {
    protected Supplier<BlockEntityType<T>> blockEntityType = null;
    protected BlockEntityTicker<T> ticker = null;
    public ModBlock(Properties properties) {
        super(properties);
    }

    public ModBlock<T> setBlockEntity(Supplier<BlockEntityType<T>> type) {
        this.ticker = (l, p, s, t) -> ((ModBlockEntity)t).tick();
        this.blockEntityType = type;
        return this;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return hasBlockEntity(state) ? blockEntityType.get().create(pos, state) : null;
    }

    public boolean hasBlockEntity(BlockState state) {
        return this.blockEntityType != null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (BlockEntityTicker<T>) ticker;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (hasBlockEntity(state)) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ModBlockEntity simpleBlockEntity) {
                simpleBlockEntity.onPlace(placer, stack);
            }
        }
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        if (hasBlockEntity(state)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ModBlockEntity modBE) {
                ItemStack stack = modBE.onClone(state, target, world, pos, player);
                if (!stack.isEmpty())
                {
                    return stack;
                }
            }
        }
        return super.getCloneItemStack(state, target, world, pos, player);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        onBlockBroken(state, level, pos);
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        onBlockBroken(state, level, pos);
        super.onBlockExploded(state, level, pos, explosion);
    }

    public void onBlockBroken(BlockState state, BlockGetter level, BlockPos pos) {
        if (hasBlockEntity(state)) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ModBlockEntity modBE) {
                modBE.onBreak();
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (hasBlockEntity(state)) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ModBlockEntity modBE) {
                return modBE.onUse(player, hand);
            }
        }
        return super.use(state, level, pos, player, hand, ray);
    }
}