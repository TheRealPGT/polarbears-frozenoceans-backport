package com.example.frozenocean.command;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class ModCommands {
    public static void registerCommands(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandFindFrozenOcean());
    }
}
