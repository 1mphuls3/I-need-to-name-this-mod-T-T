package com.Imphuls3.abigne.core.systems.spell.effect;

import com.Imphuls3.abigne.common.effect.EffectsRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Cauterize {

    public static void cast(Level level, Player player) {
        if(player.hasEffect(EffectsRegistry.BLEED.get())) {
            player.hurt(DamageSource.MAGIC, 2F);
            player.removeEffect(EffectsRegistry.BLEED.get());
        }
    }

    private void render(PoseStack stackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

    }
}
