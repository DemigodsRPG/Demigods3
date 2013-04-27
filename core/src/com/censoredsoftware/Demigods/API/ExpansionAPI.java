package com.censoredsoftware.Demigods.API;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Demigods.DemigodsExpansion;

public class ExpansionAPI
{
	/**
	 * Returns the DemigodsPlugin for <code>pluginName</code>.
	 * 
	 * @param pluginName the plugin name to look for.
	 * @return DemigodsExpansion
	 */
	public static DemigodsExpansion getExpansion(String pluginName)
	{
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(pluginName);
		if(plugin != null && plugin instanceof DemigodsExpansion)
		{
			return (DemigodsExpansion) plugin;
		}
		else return null;
	}

	/**
	 * Returns the class loader for the <code>plugin</code>.
	 * 
	 * @param plugin the plugin whose class loader to return.
	 * @return ClassLoader
	 */
	public static ClassLoader getClassLoader(DemigodsExpansion plugin)
	{
		if(plugin != null) return plugin.getClass().getClassLoader();
		else return null;
	}
}
