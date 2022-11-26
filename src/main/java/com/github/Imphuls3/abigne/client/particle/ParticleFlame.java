package com.github.Imphuls3.abigne.client.particle;

import com.github.Imphuls3.abigne.client.event.RenderHandler;
import com.github.Imphuls3.abigne.core.registry.RenderTypeRegistry;
import com.github.Imphuls3.abigne.core.registry.ShaderRegistry;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ParticleFlame extends TextureSheetParticle {
    public float colorR = 0;
    public float colorG = 0;
    public float colorB = 0;
    public float initScale = 0;
    public float initAlpha = 0;
    public SpriteSet spriteSet;

    public boolean disableDepthTest;

    public ParticleFlame(ClientLevel worldIn, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float a, float scale, int lifetime, SpriteSet sprite, boolean disableDepthTest) {
        super(worldIn, x, y, z, 0, 0, 0);
        this.hasPhysics = false;

        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
        if (this.colorR > 1.0) {
            this.colorR = this.colorR / 255.0f;
        }
        if (this.colorG > 1.0) {
            this.colorG = this.colorG / 255.0f;
        }
        if (this.colorB > 1.0) {
            this.colorB = this.colorB / 255.0f;
        }
        this.setColor(colorR, colorG, colorB);
        this.lifetime = (int) ((float) lifetime * 0.5f);
        this.quadSize = 0;
        this.initScale = scale;
        this.xd = vx * 2.0f;
        this.yd = vy * 2.0f;
        this.zd = vz * 2.0f;
        this.initAlpha = a;
        this.spriteSet = sprite;
        setSpriteFromAge(sprite);
        this.disableDepthTest = disableDepthTest;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return RenderTypeRegistry.ParticleRenderTypes.ADDITIVE_PARTICLE_RENDERTYPE;
    }

    @Override
    public void render(VertexConsumer buffer, Camera info, float ticks) {
        super.render(RenderHandler.getDelayedRender().getBuffer(RenderTypeRegistry.ADDITIVE_PARTICLE), info, ticks);
    }

    @Override
    public int getLightColor(float pTicks) {
        return 255;
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(spriteSet);
        if (new Random().nextInt(6) == 0) {
            this.age++;
        }
        float lifeCoeff = (float) this.age / (float) this.lifetime;
        this.quadSize = initScale - initScale * lifeCoeff;
        this.alpha = initAlpha * (1.0f - lifeCoeff);
    }

    @Override
    public boolean isAlive() {
        return this.age < this.lifetime;
    }
}
