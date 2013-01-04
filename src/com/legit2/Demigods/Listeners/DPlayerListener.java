package com.legit2.Demigods.Listeners;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import com.legit2.Demigods.DConfig;
import com.legit2.Demigods.DDatabase;
import com.legit2.Demigods.DUpdate;
import com.legit2.Demigods.DUtil;
import com.legit2.Demigods.Demigods;

public class DPlayerListener implements Listener
{
	static Demigods plugin;
	
	public DPlayerListener(Demigods instance)
	{
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		String username = event.getPlayer().getName();
		
		try 
		{
			DDatabase.addPlayer(username);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) throws EventException
	{
		// Define Variables
		Player player = event.getPlayer();
		
		// if(!DConfig.getEnabledWorlds().contains(player.getWorld())) return;
		
		if(DConfig.getSettingBoolean("motd"))
		{
			player.sendMessage("This server is running Demigods v"+ChatColor.YELLOW+DUtil.getPlugin().getDescription().getVersion()+ChatColor.WHITE+".");
			player.sendMessage(ChatColor.GRAY+"Type "+ChatColor.GREEN+"/dg"+ChatColor.GRAY+" for more info.");
		}
		
		if((!DConfig.getSettingBoolean("auto_update")) && (DUpdate.shouldUpdate()) && DUtil.hasPermissionOrOP(player, "demigods.admin"))
		{
			player.sendMessage(ChatColor.RED + "There is a new, stable release for Demigods.");
			player.sendMessage(ChatColor.RED + "Please update ASAP.");
			player.sendMessage(ChatColor.RED + "Latest: " + ChatColor.GREEN + "dev.bukkit.org/server-mods/demigods");
		}
	}
}
