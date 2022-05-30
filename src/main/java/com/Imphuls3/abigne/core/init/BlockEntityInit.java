package com.Imphuls3.abigne.core.init;

import com.Imphuls3.abigne.AbIgne;
import com.Imphuls3.abigne.common.block.entity.*;
import com.Imphuls3.abigne.common.block.pipe.PipeBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BlockEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> BE = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITIES, AbIgne.MOD_ID);

    public static final RegistryObject<BlockEntityType<PedestalBlockEntity>> PEDESTAL = BE.register("pedestal",
            () -> BlockEntityType.Builder.of(PedestalBlockEntity::new, BlockInit.PEDESTAL.get()).build(null));

    public static final RegistryObject<BlockEntityType<CrucibleBlockEntity>> CRUCIBLE = BE.register("crucible",
            () -> BlockEntityType.Builder.of(CrucibleBlockEntity::new, BlockInit.CRUCIBLE.get()).build(null));

    public static final RegistryObject<BlockEntityType<InfuserBlockEntity>> INFUSER = BE.register("infuser",
            () -> BlockEntityType.Builder.of(InfuserBlockEntity::new, BlockInit.INFUSER.get()).build(null));

    public static final RegistryObject<BlockEntityType<CenterPedestalBlockEntity>> RITUAL_PEDESTAL = BE.register("ritual",
            () -> BlockEntityType.Builder.of(CenterPedestalBlockEntity::new, BlockInit.RITUAL_PEDESTAL.get()).build(null));

    public static final RegistryObject<BlockEntityType<WallLampEntity>> WALL = BE.register("wall_lamp",
            () -> BlockEntityType.Builder.of(WallLampEntity::new, BlockInit.WALL.get()).build(null));

    public static final RegistryObject<BlockEntityType<PipeBlockEntity>> PIPE = BE.register("pipe",
            () -> BlockEntityType.Builder.of(PipeBlockEntity::new, BlockInit.PIPE.get()).build(null));

    public static final RegistryObject<BlockEntityType<IgnisVoidBlockEntity>> VOID = BE.register("void",
            () -> BlockEntityType.Builder.of(IgnisVoidBlockEntity::new, BlockInit.VOID.get()).build(null));

    public static final RegistryObject<BlockEntityType<ItemTransporterBlockEntity>> TRANSPORTER = BE.register("transporter",
            () -> BlockEntityType.Builder.of(ItemTransporterBlockEntity::new, BlockInit.TRANSPORTER.get()).build(null));
}
