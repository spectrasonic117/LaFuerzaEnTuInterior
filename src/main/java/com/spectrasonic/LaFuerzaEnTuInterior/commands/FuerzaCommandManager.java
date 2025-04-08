package com.spectrasonic.LaFuerzaEnTuInterior.commands;

import co.aikar.commands.PaperCommandManager;
import com.spectrasonic.LaFuerzaEnTuInterior.Main;

public class FuerzaCommandManager {
    private final Main plugin;
    private final PaperCommandManager commandManager;
    
    public FuerzaCommandManager(Main plugin) {
        this.plugin = plugin;
        this.commandManager = new PaperCommandManager(plugin);
        
        registerCommands();
    }
    
    private void registerCommands() {
        commandManager.registerCommand(new FuerzaCommand(plugin));
    }
}
