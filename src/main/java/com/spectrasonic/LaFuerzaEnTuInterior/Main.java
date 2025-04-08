package com.spectrasonic.LaFuerzaEnTuInterior;

import com.spectrasonic.LaFuerzaEnTuInterior.commands.FuerzaCommandManager;
import com.spectrasonic.LaFuerzaEnTuInterior.config.ConfigManager;
import com.spectrasonic.LaFuerzaEnTuInterior.events.GameEvents;
import com.spectrasonic.LaFuerzaEnTuInterior.events.RespawnListener;
import com.spectrasonic.LaFuerzaEnTuInterior.game.GameManager;
import com.spectrasonic.LaFuerzaEnTuInterior.player.PlayerManager;
import com.spectrasonic.LaFuerzaEnTuInterior.player.PointManager;
import com.spectrasonic.Utils.MessageUtils;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Main extends JavaPlugin {

    private ConfigManager configManager;
    private GameManager gameManager;
    private PlayerManager playerManager;
    private PointManager pointManager;
    private GameEvents gameEvents;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        
        this.configManager = new ConfigManager(this);
        this.pointManager = new PointManager(this);
        this.playerManager = new PlayerManager(this);
        this.gameManager = new GameManager(this);
        this.gameEvents = new GameEvents(this);
        
        registerCommands();
        registerEvents();
        MessageUtils.sendStartupMessage(this);
    }

    @Override
    public void onDisable() {
        if (gameManager != null && gameManager.isGameRunning()) {
            gameManager.stopGame();
        }

        MessageUtils.sendShutdownMessage(this);
    }

    public void registerCommands() {
        new FuerzaCommandManager(this);
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(playerManager, this);
        getServer().getPluginManager().registerEvents(gameEvents, this);
        getServer().getPluginManager().registerEvents(new RespawnListener(this), this);
    }
    
    public void reload() {
        reloadConfig();
        configManager.reload();
        pointManager.reload();
    }
}
