package com.spectrasonic.LaFuerzaEnTuInterior.player;

import com.spectrasonic.LaFuerzaEnTuInterior.Main;
import com.spectrasonic.Utils.MessageUtils;
import com.spectrasonic.Utils.PointsManager;
import com.spectrasonic.Utils.SoundUtils;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Getter
public class PointManager {

    private final Main plugin;
    private final PointsManager pointsManager;
    private int pointGain;
    private int pointLoss;
    
    public PointManager(Main plugin) {
        this.plugin = plugin;
        this.pointsManager = new PointsManager(plugin);
        reload();
    }
    
    public void reload() {
        this.pointGain = plugin.getConfigManager().getPointGain();
        this.pointLoss = plugin.getConfigManager().getPointLoss();
    }
    
    public void addPoints(Player player, int points) {
        if (points > 0) {
            pointsManager.addPoints(player, points);
            MessageUtils.sendActionBar(player, "<green><b>+" + points + " puntos</green>");
            SoundUtils.playerSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        } else {
            pointsManager.subtractPoints(player, Math.abs(points));
            MessageUtils.sendActionBar(player, "<red><b>-" + points + " puntos</red>");
            SoundUtils.playerSound(player, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.0f);
        }
    }
        
    public void addKillPoints(Player killer) {
        addPoints(killer, pointGain);
    }
    
    public void addDeathPoints(Player victim) {
        addPoints(victim, -pointLoss);
    }
}
