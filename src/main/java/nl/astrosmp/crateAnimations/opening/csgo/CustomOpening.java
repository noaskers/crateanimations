package nl.astrosmp.crateAnimations.opening.csgo;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.world.WorldOpening;
import su.nightexpress.excellentcrates.opening.OpeningUtils;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.util.LocationUtil;
import su.nightexpress.nightcore.util.bukkit.NightSound;

import java.util.ArrayList;
import java.util.List;

public class CustomOpening extends WorldOpening {

    private final int stepsAmount; // Total steps for the animation
    private final long stepsTick; // Ticks between each step
    private final long maxTicks; // Total ticks for the animation
    private final double spacing; // Spacing between items
    private final boolean displayname; // Display item names
    private int step;
    private List<Item> itemDisplays; // List to hold the 3 displayed items
    private Reward reward; // The final reward
    private Location displayLocation; // Location to display the items
    private List<Reward> rewardPool; // Pool of rewards to cycle through

    public CustomOpening(@NotNull CratesPlugin plugin, @NotNull Player player, @NotNull CrateSource source, @Nullable CrateKey key, int stepsAmount, long stepsTick, long completePause, double spacing, boolean displayname) {
        super(plugin, player, source, key);
        this.stepsAmount = stepsAmount;
        this.stepsTick = stepsTick;
        this.maxTicks = (long) this.stepsAmount * this.stepsTick + completePause;
        this.spacing = spacing;
        this.displayname = displayname;
        this.itemDisplays = new ArrayList<>();
        this.rewardPool = new ArrayList<>(this.crate.getRewards(player)); // Get available rewards for the player
    }

    private void onFirstTick() {
        Block block = this.source.getBlock();
        double yAdd = this.crate.getHologramYOffset();
        Location center;
        if (block == null) {
            Location playerLoc = this.player.getEyeLocation().clone();
            Vector direction = playerLoc.getDirection();

            for (int i = 0; i < 5; ++i) {
                playerLoc.add(direction);
            }

            center = playerLoc;
        } else {
            yAdd += block.getBoundingBox().getHeight() / 2.0;
            center = block.getLocation();
        }

        this.displayLocation = LocationUtil.setCenter3D(center).add(0.0, yAdd, 0.0);
        this.hideHologram(); // Hide the hologram when the animation starts

        // Spawn 3 item displays in a row
        for (int i = 0; i < 3; i++) {
            Location itemLoc = this.displayLocation.clone().add((i - 1) * spacing, 0, 0);  // Spread items horizontally
            Item itemDisplay = this.player.getWorld().spawn(itemLoc, Item.class, (item) -> {
                item.setVelocity(new Vector());
                item.setPersistent(false);
                item.setCustomNameVisible(displayname);
                item.setGravity(false);
                item.setPickupDelay(Integer.MAX_VALUE);
                item.setUnlimitedLifetime(true);
                item.setInvulnerable(true);
            });
            this.itemDisplays.add(itemDisplay);
        }
    }

    @Override
    public void instaRoll() {
        this.step = this.stepsAmount - 1;
        this.roll();
        if (this.tickCount > 0L) {
            this.displayRoll();
        }

        this.tickCount = this.maxTicks;
        this.stop();
    }

    @Override
    protected void onStart() {
        // Code to execute when the opening starts
    }

    @Override
    protected void onTick() {
        if (this.tickCount == 0L) {
            this.onFirstTick();
        }

        if (this.tickCount % this.stepsTick == 0L && this.step < this.stepsAmount) {
            this.roll();
            this.displayRoll();
        }
    }

    @Override
    protected void onComplete() {
        if (this.reward != null) {
            this.reward.give(this.player);
            CrateUtils.callRewardObtainEvent(this.player, this.reward);
        }
    }

    @Override
    protected void onStop() {
        // Remove all item displays
        for (Item itemDisplay : this.itemDisplays) {
            itemDisplay.remove();
        }
        this.itemDisplays.clear();

        this.showHologram(); // Show the hologram when the animation stops
        super.onStop();
    }

    @Override
    public boolean isCompleted() {
        return this.tickCount >= this.maxTicks;
    }

    private void roll() {
        if (this.step == this.stepsAmount - 1) {
            // Final step: set the reward
            this.reward = this.crate.rollReward(this.player);
            this.setRefundable(false);
        }
        this.step++;
    }

    private void displayRoll() {
        for (int i = 0; i < 3; i++) {
            Item itemDisplay = this.itemDisplays.get(i);
            Reward currentReward;

            if (this.step == this.stepsAmount && i == 1) {
                // Set the middle item to the final reward
                currentReward = this.reward;
            } else {
                // Cycle through rewards for the spinning effect
                currentReward = this.rewardPool.get((this.step + i) % this.rewardPool.size());
            }

            ItemStack itemStack = currentReward.getPreviewItem();
            itemDisplay.setItemStack(itemStack);
            itemDisplay.setCustomName(currentReward.getNameTranslated());
        }

        // Play sounds
        NightSound.of(Sound.UI_BUTTON_CLICK, 0.5F).play(this.displayLocation);
        if (this.step == this.stepsAmount) {
            NightSound.of(Sound.ENTITY_GENERIC_EXPLODE, 0.7F).play(this.displayLocation);
            OpeningUtils.createFirework(this.displayLocation);
        }
    }
}