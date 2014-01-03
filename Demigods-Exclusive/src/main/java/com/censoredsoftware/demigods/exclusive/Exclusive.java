package com.censoredsoftware.demigods.exclusive;

import com.censoredsoftware.demigods.engine.mythos.Mythos;
import com.censoredsoftware.demigods.exclusive.data.ExData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class Exclusive
{
	private static final Exclusive INST = new Exclusive();

	private Exclusive()
	{}

	private static Exclusive inst()
	{
		return INST;
	}

	public static boolean init()
	{
		// Register Mythos
		Bukkit.getServer().getServicesManager().register(Mythos.class, ExclusiveMythos.inst(), ExclusiveMythos.inst(), ServicePriority.Highest); // not really sure how Bukkit handles these, presuming the same way as EventPriority

		// Load data
		ExData.init();

		return true;
	}
}