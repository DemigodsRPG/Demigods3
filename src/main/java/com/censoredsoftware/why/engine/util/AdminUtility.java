package com.censoredsoftware.why.engine.util;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.censoredsoftware.why.engine.data.DataManager;

public class AdminUtility
{
	/**
	 * Returns true if the <code>player</code> is an admin and has their admin wand enabled.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean wandEnabled(OfflinePlayer player)
	{
		return player.getPlayer().hasPermission("demigods.admin") && DataManager.hasKeyTemp(player.getName(), "temp_admin_wand") && Boolean.parseBoolean(DataManager.getValueTemp(player.getName(), "temp_admin_wand").toString());
	}

	/**
	 * Returns true if the <code>player</code>'s admin wand is enabled and in their hand.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean useWand(OfflinePlayer player)
	{
		return wandEnabled(player) && player.getPlayer().getItemInHand().getTypeId() == ConfigUtility.getSettingInt("admin.wand_tool");
	}

	/**
	 * Returns true if <code>player</code>'s demigods debugging is enabled.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean playerDebugEnabled(OfflinePlayer player)
	{
		return player.getPlayer().hasPermission("demigods.admin") && DataManager.hasKeyTemp(player.getName(), "temp_admin_debug") && Boolean.parseBoolean(DataManager.getValueTemp(player.getName(), "temp_admin_debug").toString());
	}

	/**
	 * Returns true if console debugging is enabled in the config.
	 * 
	 * @return boolean
	 */
	public static boolean consoleDebugEnabled()
	{
		return ConfigUtility.getSettingBoolean("misc.console_debug");
	}

	/**
	 * Sends the <code>message</code> to all applicable recipients.
	 * 
	 * @param message the message to send.
	 */
	public static void sendDebug(String message)
	{
		// Log to console
		if(consoleDebugEnabled()) MessageUtility.info("[Debug] " + ChatColor.stripColor(message));

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
		ArrayList<Player> toReturn = new ArrayList<Player>();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(player.hasPermission("demigods.admin")) toReturn.add(player);
		}
		return toReturn;
	}
}
