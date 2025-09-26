package com.example.frozenocean;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = FrozenOceanMod.MODID, version = FrozenOceanMod.VERSION, name = "Frozen Ocean & Polar Bears")
public class FrozenOceanMod {
    public static final String MODID = "frozenocean";
    public static final String VERSION = "1.0";
    
    public static BiomeGenBase frozenOcean;
    public static int polarBearSpawnRate = 5;
    
    @SidedProxy(clientSide = "com.example.frozenocean.ClientProxy", serverSide = "com.example.frozenocean.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Load configuration
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        
        polarBearSpawnRate = config.get("spawning", "polarBearSpawnRate", 5, 
                "Polar bear spawn probability (higher = more rare)").getInt();
        
        config.save();
        
        // Register Frozen Ocean biome
        frozenOcean = new com.example.frozenocean.biome.BiomeGenFrozenOcean(150).setBiomeName("Frozen Ocean").setEnableSnow();
        
        // Add to ocean biomes list
        BiomeManager.oceanBiomes.add(frozenOcean);
        BiomeManager.addSpawnBiome(frozenOcean);
        
        // Register Polar Bear entity
        EntityRegistry.registerGlobalEntityID(com.example.frozenocean.entity.EntityPolarBear.class, "PolarBear", 
                EntityRegistry.findGlobalUniqueEntityId(), 0xFFFFFF, 0x888888);
        EntityRegistry.registerModEntity(com.example.frozenocean.entity.EntityPolarBear.class, "PolarBear", 0, this, 64, 3, true);
        
        // Add spawn for polar bears in frozen ocean
        EntityRegistry.addSpawn(com.example.frozenocean.entity.EntityPolarBear.class, polarBearSpawnRate, 1, 2, 
                net.minecraft.entity.EnumCreatureType.creature, frozenOcean);
        
        // Add spawn for polar bears in other snowy biomes (excluding taiga variants)
        addPolarBearSpawns();
    }
    
    private void addPolarBearSpawns() {
        // Higher spawn rates in frozen ocean
        EntityRegistry.addSpawn(com.example.frozenocean.entity.EntityPolarBear.class, 15, 2, 4, 
                net.minecraft.entity.EnumCreatureType.creature, frozenOcean);
        
        // Spawn in Ice Plains
        EntityRegistry.addSpawn(com.example.frozenocean.entity.EntityPolarBear.class, 8, 1, 2, 
                net.minecraft.entity.EnumCreatureType.creature, BiomeGenBase.icePlains);
        
        // Spawn in Ice Mountains
        EntityRegistry.addSpawn(com.example.frozenocean.entity.EntityPolarBear.class, 10, 1, 3, 
                net.minecraft.entity.EnumCreatureType.creature, BiomeGenBase.iceMountains);
        
        // Spawn in Cold Beach
        EntityRegistry.addSpawn(com.example.frozenocean.entity.EntityPolarBear.class, 6, 1, 2, 
                net.minecraft.entity.EnumCreatureType.creature, BiomeGenBase.coldBeach);
        
        // Spawn in Cold Taiga
        EntityRegistry.addSpawn(com.example.frozenocean.entity.EntityPolarBear.class, 5, 1, 2, 
                net.minecraft.entity.EnumCreatureType.creature, BiomeGenBase.coldTaiga);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        // Register world generator
        GameRegistry.registerWorldGenerator(new com.example.frozenocean.world.FrozenOceanWorldGenerator(), 1);
        
        // Register renderers
        proxy.registerRenderers();
        
        System.out.println("Frozen Ocean Mod initialized successfully!");
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        // Register commands
        com.example.frozenocean.command.ModCommands.registerCommands(event);
        System.out.println("Frozen Ocean Mod commands registered!");
    }
}
