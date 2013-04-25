package com.censoredsoftware.Modules;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Module to handle all permission related methods.
 */
public class PermissionModule
{
	/**
	 * Checks if <code>sender</code> has the <code>permission</code> node.
	 * 
	 * @param sender The CommandSender being checked.
	 * @param permission The permission node being checked.
	 * @return True if permission is granted.
	 */
	public boolean hasPermission(CommandSender sender, String permission)
	{
		if(sender instanceof ConsoleCommandSender) return true;
		return sender.hasPermission(permission);
	}

	/**
	 * Checks if <code>sender</code> has the <code>permission</code> node, or is an OP.
	 * 
	 * @param sender The Player being checked.
	 * @param permission The permission node being checked.
	 * @return True if permission is granted, or is OP.
	 */
	public boolean hasPermissionOrOP(CommandSender sender, String permission)
	{
		if(sender.isOp()) return true;
		return hasPermission(sender, permission);
	}

	/**
	 * Gets the player from <code>sender</code> unless the sender is the Console.
	 * 
	 * @param sender The CommandSender involved.
	 * @return The Player if exists, otherwise null for the Console.
	 */
	public Player getPlayer(CommandSender sender)
	{
		Player player = null;
		if(sender instanceof Player) player = (Player) sender;
		return player;
	}
}
