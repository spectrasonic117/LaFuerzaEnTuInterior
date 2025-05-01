package com.spectrasonic.LaFuerzaEnTuInterior.player;

import com.spectrasonic.LaFuerzaEnTuInterior.Main;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

@Getter
public class PlayerManager implements Listener {
    private final Main plugin;

    public PlayerManager(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!plugin.getGameManager().isGameRunning()) {
            return;
        }

        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        // Cancelar mensaje de muerte y drops
        event.setDeathMessage(null);
        event.setDroppedExp(0);
        // Manejar la muerte del jugador
        plugin.getGameManager().handlePlayerDeath(victim, killer);

        // Hacer que el jugador respawnee inmediatamente
        plugin.getServer().getScheduler().runTask(plugin, () -> victim.spigot().respawn());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!plugin.getGameManager().isGameRunning()) {
            return;
        }

        Player player = event.getPlayer();

        // Establecer el punto de respawn a un punto aleatorio
        event.setRespawnLocation(plugin.getGameManager().getSpawnManager().getRandomSpawnPoint());

        // Restaurar efectos y equipamiento
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getGameManager().preparePlayer(player);
        }, 1L);
    }
}
