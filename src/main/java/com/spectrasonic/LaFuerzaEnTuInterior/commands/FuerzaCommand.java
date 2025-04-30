package com.spectrasonic.LaFuerzaEnTuInterior.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.spectrasonic.LaFuerzaEnTuInterior.Main;
import com.spectrasonic.Utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("fuerza")
public class FuerzaCommand extends BaseCommand {
    private final Main plugin;

    public FuerzaCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Subcommand("game")
    @CommandPermission("fuerza.admin")
    @Description("Controla el estado del juego")
    public class GameCommands extends BaseCommand {

        @Subcommand("start")
        @Syntax("<round>")
        @CommandCompletion("1|2|3")
        @Description("Inicia el juego en una ronda específica")
        public void onStart(CommandSender sender, @Single int round) {
            if (plugin.getGameManager().isGameRunning()) {
                MessageUtils.sendMessage(sender, "<red>¡El juego ya está en curso!</red>");
                return;
            }

            if (round < 1 || round > 3) {
                MessageUtils.sendMessage(sender, "<red>¡Ronda inválida! Debe ser 1, 2 o 3.</red>");
                return;
            }

            Player player = (Player) sender;
            player.performCommand("pvp on");
            player.performCommand("multiwarp setrespawn 3_11");
            plugin.getGameManager().startGame(round);
            MessageUtils.sendMessage(sender, "<green>¡Juego iniciado en la ronda " + round + "!</green>");
        }

        @Subcommand("stop")
        @Description("Detiene el juego")
        public void onStop(CommandSender sender) {
            if (!plugin.getGameManager().isGameRunning()) {
                MessageUtils.sendMessage(sender, "<red>¡El juego no está en curso!</red>");
                return;
            }
            Player player = (Player) sender;
            player.performCommand("pvp off");

            plugin.getGameManager().stopGame();
            MessageUtils.sendMessage(sender, "<green>¡Juego detenido!</green>");
        }
    }

    @Subcommand("reload")
    @CommandPermission("fuerza.admin")
    @Description("Recarga la configuración del plugin")
    public void onReload(CommandSender sender) {
        plugin.reload();
        MessageUtils.sendMessage(sender, "<green>¡Configuración recargada correctamente!</green>");
    }
}
