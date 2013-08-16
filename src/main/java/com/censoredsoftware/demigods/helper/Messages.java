package com.censoredsoftware.demigods.helper;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

/**
 * Module to handle all common messages sent to players or the console.
 */
public class Messages
{
	private static final Logger log = Logger.getLogger("Minecraft");
	private Plugin plugin;
	private String pluginName;

	/**
	 * Constructor for the Messages.
	 * 
	 * @param instance The current instance of the Plugin running this module.
	 */
	public Messages(Plugin instance)
	{
		plugin = instance;
		pluginName = plugin.getName();
	}

	/**
	 * Sends the message <code>msg</code> as a tagged message to the <code>sender</code>.
	 * 
	 * @param sender The CommandSender to send the message to (allows console messages).
	 */
	public void tagged(CommandSender sender, String msg)
	{
		sender.sendMessage(ChatColor.RED + "[" + pluginName + "] " + ChatColor.RESET + msg);
	}

	/**
	 * Sends the console message <code>msg</code> with "info" tag.
	 * 
	 * @param msg The message to be sent.
	 */
	public void info(String msg)
	{
		log.info("[" + pluginName + "] " + msg);
	}

	/**
	 * Sends the console message <code>msg</code> with "warning" tag.
	 * 
	 * @param msg The message to be sent.
	 */
	public void warning(String msg)
	{
		log.warning("[" + pluginName + "] " + msg);
	}

	/**
	 * Sends the console message <code>msg</code> with "severe" tag.
	 * 
	 * @param msg The message to be sent.
	 */
	public void severe(String msg)
	{
		log.severe("[" + pluginName + "] " + msg);
	}

	/**
	 * Broadcast to the entire server (all players and the console) the message <code>msg</code>.
	 * 
	 * @param msg The message to be sent.
	 */
	public void broadcast(String msg)
	{
		plugin.getServer().broadcastMessage(msg);
	}

	/**
	 * Let the <code>sender</code> know it does not have permission.
	 * 
	 * @param sender The CommandSender being notified.
	 * @return True.
	 */
	public boolean noPermission(CommandSender sender)
	{
		sender.sendMessage(ChatColor.RED + "You do not have permission to do that.");
		return true;
	}

	/**
	 * Let the <code>console</code> know it cannot continue.
	 * 
	 * @param console The console.
	 * @return True.
	 */
	public boolean noConsole(ConsoleCommandSender console)
	{
		console.sendMessage("That can only be executed by a player.");
		return true;
	}

	/**
	 * Let the <code>player</code> know it cannot continue.
	 * 
	 * @param player The Player being notified.
	 * @return True.
	 */
	public boolean noPlayer(Player player)
	{
		player.sendMessage("That can only be executed by the console.");
		return true;
	}

	/**
	 * Clears the chat for <code>player</code> using .sendMessage().
	 * 
	 * @param player the player whose chat to clear.
	 */
	public void clearChat(Player player)
	{
		for(int x = 0; x < 120; x++)
			player.sendMessage(" ");
	}

	/**
	 * Clears the chat for <code>player</code> using .sendRawMessage().
	 * 
	 * @param player the player whose chat to clear.
	 */
	public void clearRawChat(Player player)
	{
		for(int x = 0; x < 120; x++)
			player.sendRawMessage(" ");
	}
}
