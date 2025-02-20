package nl.astrosmp.crateAnimations;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final JavaPlugin plugin;
    private double spacing;
    private boolean displayname;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        spacing = config.getDouble("test.distance-between", 1.5);
        displayname = config.getBoolean("test.display-name", true);
    }

    public double getSpacing() {
        return spacing;
    }
    public boolean getDisplayname() {
        return displayname;
    }
}