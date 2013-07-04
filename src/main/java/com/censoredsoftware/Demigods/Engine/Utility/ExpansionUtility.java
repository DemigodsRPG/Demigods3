package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Demigods.DemigodsPlugin;

public class ExpansionUtility
{
	/**
	 * Returns the DemigodsPlugin for <code>pluginName</code>.
	 * 
	 * @param pluginName the demigods name to look for.
	 * @return DemigodsPlugin
	 */
	public static DemigodsPlugin getExpansion(String pluginName)
	{
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(pluginName);
		if(plugin != null && plugin instanceof DemigodsPlugin)
		{
			return (DemigodsPlugin) plugin;
		}
		else return null;
	}

	/**
	 * Returns the class loader for the <code>demigods</code>.
	 * 
	 * @param plugin the demigods whose class loader to return.
	 * @return ClassLoader
	 */
	public static ClassLoader getClassLoader(DemigodsPlugin plugin)
	{
		if(plugin != null) return plugin.getClass().getClassLoader();
		else return null;
	}
}
