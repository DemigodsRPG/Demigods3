package com.censoredsoftware.Demigods.API;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedPlayer;

public class AdminAPI
{
	/**
	 * Returns true if the <code>player</code> is an admin and has their admin wand enabled.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean wandEnabled(OfflinePlayer player)
	{
		return Demigods.permission.hasPermissionOrOP(player.getPlayer(), "demigods.admin") && DemigodsData.hasKeyTemp(player.getName(), "temp_admin_wand") && Boolean.parseBoolean(DemigodsData.getValueTemp(player.getName(), "temp_admin_wand").toString());
	}

	/**
	 * Returns true if the <code>player</code>'s admin wand is enabled and in their hand.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean useWand(OfflinePlayer player)
	{
		return wandEnabled(player) && player.getPlayer().getItemInHand().getTypeId() == Demigods.config.getSettingInt("admin.wand_tool");
	}

	/**
	 * Returns true if <code>player</code>'s Demigods debugging is enabled.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public static boolean playerDebugEnabled(OfflinePlayer player)
	{
		return Demigods.permission.hasPermissionOrOP(player.getPlayer(), "demigods.admin") && DemigodsData.hasKeyTemp(player.getName(), "temp_admin_debug") && Boolean.parseBoolean(DemigodsData.getValueTemp(player.getName(), "temp_admin_debug").toString());
	}

	/**
	 * Returns true if console debugging is enabled in the config.
	 * 
	 * @return boolean
	 */
	public static boolean consoleDebugEnabled()
	{
		return Demigods.config.getSettingBoolean("misc.console_debug");
	}

	/**
	 * Sends the <code>message</code> to all applicable recipients.
	 * 
	 * @param message the message to send.
	 */
	public static void sendDebug(String message)
	{
		// Log to console
		if(consoleDebugEnabled()) Demigods.message.info("[Debug] " + ChatColor.stripColor(message));

		// Log to online, debugging admins
		for(Player player : TrackedPlayer.getOnlineAdmins())
		{
			if(playerDebugEnabled(player)) player.sendMessage(ChatColor.RED + "[Debug] " + message);
		}
	}
}
