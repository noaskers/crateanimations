package nl.astrosmp.crateAnimations.commands;

import nl.astrosmp.crateAnimations.CrateAnimations;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final CrateAnimations plugin;

    public ReloadCommand(CrateAnimations plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reloadcrateanimations")) {
            plugin.reloadConfig();
            plugin.getConfigManager().loadConfig();

            // Re-hook into ExcellentCrates
            plugin.hookIntoExcellentCrates();

            sender.sendMessage("CrateAnimations configuration reloaded.");
            return true;
        }
        return false;
    }
}