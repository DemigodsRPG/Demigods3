package com.censoredsoftware.Demigods.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.API.DeityAPI;
import com.censoredsoftware.Demigods.API.PlayerAPI;
import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacter;

public class ChatListener implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChatCommand(AsyncPlayerChatEvent event)
	{
		// Define variables
		Player player = event.getPlayer();
		Set<Player> viewing = event.getRecipients();
		String message = event.getMessage();

		if(message.equals("pl")) pl(player, event);

		// No chat toggle
		if(DemigodsData.tempPlayerData.containsKey(player, "temp_no_chat")) event.setCancelled(true);
		for(Player victim : Bukkit.getOnlinePlayers())
		{
			if(DemigodsData.tempPlayerData.containsKey(victim, "temp_no_chat")) viewing.remove(victim);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChatMessage(AsyncPlayerChatEvent event)
	{
		// Define variables
		Player player = event.getPlayer();
		String message = event.getMessage();

		// Handle chat for character switching
		if(DemigodsData.tempPlayerData.containsKey(player, "temp_chat_number"))
		{
			// Define variables
			PlayerCharacter prevChar = CharacterAPI.getChar(DemigodsData.playerData.getDataInt(player, "previous_char"));

			DemigodsData.tempPlayerData.saveData(player, "temp_chat_number", DemigodsData.tempPlayerData.getDataInt(player, "temp_chat_number") + 1);

			if(DemigodsData.tempPlayerData.getDataInt(player, "temp_chat_number") <= 2)
			{
				event.setMessage(ChatColor.GRAY + "(Previously " + DeityAPI.getDeityColor(prevChar.isDeity()) + prevChar.getName() + ChatColor.GRAY + ") " + ChatColor.WHITE + message);
			}
			else DemigodsData.tempPlayerData.removeData(player, "temp_chat_number");
		}
	}

	private void pl(Player player, AsyncPlayerChatEvent event)
	{
		HashMap<String, ArrayList<String>> alliances = new HashMap<String, ArrayList<String>>();

		for(Player onlinePlayer : Bukkit.getOnlinePlayers())
		{
			String alliance = PlayerAPI.getCurrentAlliance(player);

			if(!alliances.containsKey(alliance.toUpperCase())) alliances.put(alliance.toUpperCase(), new ArrayList<String>());
			alliances.get(alliance.toUpperCase()).add(onlinePlayer.getName());
		}

		for(String alliance : alliances.keySet())
		{
			String names = "";
			for(String name : alliances.get(alliance))
			{
				names += " " + name;
			}
			player.sendMessage(ChatColor.YELLOW + alliance + ": " + ChatColor.WHITE + names);
		}

		event.getRecipients().clear();
		event.setCancelled(true);
	}
}
