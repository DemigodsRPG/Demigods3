package com.censoredsoftware.Demigods.Engine.Runnable;

import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.Demigods.Engine.Utility.SpigotUtility;
import com.google.common.collect.Maps;

public class SpigotParticleRunnable extends BukkitRunnable
{
	public static Map<Location, Effect> particleLocation = Maps.newConcurrentMap();

	@Override
	public void run()
	{
		for(Map.Entry<Location, Effect> entry : particleLocation.entrySet())
		{
			SpigotUtility.playParticle(entry.getKey(), entry.getValue(), 0, 2, 0, 1F, 15, 15);
		}
	}
}
