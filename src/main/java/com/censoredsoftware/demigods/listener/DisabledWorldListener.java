package com.censoredsoftware.demigods.listener;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.player.DemigodsChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Set;

public class DisabledWorldListener implements Listener
{
	// TODO Special listener to prevent interactions between Demigods worlds and non-Demigods worlds.

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDemigodsChat(DemigodsChatEvent event)
	{
		Set<Player> modified = event.getRecipients();
		for(Player player : event.getRecipients())
			if(Demigods.MiscUtil.isDisabledWorld(player.getLocation())) modified.remove(player);
		if(modified.size() < 1) event.setCancelled(true);
		event.setRecipients(modified);
	}
}
