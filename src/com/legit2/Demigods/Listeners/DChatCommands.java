package com.legit2.Demigods.Listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.legit2.Demigods.Utilities.DCharUtil;
import com.legit2.Demigods.Utilities.DPlayerUtil;
import com.legit2.Demigods.Utilities.DUtil;

public class DChatCommands implements Listener
{
	@EventHandler
	public void onChatCommand(AsyncPlayerChatEvent event)
	{
		// Define variables
		Player player = event.getPlayer();
		
		if(!DPlayerUtil.isImmortal(player)) return;
		
		if(event.getMessage().equals("pl")) pl(player,event);
	}
	
	private void pl(Player player, AsyncPlayerChatEvent event)
	{
		HashMap<String, ArrayList<String>> alliances = new HashMap<String, ArrayList<String>>();
		
		for(Player onlinePlayer : DUtil.getPlugin().getServer().getOnlinePlayers())
		{
			int currentCharID = DPlayerUtil.getCurrentChar(player);

			if(DPlayerUtil.isImmortal(onlinePlayer))
			{
				if (!alliances.containsKey(DCharUtil.getAlliance(onlinePlayer, currentCharID).toUpperCase()))  alliances.put(DCharUtil.getAlliance(onlinePlayer, currentCharID).toUpperCase(), new ArrayList<String>());
				
				alliances.get(DCharUtil.getAlliance(onlinePlayer, currentCharID).toUpperCase()).add(onlinePlayer.getName());
			}
		}
	
		for(String alliance : alliances.keySet())
		{
			String names = "";
			for (String name : alliances.get(alliance)) names+=" "+name;
			player.sendMessage(ChatColor.YELLOW+alliance+": "+ChatColor.WHITE+names);
			
			event.getRecipients().clear();
			event.setCancelled(true);
		}
	}
}

