package com.example.frozenocean.biome;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenIceSpike extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        // Simple ice spike generation
        while (world.isAirBlock(x, y, z) && y > 2) {
            y--;
        }
        
        if (world.getBlock(x, y, z) != Blocks.snow) {
            return false;
        }
        
        y += random.nextInt(4);
        int height = random.nextInt(4) + 7;
        int width = height / 4 + random.nextInt(2);
        
        if (width > 1 && random.nextInt(60) == 0) {
            y += 10 + random.nextInt(30);
        }
        
        for (int i = 0; i < height; ++i) {
            float f = (1.0F - (float)i / (float)height) * width;
            int j = (int)Math.ceil(f);
            
            for (int k = -j; k <= j; ++k) {
                float f1 = (float)Math.abs(k) - 0.25F;
                
                for (int l = -j; l <= j; ++l) {
                    float f2 = (float)Math.abs(l) - 0.25F;
                    
                    if ((k == 0 && l == 0 || f1 * f1 + f2 * f2 <= f * f) && 
                        (k != -j && k != j && l != -j && l != j || random.nextFloat() <= 0.75F)) {
                        Block block = world.getBlock(x + k, y + i, z + l);
                        
                        if (block == Blocks.air || block == Blocks.snow || block == Blocks.ice) {
                            this.setBlockAndNotifyAdequately(world, x + k, y + i, z + l, Blocks.packed_ice, 0);
                        }
                        
                        if (i != 0 && j > 1) {
                            block = world.getBlock(x + k, y - i, z + l);
                            
                            if (block == Blocks.air || block == Blocks.snow || block == Blocks.ice) {
                                this.setBlockAndNotifyAdequately(world, x + k, y - i, z + l, Blocks.packed_ice, 0);
                            }
                        }
                    }
                }
            }
        }
        
        return true;
    }
}
