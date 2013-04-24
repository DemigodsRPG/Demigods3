package com.censoredsoftware.Demigods;

import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Abstract class for all expansions of Demigods to extend in their main plugin class.
 */
public abstract class DemigodsExpansion extends JavaPlugin
{
	// On-load Deity ClassPath List
	public static ArrayList<String> deityPathList = new ArrayList<String>();

	public static ArrayList<String> getDeityPaths()
	{
		return deityPathList;
	}

	/**
	 * Use this instead of the built in Bukkit method onEnable().
	 * 
	 * @return Success.
	 */
	public static boolean loadExpansion(DemigodsPlugin instance)
	{
		return true;
	}
}
