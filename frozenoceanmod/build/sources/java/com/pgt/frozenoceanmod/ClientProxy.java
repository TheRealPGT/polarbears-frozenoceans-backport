package com.example.frozenocean;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderers() {
        // Register Polar Bear renderer with our custom model
        RenderingRegistry.registerEntityRenderingHandler(
            com.example.frozenocean.entity.EntityPolarBear.class, 
            new com.example.frozenocean.render.RenderPolarBear(
                new com.example.frozenocean.render.ModelPolarBear(), 
                0.7F
            )
        );
        
        System.out.println("Polar bear renderer registered successfully!");
    }
}
