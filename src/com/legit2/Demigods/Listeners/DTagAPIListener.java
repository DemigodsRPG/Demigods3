package com.legit2.Demigods.Listeners;

import org.bukkit.event.Listener;

public class DTagAPIListener implements Listener
{
	/*
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerNameTag(PlayerReceiveNameTagEvent event)
	{
		
		// Define Variables
		Player player = event.getNamedPlayer();
		
		// Recolor names based on deity.
		if(DCharUtil.hasADeity(username))
		{
			for(String deity : DUtil.getLoadedDeityNames())
			{
				if(DCharUtil.hasDeity(username, deity))
				{
					ChatColor color = (ChatColor) DDataUtil.getData("deity_colors_temp", deity);
					
					String tag = color + username;
					if(username.length() <= 14) event.setTag(tag);
					
					break;
				}
			}
		}
		
	}
	*/
}
