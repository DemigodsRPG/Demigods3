package com.legit2.Demigods.Listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.legit2.Demigods.DUtil;

public class DChatCommands implements Listener
{
	@EventHandler
	public void onChatCommand(AsyncPlayerChatEvent event)
	{
		// Define variables
		Player player = event.getPlayer();
		
		if(!DUtil.isImmortal(player.getName())) return;
		
		if(event.getMessage().equals("dg")) dg(player,event);
	}
	
	private void dg(Player player, AsyncPlayerChatEvent event)
	{
		HashMap<String, ArrayList<String>> alliances = new HashMap<String, ArrayList<String>>();
		
		for(Player onlinePlayer : DUtil.getPlugin().getServer().getOnlinePlayers())
		{
			if (DUtil.isImmortal(onlinePlayer.getName()))
			{
				if (!alliances.containsKey(DUtil.getAlliance(onlinePlayer.getName()).toUpperCase()))  alliances.put(DUtil.getAlliance(onlinePlayer.getName()).toUpperCase(), new ArrayList<String>());
				
				alliances.get(DUtil.getAlliance(onlinePlayer.getName()).toUpperCase()).add(onlinePlayer.getName());
			}
		}
	
		for(String alliance : alliances.keySet())
		{
			{
				String names = "";
				for (String name : alliances.get(alliance)) names+=" "+name;
				player.sendMessage(ChatColor.YELLOW+alliance+": "+ChatColor.WHITE+names);
			}
			event.getRecipients().clear();
			event.setCancelled(true);
		}
	}
}

