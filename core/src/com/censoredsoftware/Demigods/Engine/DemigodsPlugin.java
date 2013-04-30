package com.censoredsoftware.Demigods.Engine;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Class for all plugins of Demigods.
 */
public class DemigodsPlugin extends JavaPlugin
{
	public static enum Deities
	{}

	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		new Demigods(this);

		Demigods.loadDepends(this);
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
		DemigodsData.Save.save(true, true);

		HandlerList.unregisterAll(this);
		Scheduler.stopThreads(this);

		Demigods.message.info("Successfully disabled.");
	}
}
