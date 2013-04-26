package com.censoredsoftware.Modules;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.censoredsoftware.Modules.DataPersistence.PlayerDataModule;
import com.censoredsoftware.Modules.DataPersistence.YAMLPersistenceModule;

/**
 * Module to handle the latest messages from a Twitter feed.
 */
public class LatestTweetModule implements Listener
{
	private Plugin plugin;
	private Logger log = Logger.getLogger("Minecraft");
	private PlayerDataModule messagesData;
	private YAMLPersistenceModule messagesYAML;
	private URL twitterFeed;
	private String pluginName, command, permission, date, link, message;
	private boolean notify;

	/**
	 * Constructor to create a new LatestTweetModule.
	 * 
	 * @param plugin The plugin instance running the module.
	 * @param url The url to the Twitter project RSS feed.
	 * @param command The full command for viewing the latest message.
	 * @param permission The full permission node for viewing the latest message.
	 * @param notify True if notifying is allowed.
	 */
	public LatestTweetModule(Plugin plugin, String url, String command, String permission, boolean notify, int start_delay, int save_interval)
	{
		try
		{
			this.plugin = plugin;
			this.twitterFeed = new URL(url);
			this.pluginName = this.plugin.getName();
			this.command = command;
			this.permission = permission;
			this.notify = notify;

			this.messagesData = new PlayerDataModule(plugin, "official_messages");
			this.messagesYAML = new YAMLPersistenceModule(true, plugin, null, "official_messages");

			initilize(start_delay, save_interval);
		}
		catch(Exception e)
		{
			log.severe("[" + pluginName + "] Could not connect to Twitter.");
		}
	}

	/**
	 * Checks for notifications and notifies if need be.
	 */
	public void initilize(int start_delay, int save_interval)
	{
		// Check for updates, and then update if need be
		if(this.notify)
		{
			// Define Notify Listener
			plugin.getServer().getPluginManager().registerEvents(this, plugin);

			// Start Save Thread
			startSaveThread(start_delay, save_interval);

			for(final Player player : Bukkit.getOnlinePlayers())
			{
				if(get(player))
				{
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
					{
						@Override
						public void run()
						{
							player.sendMessage(ChatColor.GREEN + "There is a new message from the " + pluginName + " developers!");
							player.sendMessage("Please view it now by using " + ChatColor.YELLOW + command);
						}
					}, 40);
				}
			}
		}
	}

	/**
	 * Gets the latest message from the Twitter feed.
	 * 
	 * @return True if successful.
	 */
	public synchronized boolean get(OfflinePlayer player)
	{
		try
		{
			InputStream input = this.twitterFeed.openConnection().getInputStream();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);

			NodeList messageNodes = document.getElementsByTagName("item").item(0).getChildNodes();

			try
			{
				this.date = messageNodes.item(5).getTextContent().substring(0, messageNodes.item(5).getTextContent().lastIndexOf("+"));
				this.link = messageNodes.item(9).getTextContent().replace("http://", "https://");
			}
			catch(Exception e)
			{
				log.warning("[" + pluginName + "] Failed to find latest tweet.");
			}
			input.close();

			try
			{
				URLConnection messageCon = (new URL(this.link)).openConnection();
				messageCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"); // FIXES 403 ERROR
				input = messageCon.getInputStream();
			}
			catch(Exception e)
			{
				log.warning("[" + pluginName + "] Failed to open connection with twitter page.");
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line;

			while((line = reader.readLine()) != null)
			{
				if(line.trim().startsWith("<p class=\"js-tweet-text tweet-text \">"))
				{
					this.message = line.substring(line.indexOf("<p class=\"js-tweet-text tweet-text \">") + 37, line.lastIndexOf("<"));
					break;
				}
			}

			reader.close();
			input.close();

			String lastMessage = messagesData.getDataString(player);

			if(lastMessage != null && lastMessage.equalsIgnoreCase(this.message)) return false;
			else return true;
		}
		catch(Exception e)
		{
			log.warning("[" + pluginName + "] Failed to read twitter page.");
		}
		return false;
	}

	/**
	 * Save the official message data.
	 */
	private void startSaveThread(int start_delay, int save_interval)
	{
		// Start the Save Thread
		plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if(!messagesYAML.save(messagesData))
				{
					messagesYAML.revertBackup();
					messagesYAML.load();
				}
			}
		}, start_delay, save_interval);
	}

	/**
	 * The Player Join Listener, listening only.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// Define Variables
		final Player player = event.getPlayer();
		final String pluginName = this.pluginName;
		final String command = this.command;

		// Official Messages
		if(this.notify && (player.isOp() || player.hasPermission(this.permission)))
		{
			if(get(player))
			{
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
				{
					@Override
					public void run()
					{
						player.sendMessage(ChatColor.GREEN + "There is a new message from the " + pluginName + " developers!");
						player.sendMessage("Please view it now by using " + ChatColor.YELLOW + command);
					}
				}, 40);
			}
		}
	}

	/**
	 * The Player Command Preprocess Listener, cancelling the event if the message command is found, and executing the command inside of the listener.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		// Define Variables
		Player player = event.getPlayer();
		String command = event.getMessage();

		// Check for update command
		if(command.toLowerCase().startsWith(this.command.toLowerCase()))
		{
			// Check Permissions
			if(!(player.hasPermission(this.permission) || player.isOp()))
			{
				player.sendMessage(ChatColor.RED + "You do not have permission to run this command.");
				event.setCancelled(true);
				return;
			}

			// Send the message
			player.sendMessage(ChatColor.DARK_AQUA + "[" + pluginName + "] " + ChatColor.RESET + "Posted on " + this.date);
			player.sendMessage(ChatColor.YELLOW + " Message: " + ChatColor.WHITE + this.message);
			player.sendMessage("  ");
			player.sendMessage(ChatColor.YELLOW + " " + ChatColor.WHITE + this.link.replace("https://", ""));

			// Set that the message was seen
			this.messagesData.saveData(player, this.message);
			event.setCancelled(true);
		}
	}
}
