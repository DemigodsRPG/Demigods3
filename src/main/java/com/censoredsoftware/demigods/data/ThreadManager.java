package com.censoredsoftware.demigods.data;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.ability.Ability;
import com.censoredsoftware.demigods.battle.Battle;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.player.Notification;
import com.censoredsoftware.demigods.structure.Altar;
import com.censoredsoftware.demigods.trigger.Trigger;
import com.censoredsoftware.demigods.util.Admins;
import com.censoredsoftware.demigods.util.Configs;
import com.censoredsoftware.demigods.util.Messages;
import com.censoredsoftware.demigods.util.Times;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("deprecation")
public class ThreadManager
{
	static
	{
		// Start sync demigods runnable
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Demigods.PLUGIN, Util.getSyncDemigodsRunnable(), 20, 20);
		Admins.sendDebug("Main Demigods SYNC runnable enabled...");

		// Start sync demigods runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(Demigods.PLUGIN, Util.getAsyncDemigodsRunnable(), 20, 20);
		Admins.sendDebug("Main Demigods ASYNC runnable enabled...");

		// Start spigot particle runnable
		if(Demigods.MiscUtil.isRunningSpigot())
		{
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Demigods.PLUGIN, Util.getSpigotRunnable(), 20, 20);
			Admins.sendDebug("Special (Spigot) runnable enabled...");
		}

		// Start favor runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(Demigods.PLUGIN, Util.getFavorRunnable(), 20, (Configs.getSettingInt("regeneration.favor") * 20));
		Admins.sendDebug("Favor regeneration runnable enabled...");

		// Start saving runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(Demigods.PLUGIN, Util.getSaveRunnable(), 20, (Configs.getSettingInt("saving.freq") * 20));

		// Enable Deity runnables
		for(Demigods.ListedDeity deity : Demigods.ListedDeity.values())
			for(Ability ability : deity.getDeity().getAbilities())
				if(ability.getRunnable() != null) Bukkit.getScheduler().scheduleSyncRepeatingTask(Demigods.PLUGIN, ability.getRunnable(), ability.getDelay(), ability.getRepeat());
	}

	public static void stopThreads()
	{
		Demigods.PLUGIN.getServer().getScheduler().cancelTasks(Demigods.PLUGIN);
	}

	private static class Util
	{
		private final static BukkitRunnable sync, async, save, spigot, favor;

		static
		{
			sync = new BukkitRunnable()
			{
				@Override
				public void run()
				{
					// Update online players
					for(Player player : Bukkit.getOnlinePlayers())
					{
						if(Demigods.MiscUtil.isDisabledWorld(player.getLocation())) continue;
						DPlayer.Util.getPlayer(player).updateCanPvp();
					}

					// Process Triggers
					for(Trigger trigger : Trigger.Util.getAll())
						trigger.processSync();

					// Update Battles
					Battle.Util.updateBattles();

					// Update Atlars
					Altar.Util.generateAltars();
				}
			};
			async = new BukkitRunnable()
			{
				@Override
				public void run()
				{
					// Update Timed Data
					TimedData.Util.updateTimedData();

					// Update Notifications
					Notification.Util.updateNotifications();

					// Process Triggers
					for(Trigger trigger : Trigger.Util.getAll())
						trigger.processAsync();

					// Process Atlars
					Altar.Util.processNewChunks();
				}
			};
			save = new BukkitRunnable()
			{
				@Override
				public void run()
				{
					// Save time for reference after saving
					long time = System.currentTimeMillis();

					// Save data
					DataManager.save();

					// Send the save message to the console
					Messages.info(Bukkit.getOnlinePlayers().length + " of " + DataManager.players.size() + " total players saved in " + Times.getSeconds(time) + " seconds.");
				}
			};
			spigot = new BukkitRunnable()
			{
				@Override
				public void run()
				{
					// Update Battles
					Battle.Util.updateBattleParticles();
				}
			};
			favor = new BukkitRunnable()
			{
				private final double multiplier = Configs.getSettingDouble("multipliers.favor");

				@Override
				public void run()
				{
					// Update Favor
					DPlayer.Util.updateFavor(multiplier);
				}
			};
		}

		/**
		 * Returns the main sync Demigods runnable. Methods requiring the Bukkit API and a constant
		 * update should go here.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitRunnable getSyncDemigodsRunnable()
		{
			return sync;
		}

		/**
		 * Returns the main asynchronous Demigods runnable. Methods NOT requiring the Bukkit API and a constant
		 * update should go here.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitRunnable getAsyncDemigodsRunnable()
		{
			return async;
		}

		/**
		 * Returns the runnable that handles all data saving.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitRunnable getSaveRunnable()
		{
			return save;
		}

		/**
		 * Returns the Spigot-only runnable. Methods requiring Spigot should be placed here.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitRunnable getSpigotRunnable()
		{
			return spigot;
		}

		/**
		 * Returns the favor regeneration runnable. This must be placed here due to varying favor
		 * regeneration frequencies.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitRunnable getFavorRunnable()
		{
			return favor;
		}
	}
}
