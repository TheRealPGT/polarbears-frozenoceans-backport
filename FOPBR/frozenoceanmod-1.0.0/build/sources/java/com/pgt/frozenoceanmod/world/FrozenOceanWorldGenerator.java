package com.example.frozenocean.world;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class FrozenOceanWorldGenerator implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, 
            IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if (world.provider.dimensionId == 0) { // Overworld only
            int centerX = chunkX * 16 + 8;
            int centerZ = chunkZ * 16 + 8;
            
            // Get the biome at the center of this chunk
            net.minecraft.world.biome.BiomeGenBase currentBiome = world.getBiomeGenForCoords(centerX, centerZ);
            
            // Check if this is a cold biome where we should generate frozen oceans nearby
            if (isColdBiome(currentBiome) && random.nextInt(3) == 0) { // 33% chance
                generateFrozenOceanInChunk(world, chunkX, chunkZ, random);
            }
            
            // Also generate frozen oceans in regular oceans that are in cold regions
            if ((currentBiome == net.minecraft.world.biome.BiomeGenBase.ocean || 
                 currentBiome == net.minecraft.world.biome.BiomeGenBase.deepOcean) &&
                isInColdRegion(world, centerX, centerZ)) {
                
                if (random.nextInt(4) == 0) { // 25% chance to convert ocean to frozen ocean
                    convertToFrozenOcean(world, chunkX, chunkZ, random);
                }
            }
        }
    }
    
    private boolean isColdBiome(net.minecraft.world.biome.BiomeGenBase biome) {
        // List of cold biomes where frozen oceans should generate nearby
        return biome == net.minecraft.world.biome.BiomeGenBase.icePlains ||
               biome == net.minecraft.world.biome.BiomeGenBase.iceMountains ||
               biome == net.minecraft.world.biome.BiomeGenBase.coldTaiga ||
               biome == net.minecraft.world.biome.BiomeGenBase.coldBeach ||
               biome.getEnableSnow() || // Any snowy biome
               biome.temperature < 0.2F; // Any biome with low temperature
    }
    
    private boolean isInColdRegion(World world, int x, int z) {
        // Check multiple points around the area to determine if this is a cold region
        int coldBiomeCount = 0;
        int totalChecks = 9;
        
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                net.minecraft.world.biome.BiomeGenBase biome = world.getBiomeGenForCoords(x + dx * 16, z + dz * 16);
                if (isColdBiome(biome)) {
                    coldBiomeCount++;
                }
            }
        }
        
        // If at least 1/3 of the surrounding area is cold, consider this a cold region
        return coldBiomeCount >= (totalChecks / 3);
    }
    
    private void generateFrozenOceanInChunk(World world, int chunkX, int chunkZ, Random random) {
        // Determine the size and position of the frozen ocean
        int size = 2 + random.nextInt(3); // 2-4 chunk radius
        int centerOffsetX = random.nextInt(5) - 2; // -2 to +2 chunks
        int centerOffsetZ = random.nextInt(5) - 2; // -2 to +2 chunks
        
        for (int dx = -size; dx <= size; dx++) {
            for (int dz = -size; dz <= size; dz++) {
                // Check if this position is within the circular area
                if (dx * dx + dz * dz <= size * size) {
                    int targetChunkX = chunkX + dx + centerOffsetX;
                    int targetChunkZ = chunkZ + dz + centerOffsetZ;
                    
                    // Convert this chunk to frozen ocean
                    convertToFrozenOcean(world, targetChunkX, targetChunkZ, random);
                }
            }
        }
    }
    
    private void convertToFrozenOcean(World world, int chunkX, int chunkZ, Random random) {
        // Convert regular ocean to frozen ocean in this chunk
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX * 16 + x;
                int worldZ = chunkZ * 16 + z;
                
                // Get the current biome
                net.minecraft.world.biome.BiomeGenBase currentBiome = world.getBiomeGenForCoords(worldX, worldZ);
                
                // Only convert ocean biomes or beaches
                if (currentBiome == net.minecraft.world.biome.BiomeGenBase.ocean ||
                    currentBiome == net.minecraft.world.biome.BiomeGenBase.deepOcean ||
                    currentBiome == net.minecraft.world.biome.BiomeGenBase.beach ||
                    currentBiome == net.minecraft.world.biome.BiomeGenBase.coldBeach) {
                    
                    // Set the biome to our frozen ocean
                    world.getWorldChunkManager().getBiomeGenAt(worldX, worldZ);
                    
                    // Add ice and snow to the terrain
                    if (random.nextInt(3) == 0) {
                        int y = world.getHeightValue(worldX, worldZ);
                        if (world.getBlock(worldX, y, worldZ) == net.minecraft.init.Blocks.water) {
                            world.setBlock(worldX, y, worldZ, net.minecraft.init.Blocks.ice);
                        } else if (world.isAirBlock(worldX, y + 1, worldZ)) {
                            world.setBlock(worldX, y + 1, worldZ, net.minecraft.init.Blocks.snow);
                        }
                    }
                }
            }
        }
        
        // Occasionally generate ice spikes in frozen oceans
        if (random.nextInt(8) == 0) {
            int spikeX = chunkX * 16 + random.nextInt(16);
            int spikeZ = chunkZ * 16 + random.nextInt(16);
            int spikeY = world.getHeightValue(spikeX, spikeZ);
            
            // Generate ice spike
            new com.example.frozenocean.biome.WorldGenIceSpike().generate(world, random, spikeX, spikeY, spikeZ);
        }
    }
}
