package com.example.frozenocean.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.util.DamageSource;

public class EntityPolarBear extends EntityAnimal {
    public EntityPolarBear(World world) {
        super(world);
        this.setSize(1.4F, 1.4F);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 2.0D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(3, new EntityAITempt(this, 1.25D, net.minecraft.init.Items.fish, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.25D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }
    
    @Override
    public boolean isAIEnabled() {
        return true;
    }
    
    // FIXED SOUND METHODS - Use existing Minecraft sounds
    @Override
    protected String getLivingSound() {
        return "mob.cow.say"; // Use cow sounds as placeholder
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.cow.hurt"; // Use cow hurt sounds
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.cow.hurt"; // Use cow hurt sound for death
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }
    
    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        return 3 + this.rand.nextInt(2);
    }
    
    @Override
    protected void dropFewItems(boolean recentlyHit, int looting) {
        int j = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + looting);
        
        for (int k = 0; k < j; ++k) {
            this.dropItem(net.minecraft.init.Items.fish, 1);
        }
        
        if (this.rand.nextFloat() < 0.1F) {
            this.dropItem(net.minecraft.init.Items.fish, 1);
        }
    }
    
    @Override
    public EntityAgeable createChild(EntityAgeable partner) {
        return new EntityPolarBear(this.worldObj);
    }
}
