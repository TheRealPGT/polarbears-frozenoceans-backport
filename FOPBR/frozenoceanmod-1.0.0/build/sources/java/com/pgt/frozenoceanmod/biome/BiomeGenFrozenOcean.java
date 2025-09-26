package com.example.frozenocean.biome;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeGenFrozenOcean extends BiomeGenBase {
    public BiomeGenFrozenOcean(int id) {
        super(id);
        this.spawnableCreatureList.clear();
        this.topBlock = net.minecraft.init.Blocks.ice;
        this.fillerBlock = net.minecraft.init.Blocks.packed_ice;
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.flowersPerChunk = 0;
        this.theBiomeDecorator.grassPerChunk = 0;
        this.enableSnow = true;
        this.setColor(10518688);
        this.setTemperatureRainfall(0.0F, 0.5F); // Very cold temperature
        this.setHeight(height_Oceans);
        
        // Add polar bear spawning to make them more common
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(
            com.example.frozenocean.entity.EntityPolarBear.class, 
            10,  // spawn weight
            1,   // min group
            2    // max group
        ));
    }
    
    public WorldGenerator getRandomWorldGenForTrees(java.util.Random random) {
        return new WorldGenIceSpike();
    }
}
