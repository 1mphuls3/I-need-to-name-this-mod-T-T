package com.Imphuls3.abigne.common.effect;

import com.Imphuls3.abigne.AbIgne;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionsRegistry {
    public static DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, AbIgne.MODID);

    public static final RegistryObject<Potion> BLEED_POTION = POTIONS.register("bleed_potion",
            () -> new Potion(new MobEffectInstance(EffectsRegistry.BLEED.get(), 200, 0)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
