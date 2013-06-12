package com.censoredsoftware.Demigods;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.DemigodsScheduler;
import com.censoredsoftware.Demigods.Episodes.Demo.EpisodeDemo;

/**
 * Class for all plugins of Demigods.
 */
public class DemigodsBukkit extends JavaPlugin
{
	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		// Load the game engine, passing in the game data.
		new Demigods(this, EpisodeDemo.Deities.values(), EpisodeDemo.Quests.values());

		// Start game threads.
		DemigodsScheduler.startThreads(this);

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

		// Cancel all threads, event calls, and concections.
		DemigodsScheduler.stopThreads(this);
		HandlerList.unregisterAll(this);
		DemigodsData.disconnect();

		Demigods.message.info("Successfully disabled.");
	}
}
