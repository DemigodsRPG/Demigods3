package com.censoredsoftware.why;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.censoredsoftware.why.engine.Demigods;
import com.censoredsoftware.why.engine.data.DataManager;
import com.censoredsoftware.why.engine.data.ThreadManager;
import com.censoredsoftware.why.engine.exception.DemigodsStartupException;
import com.censoredsoftware.why.engine.player.DPlayer;
import com.censoredsoftware.why.engine.util.MessageUtility;
import com.censoredsoftware.why.episodes.demo.EpisodeDemo;

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
			new Demigods(this, EpisodeDemo.Deities.values(), EpisodeDemo.Tasks.values(), EpisodeDemo.Structures.values(), null);

			// Print success!
			MessageUtility.info("Successfully enabled.");
		}
		catch(DemigodsStartupException e)
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

		MessageUtility.info("Successfully disabled.");
	}
}
