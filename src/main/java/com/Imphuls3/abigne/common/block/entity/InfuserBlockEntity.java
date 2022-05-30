package com.Imphuls3.abigne.common.block.entity;

import com.Imphuls3.abigne.common.block.entity.utils.ModInventory;
import com.Imphuls3.abigne.common.block.entity.utils.ModInventoryBlockEntity;
import com.Imphuls3.abigne.core.helper.BlockHelper;
import com.Imphuls3.abigne.core.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public class InfuserBlockEntity extends ModInventoryBlockEntity {

    public InfuserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public InfuserBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.INFUSER.get(), pos, state);;
        inv = new ModInventory(1, 64) {
            @Override
            public void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                BlockHelper.updateStateAndNeighbor(level, worldPosition);
            }
        };
    }

    @Override
    public void tick() {
        if(!level.isClientSide){

        }
    }

    @Override
    public InteractionResult onUse(Player player, InteractionHand hand) {
        if(!player.isCrouching()){
            inv.invInteract(player.level, player, hand);
        }
        return InteractionResult.SUCCESS;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inv.invOptional.cast();
        }
        return super.getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inv.invOptional.cast();
        }
        return super.getCapability(cap, side);
    }
}
