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
    private int pointGainRound1;
    private int pointGainRound2;
    private int pointGainRound3;
    private int pointLossRound1;
    private int pointLossRound2;
    private int pointLossRound3;

    public PointManager(Main plugin) {
        this.plugin = plugin;
        this.pointsManager = new PointsManager(plugin);
        reload();
    }

    public void reload() {
        this.pointGainRound1 = plugin.getConfigManager().getConfig().getInt("add_points.round_1", 10);
        this.pointGainRound2 = plugin.getConfigManager().getConfig().getInt("add_points.round_2", 20);
        this.pointGainRound3 = plugin.getConfigManager().getConfig().getInt("add_points.round_3", 30);
        this.pointLossRound1 = plugin.getConfigManager().getConfig().getInt("substract_points.round_1", 5);
        this.pointLossRound2 = plugin.getConfigManager().getConfig().getInt("substract_points.round_2", 15);
        this.pointLossRound3 = plugin.getConfigManager().getConfig().getInt("substract_points.round_3", 25);
    }

    public void addPoints(Player player, int points) {
        if (points > 0) {
            pointsManager.addPoints(player, points);
            MessageUtils.sendActionBar(player, "<green><b>+" + points + " puntos</green>");
            SoundUtils.playerSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        } else {
            pointsManager.subtractPoints(player, Math.abs(points));
            MessageUtils.sendActionBar(player, "<red><b>" + points + " puntos</red>");
            SoundUtils.playerSound(player, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.0f);
        }
    }

    public void addKillPoints(Player killer, int round) {
        int points = switch (round) {
            case 1 -> pointGainRound1;
            case 2 -> pointGainRound2;
            case 3 -> pointGainRound3;
            default -> 0;
        };
        addPoints(killer, points);
    }

    public void addDeathPoints(Player victim, int round) {
        int points = switch (round) {
            case 1 -> -pointLossRound1;
            case 2 -> -pointLossRound2;
            case 3 -> -pointLossRound3;
            default -> 0;
        };
        addPoints(victim, points);
    }
}
