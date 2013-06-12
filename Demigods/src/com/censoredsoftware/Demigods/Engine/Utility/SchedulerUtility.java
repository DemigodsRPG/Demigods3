package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.Bukkit;

import com.censoredsoftware.Demigods.DemigodsBukkit;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Runnable.FavorRunnable;
import com.censoredsoftware.Demigods.Engine.Runnable.TimedDataRunnable;

public class SchedulerUtility
{
	public static void startThreads(DemigodsBukkit instance)
	{
		// Start favor runnable
		int rate = Demigods.config.getSettingInt("regeneration.favor") * 20;
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, new FavorRunnable(Demigods.config.getSettingDouble("multipliers.favor")), 20, rate);
		AdminUtility.sendDebug("Favor regeneration runnable enabled...");

		// Start battle runnable
		// Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, new BattleRunnable(), 20, 20);
		// AdminUtility.sendDebug("Battle tracking runnable enabled...");

		// Start timed data runnable
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, new TimedDataRunnable(), 20, 20);
		AdminUtility.sendDebug("Timed data runnable enabled...");
	}

	public static void stopThreads(DemigodsBukkit instance)
	{
		instance.getServer().getScheduler().cancelTasks(instance);
	}
}
