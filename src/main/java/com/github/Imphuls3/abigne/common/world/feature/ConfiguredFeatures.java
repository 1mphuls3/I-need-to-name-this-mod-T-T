package com.github.Imphuls3.abigne.common.world.feature;

import com.github.Imphuls3.abigne.AbIgne;
import com.github.Imphuls3.abigne.core.registry.BlockRegistry;
import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class ConfiguredFeatures {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, AbIgne.MODID);

    public static final Supplier<List<OreConfiguration.TargetBlockState>> OVERWORLD_SALT_DEPOSIT = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.NATURAL_STONE, BlockRegistry.SALT_BLOCK.get().defaultBlockState())));
    public static final RegistryObject<ConfiguredFeature<?, ?>> SALT_BLOCK = CONFIGURED_FEATURES.register("salt_block",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(OVERWORLD_SALT_DEPOSIT.get(), 48)));

    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
    }
}
