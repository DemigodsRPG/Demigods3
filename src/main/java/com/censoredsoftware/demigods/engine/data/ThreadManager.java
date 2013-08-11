package com.censoredsoftware.demigods.engine.data;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.censoredsoftware.demigods.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.battle.Battle;
import com.censoredsoftware.demigods.engine.element.Ability;
import com.censoredsoftware.demigods.engine.element.Deity;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.engine.player.Notification;
import com.censoredsoftware.demigods.engine.util.Admins;
import com.google.common.collect.Sets;

@SuppressWarnings("deprecation")
public class ThreadManager
{
	private static Set<BukkitTask> tasks = Sets.newHashSet();

	public static void startThreads(DemigodsPlugin instance)
	{
		// Start sync demigods runnable
		tasks.add(Util.startSyncDemigods());
		Admins.sendDebug("Main Demigods SYNC runnable enabled...");

		// Start sync demigods runnable
		tasks.add(Util.startAsyncDemigods());
		Admins.sendDebug("Main Demigods ASYNC runnable enabled...");

		// Start spigot particle runnable
		if(Demigods.isRunningSpigot())
		{
			tasks.add(Util.startSpigot());
			Admins.sendDebug("Special (Spigot) runnable enabled...");
		}

		// Start favor runnable
		tasks.add(Util.startFavor());
		Admins.sendDebug("Favor regeneration runnable enabled...");

		// Enable Deity runnables
		for(Deity deity : Demigods.getLoadedDeities().values())
			for(Ability ability : deity.getAbilities())
				if(ability.getRunnable() != null) tasks.add(ability.getRunnable().runTaskTimer(Demigods.plugin, ability.getInfo().getDelay(), ability.getInfo().getRepeat()));
	}

	public static void stopThreads(DemigodsPlugin instance)
	{
		for(BukkitTask task : tasks)
			task.cancel();
		instance.getServer().getScheduler().cancelTasks(instance);
	}

	private static class Util
	{
		/**
		 * Returns the main sync Demigods runnable. Methods requiring the Bukkit API and a constant
		 * update should go here.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitTask startSyncDemigods()
		{
			return new BukkitRunnable()
			{
				@Override
				public void run()
				{
					// Update online players
					for(Player player : Bukkit.getOnlinePlayers())
					{
						if(Demigods.isDisabledWorld(player.getLocation())) continue;
						DPlayer.Util.getPlayer(player).updateCanPvp();
					}
				}
			}.runTaskTimer(Demigods.plugin, 20, 20);
		}

		/**
		 * Returns the main async Demigods runnable. Methods NOT requiring the Bukkit API and a constant
		 * update should go here.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitTask startAsyncDemigods()
		{
			return new BukkitRunnable()
			{
				@Override
				public void run()
				{
					// Update Battles
					Battle.Util.updateBattles();

					// Update Timed Data
					TimedData.Util.updateTimedData();

					// Update Notifications
					Notification.Util.updateNotifications();
				}
			}.runTaskTimerAsynchronously(Demigods.plugin, 20, 20);
		}

		/**
		 * Returns the Spigot-only runnable. Methods requiring Spigot should be placed here.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitTask startSpigot()
		{
			return new BukkitRunnable()
			{
				@Override
				public void run()
				{
					// Update Battles
					Battle.Util.updateBattleParticles();
				}
			}.runTaskTimer(Demigods.plugin, 20, 20);
		}

		/**
		 * Returns the favor regeneration runnable. This must be placed here due to varying favor
		 * regeneration frequencies.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitTask startFavor()
		{
			return new BukkitRunnable()
			{
				private final double multiplier = Demigods.config.getSettingDouble("multipliers.favor");

				@Override
				public void run()
				{
					// Update Favor
					DPlayer.Util.updateFavor(multiplier);
				}
			}.runTaskTimerAsynchronously(Demigods.plugin, 20, (Demigods.config.getSettingInt("regeneration.favor") * 20));
		}
	}
}
