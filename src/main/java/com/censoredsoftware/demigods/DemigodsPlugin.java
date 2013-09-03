package com.censoredsoftware.demigods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.data.ThreadManager;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.util.Messages;

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
		// Load the game engine.
		Demigods.PLUGIN = this;
		Demigods.finishLoading();

		// Print success!
		Messages.info("Successfully enabled.");
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

		// Cancel all threads, Event calls, and connections.
		ThreadManager.stopThreads();
		HandlerList.unregisterAll(this);

		Messages.info("Successfully disabled.");
	}
}
