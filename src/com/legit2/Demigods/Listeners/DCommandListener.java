package com.legit2.Demigods.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.legit2.Demigods.Demigods;

public class DCommandListener implements Listener
{
	static Demigods plugin;
	
	public DCommandListener(Demigods instance)
	{
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public static void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		String message = event.getMessage();
		event.getPlayer();
		
		// DDeityUtil.invokeDeityCommand();
	}
}
