package com.legit2.Demigods.Listeners;

import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.legit2.Demigods.DConfig;
import com.legit2.Demigods.DDatabase;
import com.legit2.Demigods.DSave;
import com.legit2.Demigods.DSouls;
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
		
		DUtil.setPlayerData(player.getName(), "lastlogintime", System.currentTimeMillis());
		
		// if(!DConfig.getEnabledWorlds().contains(player.getWorld())) return;
		
		if(DConfig.getSettingBoolean("motd"))
		{
			player.sendMessage(ChatColor.GRAY + "This server is running Demigods version: " + ChatColor.YELLOW + DUtil.getPlugin().getDescription().getVersion());
			player.sendMessage(ChatColor.GRAY + "Type "+ChatColor.GREEN + "/dg" + ChatColor.GRAY + " for more information.");
		}
		
		/*
		if((!DConfig.getSettingBoolean("auto_update")) && (DUpdate.shouldUpdate()) && DUtil.hasPermissionOrOP(player, "demigods.admin"))
		{
			player.sendMessage(ChatColor.RED + "There is a new, stable release for Demigods.");
			player.sendMessage(ChatColor.RED + "Please update ASAP.");
			player.sendMessage(ChatColor.RED + "Latest: " + ChatColor.GREEN + "dev.bukkit.org/server-mods/demigods");
		}
		*/
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCraft(CraftItemEvent event)
	{
		// Define variables
		Player player = (Player) event.getWhoClicked();
		InventoryType invType = event.getInventory().getType();
		ArrayList<ItemStack> allSouls = DSouls.returnAllSouls();
		
		if(invType.equals(InventoryType.CRAFTING) || invType.equals(InventoryType.WORKBENCH))
		{
			ItemStack[] invItems = event.getInventory().getContents();
			
			for(ItemStack soul : allSouls)
			{
				for(ItemStack invItem : invItems)
				{
					if(invItem.isSimilar(soul)) 
					{
						event.setCancelled(true);
						player.sendMessage(ChatColor.RED + "You cannot craft with souls!");
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		// Define variables
		final Player player = (Player) event.getPlayer();
		final String username = player.getName();
		int pvp_area_delay_time = DConfig.getSettingInt("pvp_area_delay_time");
		Location to = event.getTo();
		Location from = event.getFrom();
			
		// Save the time that a player left a PVP zone
		if(!DUtil.canLocationPVP(to) && DUtil.canLocationPVP(from))
		{
			DSave.savePlayerData(username, "was_PVP_temp", true);
			
			DUtil.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(DUtil.getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					DSave.removePlayerData(username, "was_PVP_temp");
					player.sendMessage(ChatColor.YELLOW + "You are now safe from PVP!");
				}
			}, pvp_area_delay_time);
		}
		
	}
}