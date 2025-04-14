package com.spectrasonic.LaFuerzaEnTuInterior.game;

import com.spectrasonic.LaFuerzaEnTuInterior.Main;
import lombok.Getter;
import org.bukkit.Location;

import java.util.List;
import java.util.Random;

@Getter
public class SpawnManager {
    
    private final Main plugin;
    private final List<Location> respawnPoints;
    private final Random random;
    
    public SpawnManager(Main plugin) {
        this.plugin = plugin;
        this.respawnPoints = plugin.getConfigManager().getRespawnPoints();
        this.random = new Random();
    }
    
    public Location getRandomSpawnPoint() {
        if (respawnPoints.isEmpty()) {
            return plugin.getServer().getWorlds().get(0).getSpawnLocation();
        }
        
        return respawnPoints.get(random.nextInt(respawnPoints.size()));
        
    }
}
