package com.censoredsoftware.Demigods.API;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.DemigodsData;

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
		return Demigods.permission.hasPermissionOrOP(player.getPlayer(), "demigods.admin") && DemigodsData.tempPlayerData.containsKey(player, "temp_admin_wand") && DemigodsData.tempPlayerData.getDataBool(player, "temp_admin_wand");
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
		return Demigods.permission.hasPermissionOrOP(player.getPlayer(), "demigods.admin") && DemigodsData.tempPlayerData.containsKey(player, "temp_admin_debug") && DemigodsData.tempPlayerData.getDataBool(player, "temp_admin_debug");
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
		for(Player player : PlayerAPI.getOnlineAdmins())
		{
			if(playerDebugEnabled(player)) player.sendMessage(ChatColor.RED + "[Debug] " + message);
		}
	}
}
