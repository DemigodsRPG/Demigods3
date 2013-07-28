package com.censoredsoftware.demigods.engine.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.demigods.DemigodsPlugin;
import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.battle.Battle;
import com.censoredsoftware.demigods.engine.element.Ability;
import com.censoredsoftware.demigods.engine.element.Deity;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.engine.utility.AdminUtility;
import com.censoredsoftware.demigods.engine.utility.ConfigUtility;

public class ThreadManager
{
	public static void startThreads(DemigodsPlugin instance)
	{
		// Start sync demigods runnable
		Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, Util.getSyncDemigodsRunnable(), 20, 20);
		AdminUtility.sendDebug("Main Demigods SYNC runnable enabled...");

		// Start sync demigods runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, Util.getAsyncDemigodsRunnable(), 20, 20);
		AdminUtility.sendDebug("Main Demigods ASYNC runnable enabled...");

		// Start spigot particle runnable
		if(Demigods.runningSpigot())
		{
			Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, Util.getSpigotRunnable(), 20, 20);
			AdminUtility.sendDebug("Special (Spigot) runnable enabled...");
		}

		// Start favor runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, Util.getFavorRunnable(), 20, (ConfigUtility.getSettingInt("regeneration.favor") * 20));
		AdminUtility.sendDebug("Favor regeneration runnable enabled...");

		// Enable Deity runnables
		for(Deity deity : Demigods.getLoadedDeities())
			for(Ability ability : deity.getAbilities())
				if(ability.getRunnable() != null) Bukkit.getScheduler().scheduleSyncRepeatingTask(Demigods.plugin, ability.getRunnable(), 0, ability.getInfo().getRepeat());
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
					// Update PVP
					for(Player player : Bukkit.getOnlinePlayers())
					{
						DPlayer.Util.getPlayer(player).updateCanPvp();
					}
				}
			};
		}

		/**
		 * Returns the main async Demigods runnable. Methods NOT requiring the Bukkit API and a constant
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
				private double multiplier = ConfigUtility.getSettingDouble("multipliers.favor");

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
