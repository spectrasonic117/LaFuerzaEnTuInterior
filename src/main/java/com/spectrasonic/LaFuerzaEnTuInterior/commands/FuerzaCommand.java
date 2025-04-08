package com.spectrasonic.LaFuerzaEnTuInterior.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.spectrasonic.LaFuerzaEnTuInterior.Main;
import com.spectrasonic.Utils.MessageUtils;
import org.bukkit.command.CommandSender;

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
        @Description("Inicia el juego")
        public void onStart(CommandSender sender) {
            if (plugin.getGameManager().isGameRunning()) {
                MessageUtils.sendMessage(sender, "<red>¡El juego ya está en curso!</red>");
                return;
            }
            
            plugin.getGameManager().startGame();
            MessageUtils.sendMessage(sender, "<green>¡Juego iniciado!</green>");
        }
        
        @Subcommand("stop")
        @Description("Detiene el juego")
        public void onStop(CommandSender sender) {
            if (!plugin.getGameManager().isGameRunning()) {
                MessageUtils.sendMessage(sender, "<red>¡El juego no está en curso!</red>");
                return;
            }
            
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
