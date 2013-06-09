package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Demigods.DemigodsBukkit;

public class ExpansionUtility
{
	/**
	 * Returns the DemigodsBukkit for <code>pluginName</code>.
	 * 
	 * @param pluginName the demigods name to look for.
	 * @return DemigodsBukkit
	 */
	public static DemigodsBukkit getExpansion(String pluginName)
	{
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(pluginName);
		if(plugin != null && plugin instanceof DemigodsBukkit)
		{
			return (DemigodsBukkit) plugin;
		}
		else return null;
	}

	/**
	 * Returns the class loader for the <code>demigods</code>.
	 * 
	 * @param plugin the demigods whose class loader to return.
	 * @return ClassLoader
	 */
	public static ClassLoader getClassLoader(DemigodsBukkit plugin)
	{
		if(plugin != null) return plugin.getClass().getClassLoader();
		else return null;
	}
}
