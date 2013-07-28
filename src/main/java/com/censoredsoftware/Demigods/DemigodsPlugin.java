package com.censoredsoftware.Demigods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.censoredsoftware.Demigods.Engine.Data.DataManager;
import com.censoredsoftware.Demigods.Engine.Data.ThreadManager;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Misc.Exception.DemigodsStartupException;
import com.censoredsoftware.Demigods.Engine.Player.DPlayer;
import com.censoredsoftware.Demigods.Engine.Utility.MessageUtility;
import com.censoredsoftware.Demigods.Episodes.Demo.EpisodeDemo;

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
