package com.censoredsoftware.demigods.exclusive.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class DistrictListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldSwitch(PlayerChangedWorldEvent event)
	{

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldLoad(WorldLoadEvent event)
	{
		// TODO
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldLoad(WorldUnloadEvent event)
	{
		// TODO
	}
}
