package com.censoredsoftware.Modules;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Module to handle all common messages sent to players or the console.
 */
public class MessageModule
{
	private Plugin plugin;
	private boolean tag;
	private String pluginName;
	private Logger log;

	/**
	 * Grab the Logger.
	 * 
	 * @return Logger.
	 */
	public Logger getLog()
	{
		return log;
	}

	/**
	 * Constructor for the MessageModule.
	 * 
	 * @param instance The current instance of the Plugin running this module.
	 */
	public MessageModule(Plugin instance, boolean tag)
	{
		this.plugin = instance;
		this.pluginName = plugin.getName();
		this.tag = tag;
		this.log = Logger.getLogger("Minecraft");
	}

	/**
	 * Sends the message <code>msg</code> as a tagged message to the <code>sender</code>.
	 * 
	 * @param sender The CommandSender to send the message to (allows console messages).
	 */
	public void tagged(CommandSender sender, String msg)
	{
		if(tag) sender.sendMessage(ChatColor.DARK_AQUA + "[" + pluginName + "] " + ChatColor.RESET + msg);
		else sender.sendMessage(msg);
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
}
