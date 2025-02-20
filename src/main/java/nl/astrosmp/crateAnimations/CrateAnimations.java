package nl.astrosmp.crateAnimations;

import nl.astrosmp.crateAnimations.commands.ReloadCommand;
import nl.astrosmp.crateAnimations.listeners.PluginListener;
import nl.astrosmp.crateAnimations.opening.csgo.CustomProvider;
import org.bukkit.plugin.java.JavaPlugin;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.opening.OpeningManager;

public final class CrateAnimations extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Initialize ConfigManager
        configManager = new ConfigManager(this);

        // Plugin startup logic
        hookIntoExcellentCrates();

        // Register the reload command
        this.getCommand("reloadcrateanimations").setExecutor(new ReloadCommand(this));

        // Register the plugin listener
        getServer().getPluginManager().registerEvents(new PluginListener(this), this);

        getLogger().info("Custom crate opening registered successfully!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void hookIntoExcellentCrates() {
        CratesPlugin excellentCrates = (CratesPlugin) getServer().getPluginManager().getPlugin("ExcellentCrates");
        if (excellentCrates == null) {
            getLogger().severe("ExcellentCrates not found! This plugin requires ExcellentCrates to function.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register the custom opening
        OpeningManager openingManager = excellentCrates.getOpeningManager();
        CustomProvider customProvider = new CustomProvider(
                excellentCrates,
                configManager.getStepsAmount(),
                configManager.getStepsTick(),
                configManager.getCompletePause(),
                configManager.getSpacing(),
                configManager.getDisplayname()
        );
        openingManager.loadProvider("world_csgo", customProvider);
    }
}