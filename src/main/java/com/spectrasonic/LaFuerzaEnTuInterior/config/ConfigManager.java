package com.spectrasonic.LaFuerzaEnTuInterior.config;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;
    
    private int sharpnessLevel;
    private int speedLevel;
    private int strengthLevel;
    
    private int pointGain;
    private int pointLoss;
    
    private List<Location> respawnPoints;
    
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        reload();
    }
    
    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        
        if (!config.contains("sharpness")) {
            config.set("sharpness", 255);
        }
        
        if (!config.contains("speed")) {
            config.set("speed", 5);
        }
        
        if (!config.contains("strength")) {
            config.set("strength", 255);
        }
        
        if (!config.contains("point_gain")) {
            config.set("point_gain", 1);
        }
        
        if (!config.contains("point_loose")) {
            config.set("point_loose", 1);
        }
        
        if (!config.contains("respawn_points")) {
            ConfigurationSection respawnSection = config.createSection("respawn_points");
            World world = plugin.getServer().getWorlds().get(0);
            
            respawnSection.set("respawn1", createLocationMap(world.getName(), 0, 0, 0));
            respawnSection.set("respawn2", createLocationMap(world.getName(), 100, 100, 100));
            
            plugin.saveConfig();
        }
        
        loadConfig();
    }
    
    private void loadConfig() {
        this.sharpnessLevel = config.getInt("sharpness", 255);
        this.speedLevel = config.getInt("speed", 5);
        this.strengthLevel = config.getInt("strength", 255);
        
        this.pointGain = config.getInt("point_gain", 1);
        this.pointLoss = config.getInt("point_loose", 1);
        
        loadRespawnPoints();
    }
    
    private void loadRespawnPoints() {
        this.respawnPoints = new ArrayList<>();
        ConfigurationSection respawnSection = config.getConfigurationSection("respawn_points");
        
        if (respawnSection != null) {
            for (String key : respawnSection.getKeys(false)) {
                ConfigurationSection pointSection = respawnSection.getConfigurationSection(key);
                
                if (pointSection != null) {
                    double x = pointSection.getDouble("x", 0);
                    double y = pointSection.getDouble("y", 0);
                    double z = pointSection.getDouble("z", 0);
                    
                    World world = plugin.getServer().getWorlds().get(0);
                    
                    Location location = new Location(world, x, y, z);
                    respawnPoints.add(location);
                }
            }
        }
    }
    
    public List<Location> getRespawnLocations() {
        List<Location> respawnPoints = new ArrayList<>();
        if (config.isConfigurationSection("respawn_points")) {
            for (String key : config.getConfigurationSection("respawn_points").getKeys(false)) {
                Map<String, Object> point = config.getConfigurationSection("respawn_points." + key).getValues(false);

                double x = ((Number) point.get("x")).doubleValue();
                double y = ((Number) point.get("y")).doubleValue();
                double z = ((Number) point.get("z")).doubleValue();

                Location loc = new Location(Bukkit.getWorld("world"), x, y, z);
                respawnPoints.add(loc);
            }
        }
        return respawnPoints;
    }

    private ConfigurationSection createLocationMap(String worldName, double x, double y, double z) {
        ConfigurationSection section = config.createSection("temp");
        section.set("x", x);
        section.set("y", y);
        section.set("z", z);
        
        ConfigurationSection result = section.getConfigurationSection("");
        config.set("temp", null);
        
        return result;
    }
}
