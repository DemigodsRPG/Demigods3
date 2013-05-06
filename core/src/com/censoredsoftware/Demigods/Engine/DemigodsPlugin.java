package com.censoredsoftware.Demigods.Engine;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.censoredsoftware.Demigods.Demo.Deities;
import com.censoredsoftware.Demigods.Demo.Quests;

/**
 * Class for all plugins of Demigods.
 */
public class DemigodsPlugin extends JavaPlugin
{
	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		// Load the game engine, passing in the game data.
		new Demigods(this, Deities.values(), Quests.values());

		// Start game threads.
		Scheduler.startThreads(this);

		Demigods.message.info("Successfully enabled.");
	}

	/**
	 * The Bukkit disable method.
	 */
	@Override
	public void onDisable()
	{
		// Save all the data.
		DemigodsData.save();

		// Cancel all threads and event calls.
		Scheduler.stopThreads(this);
		HandlerList.unregisterAll(this);

		Demigods.message.info("Successfully disabled.");
	}
}
