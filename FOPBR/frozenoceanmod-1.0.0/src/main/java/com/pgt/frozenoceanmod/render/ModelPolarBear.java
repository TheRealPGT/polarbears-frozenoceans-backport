package com.example.frozenocean.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelPolarBear extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer leg1;
    public ModelRenderer leg2;
    public ModelRenderer leg3;
    public ModelRenderer leg4;
    public ModelRenderer nose;

    public ModelPolarBear() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-3.5F, -3.0F, -3.0F, 7, 7, 7);
        this.head.setRotationPoint(0.0F, 10.0F, -16.0F);
        this.setRotation(head, 0.0F, 0.0F, 0.0F);
        
        this.nose = new ModelRenderer(this, 0, 32);
        this.nose.addBox(-1.5F, -1.0F, -4.0F, 3, 3, 2);
        this.nose.setRotationPoint(0.0F, 10.0F, -16.0F);
        this.setRotation(nose, 0.0F, 0.0F, 0.0F);
        
        this.body = new ModelRenderer(this, 0, 19);
        this.body.addBox(-5.0F, -8.0F, -10.0F, 10, 16, 16);
        this.body.setRotationPoint(0.0F, 10.0F, 0.0F);
        this.setRotation(body, 1.5708F, 0.0F, 0.0F);
        
        this.leg1 = new ModelRenderer(this, 50, 0);
        this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4);
        this.leg1.setRotationPoint(-3.5F, 10.0F, 8.0F);
        this.setRotation(leg1, 0.0F, 0.0F, 0.0F);
        
        this.leg2 = new ModelRenderer(this, 50, 0);
        this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4);
        this.leg2.setRotationPoint(3.5F, 10.0F, 8.0F);
        this.setRotation(leg2, 0.0F, 0.0F, 0.0F);
        
        this.leg3 = new ModelRenderer(this, 50, 0);
        this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4);
        this.leg3.setRotationPoint(-3.5F, 10.0F, -6.0F);
        this.setRotation(leg3, 0.0F, 0.0F, 0.0F);
        
        this.leg4 = new ModelRenderer(this, 50, 0);
        this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 14, 4);
        this.leg4.setRotationPoint(3.5F, 10.0F, -6.0F);
        this.setRotation(leg4, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        
        head.render(f5);
        nose.render(f5);
        body.render(f5);
        leg1.render(f5);
        leg2.render(f5);
        leg3.render(f5);
        leg4.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        
        this.head.rotateAngleY = f3 / (180F / (float)Math.PI);
        this.head.rotateAngleX = f4 / (180F / (float)Math.PI);
        
        this.leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        this.leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        this.leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }
}
