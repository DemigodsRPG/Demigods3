package com.censoredsoftware.demigods.engine.data;

import com.censoredsoftware.censoredlib.util.Threads;
import com.censoredsoftware.censoredlib.util.Times;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.conversation.Administration;
import com.censoredsoftware.demigods.engine.data.serializable.Battle;
import com.censoredsoftware.demigods.engine.data.serializable.DCharacter;
import com.censoredsoftware.demigods.engine.data.serializable.DPlayer;
import com.censoredsoftware.demigods.engine.data.wrap.NotificationManager;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import com.censoredsoftware.demigods.engine.mythos.Structure;
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
		Administration.Util.sendDebug("Main Demigods SYNC runnable enabled...");

		// Start async demigods runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(DemigodsPlugin.plugin(), Util.getAsyncDemigodsRunnable(), 20, 20);
		Administration.Util.sendDebug("Main Demigods ASYNC runnable enabled...");

		// Start favor runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(DemigodsPlugin.plugin(), Util.getFavorRunnable(), 20, (Configs.getSettingInt("regeneration_rates.favor") * 20));
		Administration.Util.sendDebug("Favor regeneration runnable enabled...");

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
		private static final BukkitRunnable SYNC, ASYNC, SAVE, FAVOR;

		static
		{
			SYNC = new BukkitRunnable()
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
			ASYNC = new BukkitRunnable()
			{
				@Override
				public void run()
				{
					// Update Timed Data
					FileDataSource.TIMED.clearExpired();

					// Update Notifications
					NotificationManager.updateNotifications();
				}
			};
			SAVE = new BukkitRunnable()
			{
				@Override
				public void run()
				{
					// Save time for reference after saving
					long time = System.currentTimeMillis();

					// Save data
					FileDataSource.save();

					// Send the save message to the console
					if(SAVE_ALERT) Messages.info(Bukkit.getOnlinePlayers().length + " of " + FileDataSource.PLAYER.keySet().size() + " total players saved in " + Times.getSeconds(time) + " seconds.");
				}
			};
			FAVOR = new BukkitRunnable()
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
		 * Returns the main SYNC Demigods runnable. Methods requiring the Bukkit API and a constant
		 * update should go here.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitRunnable getSyncDemigodsRunnable()
		{
			return SYNC;
		}

		/**
		 * Returns the main asynchronous Demigods runnable. Methods NOT requiring the Bukkit API and a constant
		 * update should go here.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitRunnable getAsyncDemigodsRunnable()
		{
			return ASYNC;
		}

		/**
		 * Returns the runnable that handles all data saving.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitRunnable getSaveRunnable()
		{
			return SAVE;
		}

		/**
		 * Returns the FAVOR regeneration runnable. This must be placed here due to varying FAVOR
		 * regeneration frequencies.
		 * 
		 * @return the runnable to be enabled.
		 */
		public static BukkitRunnable getFavorRunnable()
		{
			return FAVOR;
		}
	}
}
