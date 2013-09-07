package com.censoredsoftware.demigods.listener;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.potion.PotionEffect;

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
		final DCharacter character = dPlayer.getCurrent();

		if(dPlayer.getCurrent() == null) return;

		// Leaving a disabled world
		if(Demigods.MiscUtil.isDisabledWorld(event.getFrom()) && !Demigods.MiscUtil.isDisabledWorld(player.getWorld()))
		{
			dPlayer.setDisabledWorldInventory(player);
			player.setDisplayName(character.getDeity().getColor() + character.getName());
			try
			{
				player.setPlayerListName(character.getDeity().getColor() + character.getName());
			}
			catch(Exception e)
			{
				Messages.warning("Character name too long.");
			}
			player.setMaxHealth(character.getMaxHealth());
			player.setHealth(character.getHealth() >= character.getMaxHealth() ? character.getMaxHealth() : character.getHealth());
			player.setFoodLevel(character.getHunger());
			player.setExp(character.getExperience());
			player.setLevel(character.getLevel());
			for(PotionEffect potion : player.getActivePotionEffects())
				player.removePotionEffect(potion.getType());
			if(character.getPotionEffects() != null) player.addPotionEffects(character.getPotionEffects());
			player.setBedSpawnLocation(character.getBedSpawn());
			character.getInventory().setToPlayer(player);
			player.setDisplayName(character.getDeity().getColor() + character.getName());
			player.setPlayerListName(character.getDeity().getColor() + character.getName());
			player.sendMessage(ChatColor.YELLOW + "Demigods is enabled in this world.");
		}
		else if(!Demigods.MiscUtil.isDisabledWorld(event.getFrom()) && Demigods.MiscUtil.isDisabledWorld(player.getWorld()))
		{
			character.setHealth(player.getHealth() >= character.getMaxHealth() ? character.getMaxHealth() : player.getHealth());
			character.setHunger(player.getFoodLevel());
			character.setLevel(player.getLevel());
			character.setExperience(player.getExp());
			character.setLocation(player.getLocation());
			if(player.getBedSpawnLocation() != null) character.setBedSpawn(player.getBedSpawnLocation());
			character.setPotionEffects(player.getActivePotionEffects());
			character.saveInventory();
			player.setMaxHealth(20.0);
			player.setHealth(20.0);
			player.setFoodLevel(20);
			player.setExp(0);
			player.setLevel(0);
			for(PotionEffect potion : player.getActivePotionEffects())
				player.removePotionEffect(potion.getType());
			dPlayer.applyDisabledWorldInventory();
			player.setDisplayName(player.getName());
			player.setPlayerListName(player.getName());
			player.sendMessage(ChatColor.GRAY + "Demigods is disabled in this world.");
		}
	}
}
