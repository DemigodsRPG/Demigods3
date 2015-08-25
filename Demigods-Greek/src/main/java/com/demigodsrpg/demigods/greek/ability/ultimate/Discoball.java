package com.demigodsrpg.demigods.greek.ability.ultimate;

import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.DemigodsServer;
import com.demigodsrpg.demigods.engine.entity.player.attribute.Skill;
import com.demigodsrpg.demigods.engine.location.DemigodsLocation;
import com.demigodsrpg.demigods.engine.util.Randoms;
import com.demigodsrpg.demigods.engine.util.Spigots;
import com.demigodsrpg.demigods.engine.util.Zones;
import com.demigodsrpg.demigods.greek.ability.GreekAbility;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Set;

public class Discoball extends GreekAbility {
    private static final String NAME = "Discoball of Doom", COMMAND = "discoball";
    private static final int COST = 30, DELAY = 30, REPEAT = 4;
    private static final List<String> DETAILS = Lists.newArrayList("Spread the music while causing destruction.");
    private static final Skill.Type TYPE = Skill.Type.ULTIMATE;
    private static Set<FallingBlock> discoBalls = Sets.newHashSet();

    public Discoball(String deity) {
        super(NAME, COMMAND, deity, COST, DELAY, REPEAT, DETAILS, TYPE, null, new Predicate<Player>() {
            @Override
            public boolean apply(final Player player) {
                balls(player);

                player.sendMessage(ChatColor.YELLOW + "Dance!");

                Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new Runnable() {
                    @Override
                    public void run() {
                        player.sendMessage(ChatColor.RED + "B" + ChatColor.GOLD + "o" + ChatColor.YELLOW + "o" + ChatColor.GREEN + "g" + ChatColor.AQUA + "i" + ChatColor.LIGHT_PURPLE + "e" + ChatColor.DARK_PURPLE + " W" + ChatColor.BLUE + "o" + ChatColor.RED + "n" + ChatColor.GOLD + "d" + ChatColor.YELLOW + "e" + ChatColor.GREEN + "r" + ChatColor.AQUA + "l" + ChatColor.LIGHT_PURPLE + "a" + ChatColor.DARK_PURPLE + "n" + ChatColor.BLUE + "d" + ChatColor.RED + "!");
                    }
                }, 40);

                return true;
            }
        }, new Listener() {
            @EventHandler(priority = EventPriority.HIGHEST)
            public void onBlockChange(EntityChangeBlockEvent changeEvent) {
                if (Zones.inNoDemigodsZone(changeEvent.getBlock().getLocation())) return;

                if (changeEvent.getEntityType() != EntityType.FALLING_BLOCK) return;
                changeEvent.getBlock().setType(Material.AIR);
                FallingBlock block = (FallingBlock) changeEvent.getEntity();
                if (discoBalls.contains(block)) {
                    discoBalls.remove(block);
                    block.remove();
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                for (FallingBlock block : discoBalls) {
                    if (block != null) {
                        Location location = block.getLocation();
                        if (Zones.inNoDemigodsZone(location)) return;
                        playRandomNote(location, 2F);
                        sparkleSparkle(location);
                        destroyNearby(location);
                    }
                }
            }
        });
    }

    public static void balls(Player player) {
        for (Location location : DemigodsLocation.getCirclePoints(new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() + 30 < 256 ? player.getLocation().getBlockY() + 30 : 256, player.getLocation().getBlockZ()), 3.0, 50))
            spawnBall(location);
    }

    public static void spawnBall(Location location) {
        final FallingBlock discoBall = location.getWorld().spawnFallingBlock(location, Material.GLOWSTONE, (byte) 0);
        discoBalls.add(discoBall);
        Bukkit.getScheduler().scheduleSyncDelayedTask(DemigodsPlugin.getInst(), new BukkitRunnable() {
            @Override
            public void run() {
                discoBalls.remove(discoBall);
                discoBall.remove();
            }
        }, 600);
    }

    public static void rainbow(Player disco, Player player) {
        player.sendBlockChange(disco.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(), Material.WOOL, (byte) Randoms.generateIntRange(0, 15));
        if (DemigodsServer.isRunningSpigot())
            Spigots.playParticle(disco.getLocation(), Effect.COLOURED_DUST, 1, 0, 1, 10F, 100, 30);
    }

    public static void playRandomNote(Location location, float volume) {
        location.getWorld().playSound(location, Sound.NOTE_BASS_GUITAR, volume, (float) ((double) Randoms.generateIntRange(5, 10) / 10.0));
    }

    public static void sparkleSparkle(Location location) {
        if (DemigodsServer.isRunningSpigot()) Spigots.playParticle(location, Effect.CRIT, 1, 1, 1, 10F, 1000, 30);
    }

    public static void destroyNearby(Location location) {
        location.getWorld().createExplosion(location, 2F);
    }
}
