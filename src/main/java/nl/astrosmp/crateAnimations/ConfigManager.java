package nl.astrosmp.crateAnimations;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final JavaPlugin plugin;
    private double spacing;
    private boolean displayname;
    private int stepsAmount;
    private long stepsTick;
    private long completePause;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();
        spacing = config.getDouble("world_csgo.distance-between", 1.5);
        displayname = config.getBoolean("world_csgo.display-name", true);
        stepsAmount = config.getInt("world_csgo.steps-amount", 15);
        stepsTick = config.getLong("world_csgo.steps-tick", 3);
        completePause = config.getLong("world_csgo.complete-pause", 40);
    }

    public double getSpacing() {
        return spacing;
    }

    public boolean getDisplayname() {
        return displayname;
    }

    public int getStepsAmount() {
        return stepsAmount;
    }

    public long getStepsTick() {
        return stepsTick;
    }

    public long getCompletePause() {
        return completePause;
    }
}