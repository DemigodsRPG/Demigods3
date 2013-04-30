package com.censoredsoftware.Demigods.API;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Demigods.Engine.DemigodsPlugin;

public class ExpansionAPI
{
	/**
	 * Returns the DemigodsPlugin for <code>pluginName</code>.
	 * 
	 * @param pluginName the plugin name to look for.
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
	 * Returns the class loader for the <code>plugin</code>.
	 * 
	 * @param plugin the plugin whose class loader to return.
	 * @return ClassLoader
	 */
	public static ClassLoader getClassLoader(DemigodsPlugin plugin)
	{
		if(plugin != null) return plugin.getClass().getClassLoader();
		else return null;
	}
}
