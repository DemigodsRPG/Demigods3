package com.censoredsoftware.demigods.util;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.errornoise.ErrorNoise;
import org.bukkit.Bukkit;

public class Errors
{
	static Boolean ERROR_NOISE;

	static
	{
		ERROR_NOISE = Bukkit.getPluginManager().getPlugin("ErrorNoise") instanceof ErrorNoise;
	}

	public static void triggerError(String... message)
	{
		if(!ERROR_NOISE) return;
		ErrorNoise.API.triggerError(Demigods.PLUGIN, message);
	}
}
