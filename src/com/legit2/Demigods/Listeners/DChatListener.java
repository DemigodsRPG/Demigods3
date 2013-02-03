package com.legit2.Demigods.Listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.legit2.Demigods.Utilities.DCharUtil;
import com.legit2.Demigods.Utilities.DDataUtil;
import com.legit2.Demigods.Utilities.DPlayerUtil;
import com.legit2.Demigods.Utilities.DMiscUtil;

public class DChatListener implements Listener
{
	@EventHandler
	public void onChatCommand(AsyncPlayerChatEvent event)
	{
		// Define variables
		Player player = event.getPlayer();
		Set<Player> viewing = event.getRecipients();
		
		if(!DCharUtil.isImmortal(player)) return;
		
		if(event.getMessage().equals("pl")) pl(player,event);
		
		// NO CHAT TOGGLE
		if(DDataUtil.hasPlayerData(player, "temp_no_chat")) event.setCancelled(true);
		for(Player victim : DPlayerUtil.getOnlinePlayers())
		{
			if(DDataUtil.hasPlayerData(victim, "temp_no_chat")) viewing.remove(victim);
		}
	}
	
	private void pl(Player player, AsyncPlayerChatEvent event)
	{
		HashMap<String, ArrayList<String>> alliances = new HashMap<String, ArrayList<String>>();
		
		for(Player onlinePlayer : DMiscUtil.getPlugin().getServer().getOnlinePlayers())
		{
			int currentCharID = DPlayerUtil.getCurrentChar(player);

			if(DCharUtil.isImmortal(onlinePlayer))
			{
				if (!alliances.containsKey(DCharUtil.getAlliance(currentCharID).toUpperCase()))  alliances.put(DCharUtil.getAlliance(currentCharID).toUpperCase(), new ArrayList<String>());
				
				alliances.get(DCharUtil.getAlliance(currentCharID).toUpperCase()).add(onlinePlayer.getName());
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

