package com.example.frozenocean.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderPolarBear extends RenderLiving {
    private static final ResourceLocation polarBearTextures = 
            new ResourceLocation("frozenocean:textures/entity/polarbear.png");
    
    public RenderPolarBear(net.minecraft.client.model.ModelBase model, float shadowSize) {
        super(model, shadowSize);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return polarBearTextures;
    }
    
    // Add this method to ensure texture binding works correctly
    @Override
    protected void bindEntityTexture(Entity entity) {
        this.bindTexture(polarBearTextures);
    }
}
