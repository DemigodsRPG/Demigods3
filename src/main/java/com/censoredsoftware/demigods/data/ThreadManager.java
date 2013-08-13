package com.censoredsoftware.demigods.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.core.util.Times;
import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.DemigodsPlugin;
import com.censoredsoftware.demigods.Elements;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.battle.Battle;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.player.Notification;
import com.censoredsoftware.demigods.util.Admins;

@SuppressWarnings("deprecation")
public class ThreadManager
{
	public static void startThreads(DemigodsPlugin instance)
	{
		// Start sync demigods runnable
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, Util.getSyncDemigodsRunnable(), 20, 20);
		Admins.sendDebug("Main Demigods SYNC runnable enabled...");

		// Start sync demigods runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, Util.getAsyncDemigodsRunnable(), 20, 20);
		Admins.sendDebug("Main Demigods ASYNC runnable enabled...");

		// Start spigot particle runnable
		if(Demigods.isRunningSpigot())
		{
			Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, Util.getSpigotRunnable(), 20, 20);
			Admins.sendDebug("Special (Spigot) runnable enabled...");
		}

		// Start favor runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, Util.getFavorRunnable(), 20, (Demigods.config.getSettingInt("regeneration.favor") * 20));
		Admins.sendDebug("Favor regeneration runnable enabled...");

		// Start saving runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, Util.getSaveRunnable(), 20, (Demigods.config.getSettingInt("saving.freq") * 20));

		// Enable Deity runnables
		for(Elements.ListedDeity deity : Elements.Deities.values())
			for(Ability ability : deity.getDeity().getAbilities())
				if(ability.getRunnable() != null) Bukkit.getScheduler().scheduleSyncRepeatingTask(Demigods.plugin, ability.getRunnable(), ability.getDelay(), ability.getRepeat());
	}

	public static void stopThreads(DemigodsPlugin instance)
	{
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
		public static BukkitRunnable getSyncDemigodsRunnable()
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
			};
		}

		/**
		 * Returns the main asynchronous Demigods runnable. Methods NOT requiring the Bukkit API and a constant
		 * update should go here.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitRunnable getAsyncDemigodsRunnable()
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
			};
		}

		/**
		 * Returns the runnable that handles all data saving.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitRunnable getSaveRunnable()
		{
			return new BukkitRunnable()
			{
				@Override
				public void run()
				{
					// Save time for reference after saving
					long time = System.currentTimeMillis();

					// Save data
					DataManager.save();

					// Send the save message to the console
					Demigods.message.info(Bukkit.getOnlinePlayers().length + " of " + "X" + " total players saved in " + Times.getSeconds(time) + " seconds."); // TODO: Replace X
				}
			};
		}

		/**
		 * Returns the Spigot-only runnable. Methods requiring Spigot should be placed here.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitRunnable getSpigotRunnable()
		{
			return new BukkitRunnable()
			{
				@Override
				public void run()
				{
					// Update Battles
					Battle.Util.updateBattleParticles();
				}
			};
		}

		/**
		 * Returns the favor regeneration runnable. This must be placed here due to varying favor
		 * regeneration frequencies.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitRunnable getFavorRunnable()
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
			};
		}
	}
}
