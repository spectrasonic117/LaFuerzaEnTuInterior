package com.spectrasonic.LaFuerzaEnTuInterior.game;

import com.spectrasonic.LaFuerzaEnTuInterior.Main;
import org.bukkit.Location;
import lombok.Getter;
import java.util.List;
import java.util.Random;

@Getter
public class SpawnManager {
    
    private final Main plugin;
    private final Random random;
    private List<Location> respawnPoints;

    public SpawnManager(Main plugin) {
        this.plugin = plugin;
        this.random = new Random();
        this.respawnPoints = plugin.getConfigManager().getRespawnPoints();
    }

    public void reload() {
        this.respawnPoints = plugin.getConfigManager().getRespawnPoints();
    }

    public Location getRandomSpawnPoint() {
        if (respawnPoints == null || respawnPoints.isEmpty()) {
            return plugin.getServer().getWorlds().get(0).getSpawnLocation();
        }
        return respawnPoints.get(random.nextInt(respawnPoints.size()));
    }
}
