package com.censoredsoftware.Demigods.Engine.Runnable;

import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.Demigods.Engine.Object.General.TimedData;

public class TimedDataRunnable extends BukkitRunnable
{
	@Override
	public void run()
	{
		for(TimedData data : TimedData.getAll())
		{
			if(data.getExpiration() <= System.currentTimeMillis()) data.delete();
		}
	}
}
