package com.spectrasonic.LaFuerzaEnTuInterior.events;

import com.spectrasonic.LaFuerzaEnTuInterior.Main;
import com.spectrasonic.LaFuerzaEnTuInterior.config.TeleportEffectUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;
import java.util.Random;

public class RespawnListener implements Listener {

    private final Main plugin;
    private final Random random = new Random();

    public RespawnListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        List<Location> respawnPoints = plugin.getConfigManager().getRespawnLocations();
        if (respawnPoints.isEmpty()) return;

        Location newRespawnLoc = respawnPoints.get(random.nextInt(respawnPoints.size()));
        event.setRespawnLocation(newRespawnLoc);

        TeleportEffectUtils.createDNAHelix(plugin, newRespawnLoc, 3.0, 20);
    }
}
