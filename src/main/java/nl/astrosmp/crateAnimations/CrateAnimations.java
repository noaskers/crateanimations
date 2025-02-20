package nl.astrosmp.crateAnimations;

import nl.astrosmp.crateAnimations.opening.test.CustomProvider;
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
        CratesPlugin excellentCrates = (CratesPlugin) getServer().getPluginManager().getPlugin("ExcellentCrates");
        if (excellentCrates == null) {
            getLogger().severe("ExcellentCrates not found! This plugin requires ExcellentCrates to function.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register the custom opening
        OpeningManager openingManager = excellentCrates.getOpeningManager();
        CustomProvider customProvider = new CustomProvider(excellentCrates, 10, 7L, 20L, configManager.getSpacing(), configManager.getDisplayname()); // Adjust parameters as needed
        openingManager.loadProvider("custom_opening", customProvider);

        getLogger().info("Custom crate opening registered successfully!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}