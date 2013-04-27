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
		new Demigods(this);

		Demigods.loadDepends(this);
		Demigods.loadExpansions(this);
		Demigods.loadDeities();
		Demigods.loadListeners(this);
		Demigods.loadCommands(this);

		Scheduler.startThreads(this);

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
