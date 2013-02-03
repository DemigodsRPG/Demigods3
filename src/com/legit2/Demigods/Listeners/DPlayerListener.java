package com.legit2.Demigods.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.kitteh.tag.TagAPI;

import com.legit2.Demigods.Demigods;
import com.legit2.Demigods.Utilities.DConfigUtil;
import com.legit2.Demigods.Utilities.DDataUtil;
import com.legit2.Demigods.Utilities.DPlayerUtil;
import com.legit2.Demigods.Utilities.DMiscUtil;
import com.legit2.Demigods.Utilities.DZoneUtil;

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
		try 
		{
			Player player = event.getPlayer();		
			if(Bukkit.getServer().hasWhitelist())
			{
				if(player.isWhitelisted()) DPlayerUtil.createNewPlayer(player);
				return;
			}
			else DPlayerUtil.createNewPlayer(player);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) throws EventException
	{	
		// Define Variables
		Player player = event.getPlayer();

		// Set their lastlogintime
		DDataUtil.savePlayerData(player, "player_lastlogin", System.currentTimeMillis());
	
		// TagAPI support
		if(plugin.TAGAPI != null)
		{
			TagAPI.refreshPlayer(player);
			
			for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers())
			{
				if(onlinePlayer == player) continue;
				TagAPI.refreshPlayer(onlinePlayer, player);
			}
		}
			
		// if(!DConfigUtilUtil.getEnabledWorlds().contains(player.getWorld())) return;
		
		if(DConfigUtil.getSettingBoolean("motd"))
		{
			player.sendMessage(ChatColor.GRAY + "This server is running Demigods version: " + ChatColor.YELLOW + DMiscUtil.getPlugin().getDescription().getVersion());
			player.sendMessage(ChatColor.GRAY + "Type "+ChatColor.GREEN + "/dg" + ChatColor.GRAY + " for more information.");
		}
		
		/*
		if((!DConfigUtilUtil.getSettingBoolean("auto_update")) && (DUpdate.shouldUpdate()) && DMiscUtil.hasPermissionOrOP(player, "demigods.admin"))
		{
			player.sendMessage(ChatColor.RED + "There is a new, stable release for Demigods.");
			player.sendMessage(ChatColor.RED + "Please update ASAP.");
			player.sendMessage(ChatColor.RED + "Latest: " + ChatColor.GREEN + "dev.bukkit.org/server-mods/demigods");
		}
		*/
	}
	
	/*
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
	*/
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		// Define variables
		final Player player = event.getPlayer();
		Location to = event.getTo();
		Location from = event.getFrom();
		int delayTime = DConfigUtil.getSettingInt("pvp_area_delay_time");
		onPlayerLineJump(player, to, from, delayTime);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		// Define variables
		final Player player = event.getPlayer();
		Location to = event.getTo();
		Location from = event.getFrom();
		int delayTime = DConfigUtil.getSettingInt("pvp_area_delay_time");
		
		if(event.getCause() == TeleportCause.ENDER_PEARL || DDataUtil.hasPlayerData((Player) player, "temp_teleport_ability"))
		{
			onPlayerLineJump(player, to, from, delayTime);
		}
		else if(DZoneUtil.enterZoneNoPVP(to, from))
		{
			DDataUtil.removePlayerData(player, "temp_was_PVP");
			player.sendMessage(ChatColor.GRAY + "You are now safe from all PVP!");
		}
		else if(DZoneUtil.exitZoneNoPVP(to, from)) player.sendMessage(ChatColor.GRAY + "You can now PVP!");
	}
	
	public void onPlayerLineJump(final Player player, Location to, Location from, int delayTime)
	{
		// NullPointer Check
		if(to == null || from == null) return;
		
		if(DDataUtil.hasPlayerData((Player) player, "temp_was_PVP")) return;
		
		// No Spawn Line-Jumping
		if(DZoneUtil.enterZoneNoPVP(to, from) && delayTime > 0)
		{
			DDataUtil.savePlayerData(player, "temp_was_PVP", true);
			if(DDataUtil.hasPlayerData(player, "temp_teleport_ability")) DDataUtil.removePlayerData(player, "temp_teleport_ability");
			
			DMiscUtil.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(DMiscUtil.getPlugin(), new Runnable()
			{
				@Override
				public void run()
				{
					DDataUtil.removePlayerData(player, "temp_was_PVP");
					if(DZoneUtil.zoneNoPVP(player.getLocation())) player.sendMessage(ChatColor.GRAY + "You are now safe from all PVP!");
				}
			}, (delayTime * 20));
		}
		
		// Let players know where they can PVP
		if(!DDataUtil.hasPlayerData((Player) player, "temp_was_PVP"))
		{
			if(DZoneUtil.exitZoneNoPVP(to, from)) player.sendMessage(ChatColor.GRAY + "You can now PVP!");
		}
	}
}