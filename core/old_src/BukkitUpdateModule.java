package com.censoredsoftware.Modules;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.censoredsoftware.Modules.Data.PlayerDataModule;

/**
 * Module to handle all update methods from BukkitDev.
 */
public class BukkitUpdateModule implements Listener
{
	private Plugin plugin;
	private Logger log = Logger.getLogger("Minecraft");
	private PlayerDataModule confirmData;
	private URL filesFeed;
	private String pluginName, command, permission, latestVersion, currentVersion, link, jarLink;
	private boolean supported, auto, notify;
	private int confirmTime;

	/**
	 * Constructor to create a new BukkitUpdateModule.
	 * 
	 * @param plugin The demigods instance running the module.
	 * @param url The url to the BukkitDev project RSS feed.
	 * @param command The full command for updating the demigods.
	 * @param permission The full permission node for updating the demigods.
	 * @param auto True if automatic updating is allowed.
	 * @param notify True if notifying is allowed.
	 * @param confirmTime Time in seconds to confirm the update command before it forgets.
	 */
	public BukkitUpdateModule(Plugin plugin, String url, String command, String permission, boolean auto, boolean notify, int confirmTime)
	{
		try
		{
			this.plugin = plugin;
			this.filesFeed = new URL(url);
			this.confirmData = new PlayerDataModule(this.plugin, "update_confirm", System.currentTimeMillis());
			this.pluginName = this.plugin.getName();
			this.command = command;
			this.permission = permission;
			this.currentVersion = plugin.getDescription().getVersion();
			this.supported = versionExists();
			this.auto = auto;
			this.notify = notify;
			this.confirmTime = confirmTime * 1000;

			initilize();
		}
		catch(Exception e)
		{
			log.severe("[" + pluginName + "] Could not connect to BukkitDev.");
		}
	}

	/**
	 * Checks for updates and auto-updates if need be.
	 */
	private void initilize()
	{
		// Check for updates, and then update if need be
		if(this.auto || this.notify || !this.supported)
		{
			// Define Update Listener
			plugin.getServer().getPluginManager().registerEvents(this, plugin);

			if(check() || !this.supported)
			{
				if(this.auto) download();
				if(!this.supported && this.notify)
				{
					Bukkit.broadcast(ChatColor.RED + "The version of " + ChatColor.YELLOW + this.pluginName + ChatColor.RED + " you are using is not supported.", this.permission);
					Bukkit.broadcast(ChatColor.RED + "It has been removed from BukkitDev. This " + ChatColor.ITALIC + "MAY" + ChatColor.RESET + ChatColor.RED + " be because it", this.permission);
					if(this.auto) Bukkit.broadcast(ChatColor.RED + "is not safe." + ChatColor.WHITE + " Please " + ChatColor.YELLOW + "reload the server " + ChatColor.WHITE + "to finish the auto-update.", this.permission);
					else
					{
						Bukkit.broadcast(ChatColor.RED + "is not safe. It is " + ChatColor.BOLD + "strongly" + ChatColor.RESET + ChatColor.RED + " suggested that you update soon.", this.permission);
						Bukkit.broadcast("Please update by using " + ChatColor.YELLOW + this.command, this.permission);
					}
				}
				else if(this.notify)
				{
					Bukkit.broadcast(ChatColor.RED + "There is a new, stable release for " + this.pluginName + ".", this.permission);
					if(this.auto) Bukkit.broadcast("Please " + ChatColor.YELLOW + "reload the server " + ChatColor.WHITE + "to finish the auto-update.", this.permission);
					else Bukkit.broadcast("Please update by using " + ChatColor.YELLOW + this.command, this.permission);
				}
			}
		}
	}

	/**
	 * Download the update JAR file from BukkitDev.
	 */
	private boolean download()
	{
		try
		{
			// Define variables
			byte[] buffer = new byte[1024];
			int read = 0;
			int bytesTransferred = 0;
			String downloadLink = this.jarLink;

			log.info("[" + pluginName + "] Attempting to download latest version...");

			// Set latest build URL
			URL plugin = new URL(downloadLink);

			// Open connection to latest build and set user-agent for download, also determine file size
			URLConnection pluginCon = plugin.openConnection();
			pluginCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2"); // FIXES 403 ERROR
			int contentLength = pluginCon.getContentLength();

			// Check for update directory
			File updateFolder = new File("plugins" + File.separator + Bukkit.getUpdateFolder());
			if(!updateFolder.exists()) updateFolder.mkdir();

			// Create new .jar file and add it to update directory
			File pluginUpdate = new File("plugins" + File.separator + Bukkit.getUpdateFolder() + File.separator + this.pluginName + ".jar");
			log.info("[" + pluginName + "] File will been written to: " + pluginUpdate.getCanonicalPath());

			InputStream is = pluginCon.getInputStream();
			OutputStream os = new FileOutputStream(pluginUpdate);

			while((read = is.read(buffer)) > 0)
			{
				os.write(buffer, 0, read);
				bytesTransferred += read;

				if(contentLength > 0)
				{
					// Determine percent of file and add it to variable
					int percentTransferred = (int) (((float) bytesTransferred / contentLength) * 100);

					if(percentTransferred != 100)
					{
						log.info("[" + pluginName + "] Download progress: " + percentTransferred + "%");
					}
				}
			}

			is.close();
			os.flush();
			os.close();

			// Download complete!
			log.info("[" + pluginName + "] Download complete!");
			log.info("[" + pluginName + "] Update will complete on next server reload.");
			return true;
		}
		catch(MalformedURLException ex)
		{
			log.warning("[" + pluginName + "] Error accessing URL: " + ex);
		}
		catch(FileNotFoundException ex)
		{
			log.warning("[" + pluginName + "] Error accessing URL: " + ex);
		}
		catch(IOException ex)
		{
			log.warning("[" + pluginName + "] Error downloading file: " + ex);
		}
		return false;
	}

	/**
	 * Check if the latest version of this demigods on BukkitDev is newer than the currently running version.
	 * 
	 * @return True if the version on BukkitDev is newer.
	 */
	public synchronized boolean check()
	{
		try
		{
			InputStream input = this.filesFeed.openConnection().getInputStream();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);

			Node latestFile = document.getElementsByTagName("item").item(0);
			NodeList children = latestFile.getChildNodes();

			this.latestVersion = children.item(1).getTextContent().replaceAll("[a-zA-Z ]", "");
			try
			{
				this.link = children.item(3).getTextContent();
			}
			catch(Exception e)
			{
				log.warning("[" + pluginName + "] Failed to find download page.");
			}
			input.close();

			try
			{
				input = (new URL(this.link)).openConnection().getInputStream();
			}
			catch(Exception e)
			{
				log.warning("[" + pluginName + "] Failed to open connection with download page.");
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line;

			while((line = reader.readLine()) != null)
			{
				if(line.trim().startsWith("<li class=\"user-action user-action-download\">"))
				{
					this.jarLink = line.substring(line.indexOf("href=\"") + 6, line.lastIndexOf("\""));
					break;
				}
			}

			reader.close();
			input.close();

			if(this.currentVersion.equals(this.latestVersion)) return false;

			try
			{
				// Check for only new releases, must have 3 numbers (ex: 1.2.3)
				String[] current = this.currentVersion.split("\\.");
				String[] latest = this.latestVersion.split("\\.");

				// Check the amount of numbers in the current version number
				boolean current3 = current.length == 3 && Integer.parseInt(current[2]) > 0;
				boolean current4 = current.length == 4 && Integer.parseInt(current[3]) > 0;

				// All possible updates that are new
				if(latest.length == 2)
				{
					if(Integer.parseInt(current[0]) < Integer.parseInt(latest[0])) return true;
					else if(Integer.parseInt(current[0]) == Integer.parseInt(latest[0]) && Integer.parseInt(current[1]) < Integer.parseInt(latest[1]) && !current3 && !current4) return true;
				}
				else if(latest.length == 3)
				{
					if(Integer.parseInt(current[0]) < Integer.parseInt(latest[0])) return true;
					else if(Integer.parseInt(current[0]) == Integer.parseInt(latest[0]) && Integer.parseInt(current[1]) < Integer.parseInt(latest[1])) return true;
					else if(Integer.parseInt(current[1]) == Integer.parseInt(latest[1]) && Integer.parseInt(current[2]) < Integer.parseInt(latest[2]) && !current4) return true;
				}
				else if(latest.length == 4)
				{
					if(Integer.parseInt(current[0]) < Integer.parseInt(latest[0])) return true;
					else if(Integer.parseInt(current[0]) == Integer.parseInt(latest[0]) && Integer.parseInt(current[1]) < Integer.parseInt(latest[1])) return true;
					else if(Integer.parseInt(current[1]) == Integer.parseInt(latest[1]) && Integer.parseInt(current[2]) < Integer.parseInt(latest[2])) return true;
					else if(Integer.parseInt(current[2]) == Integer.parseInt(latest[2]) && Integer.parseInt(current[3]) < Integer.parseInt(latest[3])) return true;
				}
			}
			catch(Exception e)
			{
				log.warning("[" + pluginName + "] Could not parse version number.");
			}
		}
		catch(Exception e)
		{
			log.warning("[" + pluginName + "] Failed to load download page.");
		}

		return false;
	}

	/**
	 * Check to see if the currently running version of this demigods is still supported on BukkitDev.
	 * 
	 * @return True if the version exists on BukkitDev.
	 */
	private synchronized boolean versionExists()
	{
		try
		{
			InputStream input = this.filesFeed.openConnection().getInputStream();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
			NodeList fileList = document.getElementsByTagName("item");
			input.close();

			for(int i = 0; i < fileList.getLength(); i++)
			{
				Node file = fileList.item(i);
				NodeList children = file.getChildNodes();
				String fileVersion = children.item(1).getTextContent().replaceAll("[a-zA-Z ]", "");
				if(this.currentVersion.equals(fileVersion)) return true;
			}
		}
		catch(Exception e)
		{
			log.warning("[" + pluginName + "] Failed to load download page.");
		}

		return false;
	}

	/**
	 * Get the String of the current version found in the demigods.yml.
	 * 
	 * @return The latest checked version from the demigods.yml.
	 */
	public String getCurrentVersion()
	{
		return this.currentVersion;
	}

	/**
	 * Get the String of the latest version found on the BukkitDev feed.
	 * 
	 * @param check True if it should check again, or rely on the last check made.
	 * @return The latest checked version from BukkitDev.
	 */
	public String getLatestVersion(boolean check)
	{
		if(check) check();
		return this.latestVersion;
	}

	/**
	 * Get the if BukkitDev still supports the currently running version of the demigods.
	 * 
	 * @param check True if it should check again, or rely on the last check made.
	 * @return True if BukkitDev is still supporting the currently running version of the demigods.
	 */
	public boolean supported(boolean check)
	{
		if(check) this.supported = versionExists();
		return this.supported;
	}

	/**
	 * The Player Join Listener, listening only.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	private void onPlayerJoin(PlayerJoinEvent event)
	{
		// Define Variables
		final Player player = event.getPlayer();
		final String pluginName = this.pluginName;
		final boolean auto = this.auto;
		final String command = this.command;

		// Unsupported Notify
		if(!this.supported && this.notify && (player.isOp() || player.hasPermission(this.permission)))
		{
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
			{
				@Override
				public void run()
				{
					player.sendMessage(ChatColor.RED + "The version of " + ChatColor.YELLOW + pluginName + ChatColor.RED + " you are using is not supported.");
					player.sendMessage(ChatColor.RED + "It has been removed from BukkitDev. This " + ChatColor.ITALIC + "MAY" + ChatColor.RESET + ChatColor.RED + " be because it");
					if(auto) player.sendMessage(ChatColor.RED + "is not safe." + ChatColor.WHITE + " Please " + ChatColor.YELLOW + "reload the server " + ChatColor.WHITE + "to finish the auto-update.");
					else
					{
						player.sendMessage(ChatColor.RED + "is not safe. It is " + ChatColor.BOLD + "strongly" + ChatColor.RESET + ChatColor.RED + " suggested that you update soon.");
						player.sendMessage("Please update by using " + ChatColor.YELLOW + command);
					}
				}
			}, 20);
		}

		// Update Notify
		else if(this.notify && (player.isOp() || player.hasPermission(this.permission)))
		{
			if(check())
			{
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
				{
					@Override
					public void run()
					{
						player.sendMessage(ChatColor.RED + "There is a new, stable release for " + pluginName + ".");
						if(auto) player.sendMessage("Please " + ChatColor.YELLOW + "reload the server " + ChatColor.WHITE + "soon to finish an auto-update.");
						else player.sendMessage("Please update soon by using " + ChatColor.YELLOW + command);
					}
				}, 20);
			}
		}
	}

	/**
	 * The Player Command Preprocess Listener, cancelling the event if the update command is found, and executing the command inside of the listener.
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

			if(getConfirmed(player) && command.equalsIgnoreCase(this.command + " confirm"))
			{
				confirm(player, false);
				if(check() || !supported(false))
				{
					player.sendMessage(ChatColor.DARK_AQUA + "[" + pluginName + "] " + ChatColor.RESET + "Beginning download...");
					if(download()) player.sendMessage(ChatColor.DARK_AQUA + "[" + pluginName + "] " + ChatColor.RESET + "Download complete. " + ChatColor.YELLOW + "Please reload the server!");
					else player.sendMessage(ChatColor.DARK_AQUA + "[" + pluginName + "] " + ChatColor.RESET + "Download failed. " + ChatColor.WHITE + "Please try again later.");
				}
				else
				{
					player.sendMessage(ChatColor.DARK_AQUA + "[" + pluginName + "] " + ChatColor.RESET + "You are already running the latest version.");
				}
				event.setCancelled(true);
				return;
			}
			else
			{
				player.sendMessage(ChatColor.DARK_AQUA + "[" + pluginName + "] " + ChatColor.RESET + "Currently, version " + ChatColor.YELLOW + plugin.getDescription().getVersion() + ChatColor.WHITE + " is installed.");
				player.sendMessage(ChatColor.DARK_AQUA + "[" + pluginName + "] " + ChatColor.RESET + "The latest version up for download is " + ChatColor.YELLOW + getLatestVersion(false) + ChatColor.WHITE + ".");
				player.sendMessage(ChatColor.DARK_AQUA + "[" + pluginName + "] " + ChatColor.RESET + "If you would still like to update, please use ");
				player.sendMessage(ChatColor.DARK_AQUA + "[" + pluginName + "] " + ChatColor.RESET + ChatColor.YELLOW + this.command + " confirm" + ChatColor.WHITE + " to confirm.");
				confirm(player, true);
				event.setCancelled(true);
				return;
			}
		}
	}

	/**
	 * Gets the confirm data for <code>player</code>.
	 */
	private boolean getConfirmed(Player player)
	{
		if(confirmData.containsPlayer(player)) return System.currentTimeMillis() <= confirmData.getDataLong(player);
		return false;
	}

	/**
	 * Either sets the confirm data for <code>player</code> (if <code>keep</code>), or removes the confirm data altogether.
	 */
	private void confirm(Player player, boolean keep)
	{
		if(keep) confirmData.saveData(player, System.currentTimeMillis() + this.confirmTime);
		else if(confirmData.containsPlayer(player)) confirmData.removeData(player);
	}
}
