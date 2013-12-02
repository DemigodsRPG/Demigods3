package com.censoredsoftware.demigods.engine.data;

import com.censoredsoftware.censoredlib.trigger.Trigger;
import com.censoredsoftware.censoredlib.util.Times;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.ability.Ability;
import com.censoredsoftware.demigods.engine.battle.Battle;
import com.censoredsoftware.demigods.engine.data.util.Notifications;
import com.censoredsoftware.demigods.engine.data.util.TimedDatas;
import com.censoredsoftware.demigods.engine.deity.Deity;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.engine.util.Admins;
import com.censoredsoftware.demigods.engine.util.Configs;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.censoredsoftware.demigods.greek.structure.Altar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("deprecation")
public class ThreadManager {
    private static boolean SAVE_ALERT = Configs.getSettingBoolean("saving.console_alert");

    public static void startThreads() {
        // Start sync demigods runnable
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Demigods.PLUGIN, Util.getSyncDemigodsRunnable(), 20, 20);
        Admins.sendDebug("Main Demigods SYNC runnable enabled...");

        // Start sync demigods runnable
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Demigods.PLUGIN, Util.getAsyncDemigodsRunnable(), 20, 20);
        Admins.sendDebug("Main Demigods ASYNC runnable enabled...");

        // Start favor runnable
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Demigods.PLUGIN, Util.getFavorRunnable(), 20, (Configs.getSettingInt("regeneration_rates.favor") * 20));
        Admins.sendDebug("Favor regeneration runnable enabled...");

        // Start saving runnable
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Demigods.PLUGIN, Util.getSaveRunnable(), 20, (Configs.getSettingInt("saving.freq") * 20));

        // Enable Deity runnables
        for (Deity deity : Demigods.MYTHOS.getDeities())
            for (Ability ability : deity.getAbilities())
                if (ability.getRunnable() != null)
                    Bukkit.getScheduler().scheduleSyncRepeatingTask(Demigods.PLUGIN, ability.getRunnable(), ability.getDelay(), ability.getRepeat());
    }

    public static void stopThreads() {
        Demigods.PLUGIN.getServer().getScheduler().cancelTasks(Demigods.PLUGIN);
    }

    private static class Util {
        private final static BukkitRunnable sync, async, save, favor;

        static {
            sync = new BukkitRunnable() {
                @Override
                public void run() {
                    // Update online players
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (Zones.inNoDemigodsZone(player.getLocation())) continue;
                        DPlayer.Util.getPlayer(player).updateCanPvp();
                    }

                    // Process Triggers
                    for (Trigger trigger : Demigods.MYTHOS.getTriggers())
                        trigger.processSync();

                    // Update Battles
                    Battle.Util.updateBattles();

                    // Update Battle Particles
                    Battle.Util.updateBattleParticles();

                    // Update Atlars
                    Altar.Util.generateAltars();
                }
            };
            async = new BukkitRunnable() {
                @Override
                public void run() {
                    // Update Timed Data
                    TimedDatas.updateTimedData();

                    // Update Notifications
                    Notifications.updateNotifications();

                    // Process Triggers
                    for (Trigger trigger : Demigods.MYTHOS.getTriggers())
                        trigger.processAsync();
                }
            };
            save = new BukkitRunnable() {
                @Override
                public void run() {
                    // Save time for reference after saving
                    long time = System.currentTimeMillis();

                    // Save data
                    DataManager.save();

                    // Send the save message to the console
                    if (SAVE_ALERT)
                        Messages.info(Bukkit.getOnlinePlayers().length + " of " + DataManager.players.size() + " total players saved in " + Times.getSeconds(time) + " seconds.");
                }
            };
            favor = new BukkitRunnable() {
                @Override
                public void run() {
                    // Update Favor
                    DCharacter.Util.updateFavor();
                }
            };
        }

        /**
         * Returns the main sync Demigods runnable. Methods requiring the Bukkit API and a constant
         * update should go here.
         *
         * @return the runnable to be enabled.
         */
        public static BukkitRunnable getSyncDemigodsRunnable() {
            return sync;
        }

        /**
         * Returns the main asynchronous Demigods runnable. Methods NOT requiring the Bukkit API and a constant
         * update should go here.
         *
         * @return the runnable to be enabled.
         */
        public static BukkitRunnable getAsyncDemigodsRunnable() {
            return async;
        }

        /**
         * Returns the runnable that handles all data saving.
         *
         * @return the runnable to be enabled.
         */
        public static BukkitRunnable getSaveRunnable() {
            return save;
        }

        /**
         * Returns the favor regeneration runnable. This must be placed here due to varying favor
         * regeneration frequencies.
         *
         * @return the runnable to be enabled.
         */
        public static BukkitRunnable getFavorRunnable() {
            return favor;
        }
    }
}
