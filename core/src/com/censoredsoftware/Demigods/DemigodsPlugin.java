package com.censoredsoftware.Demigods;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The DemigodsPlugin that will be loaded by a Bukkit server.
 */
public class DemigodsPlugin extends JavaPlugin
{
	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		Demigods.loadDepends(this);
		Demigods.loadSettings(this);
		Demigods.loadModules(this);
		Demigods.loadData(this);
		Demigods.loadCommands(this);
		Demigods.loadListeners(this);
		Demigods.loadMetrics(this);
		Demigods.message.info("Successfully enabled.");
	}

	/**
	 * The Bukkit disable method.
	 */
	@Override
	public void onDisable()
	{
		Demigods.unload(this);
		Demigods.message.info("Successfully disabled.");
	}
}
