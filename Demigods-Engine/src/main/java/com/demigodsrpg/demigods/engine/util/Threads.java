package com.demigodsrpg.demigods.engine.util;

import com.demigodsrpg.demigods.engine.trigger.Trigger;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Create and manage Threads easily with this helpful Util.
 */
public class Threads {
    // Private Constants
    private static ConcurrentMap<UUID, CensoredRunnable> TASKS = Maps.newConcurrentMap();
    private static ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(5);
    private static ConcurrentMap<String, Hooker> HOOKERS = Maps.newConcurrentMap();

    // - TIMED TASKS - //

    /**
     * Start a new timed repeating task for the CensoredRunnable.
     *
     * @param runnable     Special Runnable for use with this class.
     * @param delay        Delay before starting the task.
     * @param milliseconds Time between each run.
     * @return The CensoredRunnable's assigned UUID.
     */
    public static UUID newTimedTask(CensoredRunnable runnable, long delay, long milliseconds) {
        UUID id = runnable.getId();
        runnable.setFuture((ScheduledFuture<CensoredRunnable>) EXECUTOR.scheduleAtFixedRate(runnable, delay, milliseconds, TimeUnit.MILLISECONDS));
        TASKS.put(id, runnable);
        return id;
    }

    /**
     * Start a new timed delayed task for the CensoredRunnable.
     *
     * @param runnable Special Runnable for use with this class.
     * @param delay    Delay before starting the task.
     * @return The CensoredRunnable's assigned UUID.
     */
    public static UUID newTimedTask(CensoredRunnable runnable, long delay) {
        UUID id = runnable.getId();
        runnable.setFuture((ScheduledFuture<CensoredRunnable>) EXECUTOR.schedule(runnable, delay, TimeUnit.MILLISECONDS));
        TASKS.put(id, runnable);
        return id;
    }

    /**
     * Get the CensoredRunnable back.
     *
     * @param id The CensoredRunnable's id.
     * @return The CensoredRunnable.
     */
    public static CensoredRunnable getTimedTask(UUID id) {
        if (TASKS.containsKey(id)) return TASKS.get(id);
        return null;
    }

    // - HOOKERS - //

    /**
     * Get the Hooker for a plugin.
     *
     * @param plugin The plugin.
     * @return The Hooker.
     */
    public static Hooker getHooker(Plugin plugin) {
        String pluginName = plugin.getName();
        if (!HOOKERS.containsKey(pluginName)) HOOKERS.putIfAbsent(pluginName, new Hooker(plugin));
        return HOOKERS.get(pluginName);
    }

    /**
     * Register a trigger with the plugin's Hooker.
     *
     * @param plugin  The plugin.
     * @param trigger The trigger.
     */
    public static void registerTrigger(Plugin plugin, Trigger trigger) {
        getHooker(plugin).addTrigger(trigger);
    }

    /**
     * Register a collection of triggers with the plugin's Hooker.
     *
     * @param plugin   The plugin.
     * @param triggers The triggers.
     */
    public static void registerTriggers(Plugin plugin, Collection<Trigger> triggers) {
        for (Trigger trigger : triggers)
            getHooker(plugin).addTrigger(trigger);
    }

    /**
     * Unregister a trigger with the plugin's Hooker.
     *
     * @param plugin  The plugin.
     * @param trigger The trigger.
     */
    public static void unregisterTrigger(Plugin plugin, Trigger trigger) {
        getHooker(plugin).removeTrigger(trigger);
    }

    /**
     * Unregister a collection of triggers with the plugin's Hooker.
     *
     * @param plugin   The plugin.
     * @param triggers The triggers.
     */
    public static void unregisterTriggers(Plugin plugin, Collection<Trigger> triggers) {
        for (Trigger trigger : triggers)
            getHooker(plugin).removeTrigger(trigger);
    }

    /**
     * Stop the Hooker for a plugin.
     *
     * @param plugin The plugin.
     */
    public static void stopHooker(Plugin plugin) {
        getHooker(plugin).stop();
    }

    // - MISC - //

    /**
     * They're hooks for Triggers... I swear that's all they are!
     */
    static class Hooker extends CensoredRunnable {
        private String pluginName;
        private Set<Trigger> triggers;
        private int syncId = -1;

        /**
         * Add a trigger.
         *
         * @param trigger The trigger.
         */
        public void addTrigger(Trigger trigger) {
            triggers.add(trigger);
            HOOKERS.put(pluginName, this);
        }

        /**
         * Remove a trigger.
         *
         * @param trigger The trigger.
         */
        public void removeTrigger(Trigger trigger) {
            triggers.remove(trigger);
            HOOKERS.put(pluginName, this);
        }

        Hooker(Plugin plugin) {
            pluginName = plugin.getName();
            triggers = Sets.newHashSet();
            newTimedTask(this, 100, 50);
            syncId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    for (Trigger trigger : triggers)
                        trigger.processSync();
                }
            }, 2, 1);
        }

        /**
         * Stop the Hooker.
         */
        public void stop() {
            cancel();
            Bukkit.getScheduler().cancelTask(syncId);
            HOOKERS.remove(pluginName);
        }

        @Override
        public void runIt() {
            for (Trigger trigger : triggers)
                trigger.processAsync();
        }
    }

    /**
     * According to the JavaDocs for Thread, nobody should ever use the stop() method.
     * It is instead recommended that everybody simply return from the run method.
     * To deal with this, the CensoredRunnable class was created.
     * <p/>
     * Create a new Thread with the newThread() method, passing in one of these, and you're done.
     */
    public static abstract class CensoredRunnable implements Runnable {
        private final UUID id;
        private volatile ScheduledFuture<CensoredRunnable> future;
        private boolean stop;

        /**
         * Special Runnable for use with com.censoredsoftware.censoredlib.util.Threads.
         */
        public CensoredRunnable() {
            id = UUID.randomUUID();
            stop = false;
        }

        @Override
        public final void run() {
            if (stop) return;
            runIt();
        }

        /**
         * Replacement for run().
         */
        public abstract void runIt();

        public final UUID getId() {
            return id;
        }

        final void setFuture(ScheduledFuture<CensoredRunnable> future) {
            this.future = future;
        }

        /**
         * "Stops" the Thread related to this Runnable.
         *
         * @return Success.
         */
        public final boolean cancel() {
            if (TASKS.containsKey(id)) TASKS.remove(id);
            future.cancel(false);
            return stop = true;
        }
    }
}
