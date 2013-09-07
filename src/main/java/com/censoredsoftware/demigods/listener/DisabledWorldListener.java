package com.censoredsoftware.demigods.listener;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

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

	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldSwitch(PlayerChangedWorldEvent event)
	{
		// Only continue if the player is a character
		Player player = event.getPlayer();
		DPlayer dPlayer = DPlayer.Util.getPlayer(player);
		DCharacter character = dPlayer.getCurrent();
		if(!DPlayer.Util.isImmortal(player)) return;

		// Leaving a disabled world
		if(Demigods.MiscUtil.isDisabledWorld(event.getFrom()) && !Demigods.MiscUtil.isDisabledWorld(player.getWorld()))
		{
			dPlayer.setDisabledWorldInventory(player);
			character.getInventory().setToPlayer(player);
			player.setDisplayName(character.getDeity().getColor() + character.getName());
			player.setPlayerListName(character.getDeity().getColor() + character.getName());
			player.sendMessage(ChatColor.YELLOW + "Demigods is enabled in this world.");
		}
		else if(!Demigods.MiscUtil.isDisabledWorld(event.getFrom()) && Demigods.MiscUtil.isDisabledWorld(player.getWorld()))
		{
			character.saveInventory();
			dPlayer.applyDisabledWorldInventory();
			player.setDisplayName(player.getName());
			player.setPlayerListName(player.getName());
			player.sendMessage(ChatColor.GRAY + "Demigods is disabled in this world.");
		}
	}
}
