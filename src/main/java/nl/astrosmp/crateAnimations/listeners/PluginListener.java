package nl.astrosmp.crateAnimations.listeners;

import nl.astrosmp.crateAnimations.CrateAnimations;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import su.nightexpress.excellentcrates.CratesPlugin;

public class PluginListener implements Listener {

    private final CrateAnimations plugin;

    public PluginListener(CrateAnimations plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin() instanceof CratesPlugin) {
            plugin.hookIntoExcellentCrates();
        }
    }
}