package com.spectrasonic.LaFuerzaEnTuInterior.events;

import com.spectrasonic.LaFuerzaEnTuInterior.Main;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Getter
public class GameEvents implements Listener {
    private final Main plugin;
    
    public GameEvents(Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Si el juego está en curso y el jugador está en modo aventura, prepararlo
        if (plugin.getGameManager().isGameRunning() && player.getGameMode() == GameMode.ADVENTURE) {
            plugin.getGameManager().preparePlayer(player);
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Podríamos implementar lógica adicional si fuera necesario
    }
}
