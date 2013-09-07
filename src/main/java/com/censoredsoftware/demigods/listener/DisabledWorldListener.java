package com.censoredsoftware.demigods.listener;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;

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

	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldSwitch(PlayerChangedWorldEvent event)
	{
		// Only continue if the player is a character
		Player player = event.getPlayer();
		DPlayer dPlayer = DPlayer.Util.getPlayer(player);
		final DCharacter character = dPlayer.getCurrent();

		if(dPlayer.getCurrent() == null) return;

		// Leaving a disabled world
		if(Demigods.MiscUtil.isDisabledWorld(event.getFrom()) && !Demigods.MiscUtil.isDisabledWorld(player.getWorld()))
		{
			dPlayer.saveMortalInventory(player.getInventory());
			character.applyToPlayer(player);
			player.sendMessage(ChatColor.YELLOW + "Demigods is enabled in this world.");
		}
		// Entering a disabled world
		else if(!Demigods.MiscUtil.isDisabledWorld(event.getFrom()) && Demigods.MiscUtil.isDisabledWorld(player.getWorld()))
		{
			dPlayer.setToMortal();
			player.sendMessage(ChatColor.GRAY + "Demigods is disabled in this world.");
		}
	}
}
