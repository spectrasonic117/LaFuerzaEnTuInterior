package com.spectrasonic.LaFuerzaEnTuInterior.game;

import com.spectrasonic.LaFuerzaEnTuInterior.Main;
import com.spectrasonic.Utils.ItemBuilder;
import com.spectrasonic.Utils.MessageUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
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

        plugin.getServer().getWorlds().forEach(world -> {
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            world.setGameRule(GameRule.KEEP_INVENTORY, true);
        });

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.ADVENTURE) {
                preparePlayer(player);
            }
        }
    }

    public void stopGame() {
        if (!gameRunning) {
            return;
        }

        gameRunning = false;

        plugin.getServer().getWorlds().forEach(world -> {
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
            world.setGameRule(GameRule.KEEP_INVENTORY, false);
        });

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.ADVENTURE) {
                cleanupPlayer(player);
            }
        }
    }

    public void preparePlayer(Player player) {
        player.getInventory().clear();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        int sharpnessLevel = plugin.getConfigManager().getSharpnessLevel();
        int speedLevel = plugin.getConfigManager().getSpeedLevel() - 1;
        int strengthLevel = plugin.getConfigManager().getStrengthLevel() - 1;

        ItemStack sword = ItemBuilder.setMaterial("DIAMOND_SWORD")
                .setName("<gold>Espada Laser</gold>")
                .addEnchantment("sharpness", sharpnessLevel)
                .setFlag("HIDE_ENCHANTS")
                .setUnbreakable(true)
                .setCustomModelData(101)
                .build();

        player.getInventory().addItem(sword);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speedLevel, false, false, true));
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, strengthLevel, false, false, true));

        player.setGameMode(GameMode.SURVIVAL);

        // player.teleport(spawnManager.getRandomSpawnPoint());

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20);
    }

    /**
     * Cleans up a player after the game
     *
     * @param player Player to clean up
     */
    public void cleanupPlayer(Player player) {
        player.getInventory().clear();

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.setGameMode(GameMode.ADVENTURE);

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20);
    }

    public void handlePlayerDeath(Player victim, Player killer) {
        if (!gameRunning) {
            return;
        }

        if (killer != null) {
            plugin.getPointManager().addKillPoints(killer);
            MessageUtils.sendMessage(killer, "<green>¡Eliminaste a " + victim.getName() + "!</green>");
        }

        plugin.getPointManager().addDeathPoints(victim);

        victim.setHealth(victim.getMaxHealth());
        victim.teleport(spawnManager.getRandomSpawnPoint());
    }
}
