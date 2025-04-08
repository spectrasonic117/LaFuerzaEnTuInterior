package com.spectrasonic.LaFuerzaEnTuInterior.game;

import com.spectrasonic.LaFuerzaEnTuInterior.Main;
import com.spectrasonic.Utils.ItemBuilder;
import com.spectrasonic.Utils.MessageUtils;
import com.spectrasonic.Utils.SoundUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
public class GameManager {
    private final Main plugin;
    private final SpawnManager spawnManager;
    private boolean gameRunning;
    
    public GameManager(Main plugin) {
        this.plugin = plugin;
        this.spawnManager = new SpawnManager(plugin);
        this.gameRunning = false;
    }
    
    public void startGame() {
        if (gameRunning) {
            return;
        }
        
        gameRunning = true;
        
        // Activar keepInventory
        for (var world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.KEEP_INVENTORY, true);
        }
        
        // Preparar a todos los jugadores
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.ADVENTURE) {
                preparePlayer(player);
            }
        }
        
        // MessageUtils.broadcastTitle("<gold>¡La Fuerza Interior!</gold>", 
        //         "<yellow>¡El juego ha comenzado!</yellow>", 1, 1, 1);
        // MessageUtils.sendBroadcastMessage("<gold>¡El juego La Fuerza Interior ha comenzado!</gold>");
        // SoundUtils.broadcastPlayerSound(Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
    }
    
    public void stopGame() {
        if (!gameRunning) {
            return;
        }
        
        gameRunning = false;
        
        // Restaurar keepInventory a su valor original (false)
        for (var world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.KEEP_INVENTORY, false);
        }
        
        // Limpiar a todos los jugadores
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SURVIVAL) {
                cleanupPlayer(player);
            }
        }
        
        // MessageUtils.broadcastTitle("<gold>¡La Fuerza Interior!</gold>", 
                // "<red>¡El juego ha terminado!</red>", 1, 3, 1);
        // MessageUtils.sendBroadcastMessage("<gold>¡El juego La Fuerza Interior ha terminado!</gold>");
        SoundUtils.broadcastPlayerSound(Sound.ENTITY_WITHER_DEATH, 1.0f, 1.0f);
    }
    
    public void preparePlayer(Player player) {
        // Limpiar inventario y efectos
        player.getInventory().clear();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        
        // Obtener valores de la configuración
        int sharpnessLevel = plugin.getConfigManager().getSharpnessLevel();
        int speedLevel = plugin.getConfigManager().getSpeedLevel() - 1; // Ajustar para el nivel de poción (0-255)
        int strengthLevel = plugin.getConfigManager().getStrengthLevel() - 1; // Ajustar para el nivel de poción (0-255)
        
        // Dar espada de diamante con Sharpness configurado
        ItemStack sword = ItemBuilder.setMaterial("DIAMOND_SWORD")
                .setName("<gold>Espada Laser</gold>")
                .addEnchantment("sharpness", sharpnessLevel)
                .setFlag("HIDE_ENCHANTS")
                .setUnbreakable(true)
                .build();
        
        player.getInventory().addItem(sword);
        
        // Aplicar efectos según la configuración
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speedLevel, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, strengthLevel, false, false, true));
        
        // Cambiar a modo supervivencia
        player.setGameMode(GameMode.SURVIVAL);
        
        // Teletransportar a un punto de spawn aleatorio
        player.teleport(spawnManager.getRandomSpawnPoint());
        
        // Restaurar salud y comida
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20);
    }
    
    public void cleanupPlayer(Player player) {
        // Limpiar inventario
        player.getInventory().clear();
        
        // Eliminar todos los efectos
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        
        // Cambiar a modo aventura
        player.setGameMode(GameMode.ADVENTURE);
        
        // Restaurar salud y comida
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20);
    }
    
    public void handlePlayerDeath(Player victim, Player killer) {
        if (!gameRunning) {
            return;
        }
        
        // Otorgar puntos
        if (killer != null) {
            plugin.getPointManager().addKillPoints(killer);
            MessageUtils.sendMessage(killer, "<green>¡Has eliminado a " + victim.getName() + "!</green>");
        }
        
        plugin.getPointManager().addDeathPoints(victim);
        
        // Restaurar al jugador
        victim.setHealth(victim.getMaxHealth());
        victim.teleport(spawnManager.getRandomSpawnPoint());
    }
}
