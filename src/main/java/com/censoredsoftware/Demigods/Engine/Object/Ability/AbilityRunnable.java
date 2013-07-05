package com.censoredsoftware.Demigods.Engine.Object.Ability;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.Demigods.Engine.Demigods;

public abstract class AbilityRunnable extends BukkitRunnable
{
	public AbilityRunnable(int delay, int repeat)
	{
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Demigods.plugin, this, delay, repeat);
	}
}
