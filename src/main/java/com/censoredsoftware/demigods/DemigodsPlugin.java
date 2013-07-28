package com.censoredsoftware.demigods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.censoredsoftware.demigods.engine.Demigods;
import com.censoredsoftware.demigods.engine.data.DataManager;
import com.censoredsoftware.demigods.engine.data.ThreadManager;
import com.censoredsoftware.demigods.engine.exception.DemigodsStartupException;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.censoredsoftware.demigods.episodes.demo.EpisodeDemo;

/**
 * Class for all plugins of demigods.
 */
public class DemigodsPlugin extends JavaPlugin
{
	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		try
		{
			// Load the game engine, passing in the game data.
			new Demigods(this, EpisodeDemo.Deities.values(), EpisodeDemo.Tasks.values(), EpisodeDemo.Structures.values(), null, EpisodeDemo.getListener());

			// Print success!
			Messages.info("Successfully enabled.");
		}
		catch(DemigodsStartupException ignored)
		{}
	}

	/**
	 * The Bukkit disable method.
	 */
	@Override
	public void onDisable()
	{
		// Save all the data.
		if(DataManager.isConnected()) DataManager.save();

		// Toggle all prayer off
		for(Player player : Bukkit.getOnlinePlayers())
			DPlayer.Util.togglePrayingSilent(player, false);

		// Cancel all threads, callAbilityEvent calls, and connections.
		ThreadManager.stopThreads(this);
		HandlerList.unregisterAll(this);
		DataManager.disconnect();

		Messages.info("Successfully disabled.");
	}
}
