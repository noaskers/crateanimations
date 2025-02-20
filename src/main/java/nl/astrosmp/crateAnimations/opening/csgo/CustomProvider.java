package nl.astrosmp.crateAnimations.opening.csgo;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.OpeningProvider;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;

public class CustomProvider implements OpeningProvider {

    private final CratesPlugin plugin;
    private final int stepsAmount;
    private final long stepsTick;
    private final long completePause;
    private final double spacing;
    private final boolean displayname;

    public CustomProvider(@NotNull CratesPlugin plugin, int stepsAmount, long stepsTick, long completePause, double spacing, boolean displayname) {
        this.plugin = plugin;
        this.stepsAmount = stepsAmount;
        this.stepsTick = stepsTick;
        this.completePause = completePause;
        this.spacing = spacing;
        this.displayname = displayname;
    }

    @Override
    public @NotNull CustomOpening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable CrateKey key) {
        return new CustomOpening(this.plugin, player, source, key, stepsAmount, stepsTick, completePause, spacing, displayname);
    }
}