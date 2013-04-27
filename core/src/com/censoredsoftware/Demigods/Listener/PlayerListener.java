package com.censoredsoftware.Demigods.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.censoredsoftware.Demigods.API.BattleAPI;
import com.censoredsoftware.Demigods.API.DeityAPI;
import com.censoredsoftware.Demigods.API.PlayerAPI;
import com.censoredsoftware.Demigods.API.ZoneAPI;
import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacter;

public class PlayerListener implements Listener
{
	public static boolean filterCheckGeneric = false;
	public static boolean filterCheckStream = false;
	public static boolean filterCheckOverflow = false;
	public static boolean filterCheckQuitting = false;
	public static boolean filterCheckTimeout = false;

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// Define Variables
		Player player = event.getPlayer();
		PlayerCharacter character = PlayerAPI.getCurrentChar(player);

		// Set their lastlogintime
		DemigodsData.playerData.saveData(player, "player_lastlogin", System.currentTimeMillis());

		// Set Displayname
		if(character != null)
		{
			String name = character.getName();
			ChatColor color = DeityAPI.getDeityColor(character.isDeity());
			player.setDisplayName(color + name + ChatColor.WHITE);
			player.setPlayerListName(color + name + ChatColor.WHITE);
		}

		if(Demigods.config.getSettingBoolean("misc.welcome_message"))
		{
			player.sendMessage(ChatColor.GRAY + "This server is running Demigods version: " + ChatColor.YELLOW + Demigods.demigods.getDescription().getVersion());
			player.sendMessage(ChatColor.GRAY + "Type " + ChatColor.GREEN + "/dg" + ChatColor.GRAY + " for more information.");
		}
	}

	/*
	 * TODO
	 * 
	 * @EventHandler(priority = EventPriority.HIGHEST)
	 * public void onPlayerCraft(CraftItemEvent event)
	 * {
	 * // Define variables
	 * Player player = (Player) event.getWhoClicked();
	 * InventoryType invType = event.getInventory().getType();
	 * ArrayList<ItemStack> allSouls = DSouls.returnAllSouls();
	 * 
	 * if(invType.equals(InventoryType.CRAFTING) || invType.equals(InventoryType.WORKBENCH))
	 * {
	 * ItemStack[] invItems = event.getInventory().getContents();
	 * 
	 * for(ItemStack soul : allSouls)
	 * {
	 * for(ItemStack invItem : invItems)
	 * {
	 * if(invItem.isSimilar(soul))
	 * {
	 * event.setCancelled(true);
	 * player.sendMessage(ChatColor.RED + "You cannot craft with souls!");
	 * }
	 * }
	 * }
	 * }
	 * }
	 */

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		// Define variables
		final Player player = event.getPlayer();
		Location to = event.getTo();
		Location from = event.getFrom();
		int delayTime = Demigods.config.getSettingInt("pvp_area_delay_time");

		// No-PVP Zones
		onPlayerLineJump(player, to, from, delayTime);

		// Player Hold
		if(DemigodsData.tempPlayerData.containsKey(player, "temp_player_hold"))
		{
			if(from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ())
			{
				event.setCancelled(true);
				player.teleport(from);
				DemigodsData.tempPlayerData.saveData(player, "temp_player_held", true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		// Define variables
		final Player player = event.getPlayer();
		Location to = event.getTo();
		Location from = event.getFrom();
		int delayTime = Demigods.config.getSettingInt("zones.pvp_area_delay_time");

		// No-PVP Zones
		if(event.getCause() == TeleportCause.ENDER_PEARL || DemigodsData.tempPlayerData.containsKey(player, "temp_teleport_ability"))
		{
			onPlayerLineJump(player, to, from, delayTime);
		}
		else if(ZoneAPI.enterZoneNoPVP(to, from))
		{
			DemigodsData.tempPlayerData.removeData(player, "temp_was_PVP");
			player.sendMessage(ChatColor.GRAY + "You are now safe from all PVP!");
		}
		else if(ZoneAPI.exitZoneNoPVP(to, from))
		{
			player.sendMessage(ChatColor.GRAY + "You can now PVP!");
			return;
		}

		// Player Hold
		if(DemigodsData.tempPlayerData.containsKey(player, "temp_player_held")) DemigodsData.tempPlayerData.removeData(player, "temp_player_held");
		else if(DemigodsData.tempPlayerData.containsKey(player, "temp_player_hold")) event.setCancelled(true);
	}

	public void onPlayerLineJump(final Player player, Location to, Location from, int delayTime)
	{
		// NullPointer Check
		if(to == null || from == null) return;

		if(DemigodsData.tempPlayerData.containsKey(player, "temp_was_PVP")) return;

		// No Spawn Line-Jumping
		if(ZoneAPI.enterZoneNoPVP(to, from) && delayTime > 0)
		{
			DemigodsData.tempPlayerData.saveData(player, "temp_was_PVP", true);
			if(DemigodsData.tempPlayerData.containsKey(player, "temp_teleport_ability")) DemigodsData.tempPlayerData.removeData(player, "temp_teleport_ability");

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Demigods.demigods, new Runnable()
			{
				@Override
				public void run()
				{
					DemigodsData.tempPlayerData.removeData(player, "temp_was_PVP");
					if(ZoneAPI.zoneNoPVP(player.getLocation())) player.sendMessage(ChatColor.GRAY + "You are now safe from all PVP!");
				}
			}, (delayTime * 20));
		}

		// Let players know where they can PVP
		if(!DemigodsData.tempPlayerData.containsKey(player, "temp_was_PVP"))
		{
			if(ZoneAPI.exitZoneNoPVP(to, from)) player.sendMessage(ChatColor.GRAY + "You can now PVP!");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		String name = event.getPlayer().getName();
		if(filterCheckGeneric)
		{
			String message = ChatColor.YELLOW + name + " has either lost connection or crashed.";
			event.setQuitMessage(message);
		}
		else if(filterCheckStream)
		{
			String message = ChatColor.YELLOW + name + " has lost connection.";
			event.setQuitMessage(message);
		}
		else if(filterCheckOverflow)
		{
			String message = ChatColor.YELLOW + name + " has disconnected due to overload.";
			event.setQuitMessage(message);
		}
		else if(filterCheckQuitting)
		{
			if(ZoneAPI.canTarget(event.getPlayer()) && BattleAPI.isInAnyActiveBattle(PlayerAPI.getCurrentChar(event.getPlayer())))
			{
				String message = ChatColor.YELLOW + name + " has PvP Logged."; // TODO
				event.setQuitMessage(message);
				return;
			}
			String message = ChatColor.YELLOW + name + " has left the game.";
			event.setQuitMessage(message);
		}
		else if(filterCheckTimeout)
		{
			String message = ChatColor.YELLOW + name + " has disconnected due to timeout.";
			event.setQuitMessage(message);
		}
	}
}
