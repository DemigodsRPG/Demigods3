package com.censoredsoftware.demigods.engine.data;

import com.censoredsoftware.censoredlib.util.Threads;
import com.censoredsoftware.censoredlib.util.Times;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.ability.Ability;
import com.censoredsoftware.demigods.engine.battle.Battle;
import com.censoredsoftware.demigods.engine.data.util.Notifications;
import com.censoredsoftware.demigods.engine.data.util.TimedDatas;
import com.censoredsoftware.demigods.engine.deity.Deity;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.engine.structure.Structure;
import com.censoredsoftware.demigods.engine.util.Admins;
import com.censoredsoftware.demigods.engine.util.Configs;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.engine.util.Zones;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("deprecation")
public class TaskManager
{
	private static boolean SAVE_ALERT = Configs.getSettingBoolean("saving.console_alert");

	public static void startThreads()
	{
		// Start sync demigods runnable
		Bukkit.getScheduler().scheduleSyncRepeatingTask(DemigodsPlugin.plugin(), Util.getSyncDemigodsRunnable(), 20, 20);
		Admins.sendDebug("Main Demigods SYNC runnable enabled...");

		// Start sync demigods runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(DemigodsPlugin.plugin(), Util.getAsyncDemigodsRunnable(), 20, 20);
		Admins.sendDebug("Main Demigods ASYNC runnable enabled...");

		// Start favor runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(DemigodsPlugin.plugin(), Util.getFavorRunnable(), 20, (Configs.getSettingInt("regeneration_rates.favor") * 20));
		Admins.sendDebug("Favor regeneration runnable enabled...");

		// Start saving runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(DemigodsPlugin.plugin(), Util.getSaveRunnable(), 20, (Configs.getSettingInt("saving.freq") * 20));

		// Enable Deity runnables
		for(Deity deity : Demigods.mythos().getDeities())
			for(Ability ability : deity.getAbilities())
				if(ability.getRunnable() != null) Bukkit.getScheduler().scheduleSyncRepeatingTask(DemigodsPlugin.plugin(), ability.getRunnable(), ability.getDelay(), ability.getRepeat());

		// Triggers
		Threads.registerTriggers(DemigodsPlugin.plugin(), Demigods.mythos().getTriggers());
	}

	public static void stopThreads()
	{
		DemigodsPlugin.plugin().getServer().getScheduler().cancelTasks(DemigodsPlugin.plugin());
		Threads.stopHooker(DemigodsPlugin.plugin());
	}

	private static class Util
	{
		private final static BukkitRunnable sync, async, save, favor;

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
						if(Zones.inNoDemigodsZone(player.getLocation())) continue;
						DPlayer.Util.getPlayer(player).updateCanPvp();
					}

					// Update Battles
					Battle.Util.updateBattles();

					// Update Battle Particles
					Battle.Util.updateBattleParticles();
				}
			};
			async = new BukkitRunnable()
			{
				@Override
				public void run()
				{
					// Update Timed Data
					TimedDatas.updateTimedData();

					// Update Notifications
					Notifications.updateNotifications();
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
					if(SAVE_ALERT) Messages.info(Bukkit.getOnlinePlayers().length + " of " + DataManager.players.size() + " total players saved in " + Times.getSeconds(time) + " seconds.");
				}
			};
			favor = new BukkitRunnable()
			{
				@Override
				public void run()
				{
					// Update Favor
					DCharacter.Util.updateFavor();

					// Update Sanctity
					Structure.Util.updateSanctity();
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
