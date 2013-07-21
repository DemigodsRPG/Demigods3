package com.censoredsoftware.Demigods.Engine.Module;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.censoredsoftware.Demigods.Engine.Utility.UnicodeUtility;

/**
 * Module to handle all common messages sent to players or the console.
 */
public class MessageModule
{
	private static Logger log = Logger.getLogger("Minecraft");
	private Plugin plugin;
	private FontModule font;
	private boolean tag;
	private String pluginName;

	/**
	 * Constructor for the MessageModule.
	 * 
	 * @param instance The current instance of the Plugin running this module.
	 */
	public MessageModule(Plugin instance, FontModule font, boolean tag)
	{
		this.plugin = instance;
		this.pluginName = plugin.getName();
		this.font = font;
		this.tag = tag;
	}

	/**
	 * Sends the message <code>msg</code> as a tagged message to the <code>sender</code>.
	 * 
	 * @param sender The CommandSender to send the message to (allows console messages).
	 */
	public void tagged(CommandSender sender, String msg)
	{
		if(tag) sender.sendMessage(ChatColor.RED + "[" + pluginName + "] " + ChatColor.RESET + msg);
		else sender.sendMessage(msg);
	}

	/**
	 * Creates a title ready for use in the chat with the following format:
	 * > Title Here -------------------------------------
	 * 
	 * @param title the title to return
	 * @return String
	 */
	public String chatTitle(String title)
	{
		int remaining = font.getRemainingChatWidth(UnicodeUtility.rightwardArrow() + title + "   ");
		String dashes = "";
		for(int i = 0; i + remaining >= font.getChatBoxWidth(); i += 6)
		{
			dashes += "-";
			broadcast("" + i); // TODO Testing.
		}

		return " " + UnicodeUtility.rightwardArrow() + " " + title + " " + dashes;
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
