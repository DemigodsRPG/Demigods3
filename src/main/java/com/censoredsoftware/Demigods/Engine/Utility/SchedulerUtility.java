package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.Demigods.DemigodsPlugin;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.*;

public class SchedulerUtility
{
	public static void startThreads(DemigodsPlugin instance)
	{
		// Start sync Demigods runnable
		// Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, Util.getSyncDemigodsRunnable(), 20, 20);
		AdminUtility.sendDebug("Main Demigods runnable enabled...");

		// TODO: Move these to methods where needed and organize

		// Start spigot particle runnable
		if(Demigods.runningSpigot())
		{
			Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, new Battle.SpigotBattleRunnable(), 20, 20);
			AdminUtility.sendDebug("Special (spigot) battle runnable enabled...");
		}

		// Start favor runnable
		int rate = Demigods.config.getSettingInt("regeneration.favor") * 20;
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, new DPlayer.FavorRunnable(Demigods.config.getSettingDouble("multipliers.favor")), 20, rate);
		AdminUtility.sendDebug("Favor regeneration runnable enabled...");

		// Start battle runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, new Battle.BattleRunnable(), 20, 20);
		AdminUtility.sendDebug("Battle tracking runnable enabled...");

		// Start timed data runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, new TimedData.TimedDataRunnable(), 20, 20);
		AdminUtility.sendDebug("Timed data runnable enabled...");

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
	}
}
