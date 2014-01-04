package com.censoredsoftware.demigods.engine.util;

import com.censoredsoftware.demigods.engine.data.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Admins
{
	/**
	 * Returns true if the <code>player</code> is an admin and has their admin wand enabled.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean wandEnabled(OfflinePlayer player)
	{
		return player.getPlayer().hasPermission("demigods.admin") && Data.hasKeyTemp(player.getName(), "temp_admin_wand") && Boolean.parseBoolean(Data.getValueTemp(player.getName(), "temp_admin_wand").toString());
	}

	/**
	 * Returns true if the <code>player</code>'s admin wand is enabled and in their hand.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean useWand(OfflinePlayer player)
	{
		return wandEnabled(player) && player.getPlayer().getItemInHand().getTypeId() == Configs.getSettingInt("admin.wand_tool");
	}

	/**
	 * Toggles the admin wand for the <code>player</code> to <code>option</code>.
	 * 
	 * @param player the player to toggle for.
	 * @param option the option to toggle to.
	 */
	public static void toggleWand(OfflinePlayer player, boolean option)
	{
		if(option)
		{
			Data.saveTemp(player.getName(), "temp_admin_wand", true);
		}
		else
		{
			Data.removeTemp(player.getName(), "temp_admin_wand");
		}
	}

	/**
	 * Returns true if the <code>player</code> is an admin and has their structure wand enabled.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean structureWandEnabled(OfflinePlayer player)
	{
		return player.getPlayer().hasPermission("demigods.admin") && Data.hasKeyTemp(player.getName(), "temp_admin_structurewand") && Boolean.parseBoolean(Data.getValueTemp(player.getName(), "temp_admin_structurewand").toString());
	}

	/**
	 * Returns true if the <code>player</code>'s structure wand is enabled and in their hand.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean useStructureWand(OfflinePlayer player)
	{
		return structureWandEnabled(player) && player.getPlayer().getItemInHand().getTypeId() == Configs.getSettingInt("admin.structure_wand_tool");
	}

	/**
	 * Toggles the structure wand for the <code>player</code> to <code>option</code>.
	 * 
	 * @param player the player to toggle for.
	 * @param option the option to toggle to.
	 */
	public static void toggleStructureWand(OfflinePlayer player, boolean option)
	{
		if(option)
		{
			Data.saveTemp(player.getName(), "temp_admin_structurewand", true);
		}
		else
		{
			Data.removeTemp(player.getName(), "temp_admin_structurewand");
		}
	}

	/**
	 * Toggles the debugging for the <code>player</code> to <code>option</code>.
	 * 
	 * @param player the player to toggle for.
	 * @param option the option to toggle to.
	 */
	public static void togglePlayerDebug(OfflinePlayer player, boolean option)
	{
		if(option)
		{
			Data.saveTemp(player.getName(), "temp_admin_debug", true);
		}
		else
		{
			Data.removeTemp(player.getName(), "temp_admin_debug");
		}
	}

	/**
	 * Returns true if <code>player</code>'s demigods debugging is enabled.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean playerDebugEnabled(OfflinePlayer player)
	{
		return player.getPlayer().hasPermission("demigods.admin") && Data.hasKeyTemp(player.getName(), "temp_admin_debug") && Boolean.parseBoolean(Data.getValueTemp(player.getName(), "temp_admin_debug").toString());
	}

	/**
	 * Returns true if console debugging is enabled in the config.
	 * 
	 * @return boolean
	 */
	public static boolean consoleDebugEnabled()
	{
		return Configs.getSettingBoolean("misc.console_debug");
	}

	/**
	 * Sends the <code>message</code> to all applicable recipients.
	 * 
	 * @param message the message to send.
	 */
	public static void sendDebug(String message)
	{
		// Log to console
		if(consoleDebugEnabled()) Messages.info("[Debug] " + ChatColor.stripColor(message));

		// Log to online, debugging admins
		for(Player player : getOnlineAdmins())
		{
			if(playerDebugEnabled(player)) player.sendMessage(ChatColor.RED + "[Debug] " + message);
		}
	}

	/**
	 * Returns an ArrayList of all online admins.
	 * 
	 * @return ArrayList
	 */
	public static ArrayList<Player> getOnlineAdmins()
	{
		ArrayList<Player> toReturn = new ArrayList<>();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(player.hasPermission("demigods.admin")) toReturn.add(player);
		}
		return toReturn;
	}
}
