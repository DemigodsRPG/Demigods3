package com.demigodsrpg.demigods.engine.util;

import com.demigodsrpg.demigods.engine.DemigodsPlugin;
import com.demigodsrpg.demigods.engine.event.DemigodsChatEvent;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

/**
 * Module to handle all common messages sent to players or the console.
 */
public class Messages
{
	private static final Logger LOGGER;
	private static final String PLUGIN_NAME;
	private static final int LINE_SIZE, IN_GAME_LINE_SIZE;

	/**
	 * Constructor for the Messages.
	 * 
	 * @param instance The current instance of the Plugin running this module.
	 */
	static
	{
		LOGGER = DemigodsPlugin.getInst().getLogger();
		PLUGIN_NAME = DemigodsPlugin.getInst().getName();
		LINE_SIZE = 59 - PLUGIN_NAME.length();
		IN_GAME_LINE_SIZE = 54;
	}

	/**
	 * Sends the message <code>msg</code> as a tagged message to the <code>sender</code>.
	 * 
	 * @param sender The CommandSender to send the message to (allows console messages).
	 */
	public static void tagged(CommandSender sender, String msg)
	{
		if(msg.length() + PLUGIN_NAME.length() + 3 > IN_GAME_LINE_SIZE)
		{
			for(String line : wrapInGame(ChatColor.RED + "[" + PLUGIN_NAME + "] " + ChatColor.RESET + msg))
				sender.sendMessage(line);
			return;
		}
		sender.sendMessage(ChatColor.RED + "[" + PLUGIN_NAME + "] " + ChatColor.RESET + msg);
	}

	/**
	 * Sends the console message <code>msg</code> with "info" tag.
	 * 
	 * @param msg The message to be sent.
	 */
	public static void info(String msg)
	{
		if(msg.length() > LINE_SIZE)
		{
			for(String line : wrapConsole(msg))
				LOGGER.info(line);
			return;
		}
		LOGGER.info(msg);
	}

	/**
	 * Sends the console message <code>msg</code> with "warning" tag.
	 * 
	 * @param msg The message to be sent.
	 */
	public static void warning(String msg)
	{
		if(msg.length() > LINE_SIZE)
		{
			for(String line : wrapConsole(msg))
				LOGGER.warning(line);
			return;
		}
		LOGGER.warning(msg);
	}

	/**
	 * Sends the console message <code>msg</code> with "severe" tag.
	 * 
	 * @param msg The message to be sent.
	 */
	public static void severe(String msg)
	{
		if(msg.length() >= LINE_SIZE)
		{
			for(String line : wrapConsole(msg))
				LOGGER.severe(line);
			return;
		}
		LOGGER.severe(msg);
	}

	public static String[] wrapConsole(String msg)
	{
		return WordUtils.wrap(msg, LINE_SIZE, "/n", false).split("/n");
	}

	/**
	 * Broadcast to the entire server (all players and the console) the message <code>msg</code>.
	 * 
	 * @param msg The message to be sent.
	 */
	public static void broadcast(String msg)
	{
		if(ChatColor.stripColor(msg).length() > IN_GAME_LINE_SIZE)
		{
			Server server = DemigodsPlugin.getInst().getServer();
			for(String line : wrapInGame(msg))
			{
				DemigodsChatEvent chatEvent = new DemigodsChatEvent(line);
				Bukkit.getPluginManager().callEvent(chatEvent);
				if(!chatEvent.isCancelled()) server.broadcastMessage(line);
			}
			return;
		}
		DemigodsChatEvent chatEvent = new DemigodsChatEvent(msg);
		Bukkit.getPluginManager().callEvent(chatEvent);
		if(!chatEvent.isCancelled()) DemigodsPlugin.getInst().getServer().broadcastMessage(msg);
	}

	public static String[] wrapInGame(String msg)
	{
		return WordUtils.wrap(msg, IN_GAME_LINE_SIZE, "/n", false).split("/n");
	}

	/**
	 * Let the <code>sender</code> know it does not have permission.
	 * 
	 * @param sender The CommandSender being notified.
	 * @return True.
	 */
	public static boolean noPermission(CommandSender sender)
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
	public static boolean noConsole(ConsoleCommandSender console)
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
	public static boolean noPlayer(Player player)
	{
		player.sendMessage("That can only be executed by the console.");
		return true;
	}

	/**
	 * Clears the chat for <code>player</code> using .sendMessage().
	 * 
	 * @param player the player whose chat to clear.
	 */
	public static void clearChat(Player player)
	{
		for(int x = 0; x < 120; x++)
			player.sendMessage(" ");
	}

	/**
	 * Clears the chat for <code>player</code> using .sendRawMessage().
	 * 
	 * @param player the player whose chat to clear.
	 */
	public static void clearRawChat(Player player)
	{
		for(int x = 0; x < 120; x++)
			player.sendRawMessage(" ");
	}
}
