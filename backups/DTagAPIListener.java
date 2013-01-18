package com.legit2.Demigods.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

import com.legit2.Demigods.DSave;
import com.legit2.Demigods.Utilities.DUtil;

public class DTagAPIListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerNameTag(PlayerReceiveNameTagEvent event)
	{
		// Define Variables
		Player player = event.getNamedPlayer();
		String username = player.getName();
		
		// Recolor names based on deity.
		if(DUtil.hasADeity(username))
		{
			for(String deity : DUtil.getLoadedDeityNames())
			{
				if(DUtil.hasDeity(username, deity))
				{
					ChatColor color = (ChatColor) DSave.getData("deity_colors_temp", deity);
					
					String tag = color + username;
					if(username.length() <= 14) event.setTag(tag);
					
					break;
				}
			}
		}
	}
}
