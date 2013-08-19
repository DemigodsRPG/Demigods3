package com.censoredsoftware.demigods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.data.ThreadManager;
import com.censoredsoftware.demigods.exception.DemigodsStartupException;
import com.censoredsoftware.demigods.helper.CSPlugin;
import com.censoredsoftware.demigods.player.DPlayer;

/**
 * Class for all plugins of demigods.
 */
public class DemigodsPlugin extends CSPlugin
{
	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		try
		{
			// Load the game engine.
			new Demigods(this);

			// Print success!
			Demigods.message.info("Successfully enabled.");
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
		DataManager.save();

		// Toggle all prayer off
		for(Player player : Bukkit.getOnlinePlayers())
			DPlayer.Util.togglePrayingSilent(player, false, false);

		// Cancel all threads, callAbilityEvent calls, and connections.
		ThreadManager.stopThreads(this);
		HandlerList.unregisterAll(this);

		Demigods.message.info("Successfully disabled.");
	}
}
