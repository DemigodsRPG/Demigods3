package com.censoredsoftware.demigods.engine;

import com.censoredsoftware.censoredlib.CensoredLibPlugin;
import com.censoredsoftware.censoredlib.helper.CensoredJavaPlugin;
import com.censoredsoftware.demigods.engine.data.DataManager;
import com.censoredsoftware.demigods.engine.data.ThreadManager;
import com.censoredsoftware.demigods.engine.player.DCharacter;
import com.censoredsoftware.demigods.engine.player.DPlayer;
import com.censoredsoftware.demigods.engine.util.Messages;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Class for all plugins of demigods.
 */
public class DemigodsPlugin extends CensoredJavaPlugin
{
	private static final String CENSORED_LIBRARY_VERSION = "1.0";
	static boolean READY = false;

	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		if(!checkForCensoredLib())
		{
			getPluginLoader().disablePlugin(this);
			return;
		}

		loadAddons();

		// Load the game engine.
		if(!Demigods.init())
		{
			getPluginLoader().disablePlugin(this);
			return;
		}

		// Print success!
		enableMessage();
	}

	/**
	 * The Bukkit disable method.
	 */
	@Override
	public void onDisable()
	{
		if(READY)
		{
			// Save all the data.
			DataManager.save();

			// Handle online characters
			for(DCharacter character : DCharacter.Util.getOnlineCharacters())
			{
				// Toggle prayer off and clear the session
				DPlayer.Util.togglePrayingSilent(character.getOfflinePlayer().getPlayer(), false, false);
				DPlayer.Util.clearPrayerSession(character.getOfflinePlayer().getPlayer());
			}
		}

		// Cancel all threads, event calls, and unregister permissions.
		try
		{
			ThreadManager.stopThreads();
			HandlerList.unregisterAll(this);
			Demigods.unloadPermissions();

			Messages.info("Successfully disabled.");
		}
		catch(Throwable ignored)
		{}
	}

	private void enableMessage()
	{
		getLogger().info("  ");
		getLogger().info("     ____            _           _");
		getLogger().info("    |    \\ ___ _____|_|___ ___ _| |___");
		getLogger().info("    |  |  | -_|     | | . | . | . |_ -|");
		getLogger().info("    |____/|___|_|_|_|_|_  |___|___|___|");
		getLogger().info("        Battle of the |___| Chosen");
		getLogger().info("  ");
		getLogger().info("  ");
		getLogger().info("   _   _ _____    333333333333333");
		getLogger().info("  | | | | ___ |  3:::::::::::::::33");
		getLogger().info("   \\ V /| ____|  3::::::33333::::::3");
		getLogger().info("    \\_/ |_____)  3333333     3:::::3");
		getLogger().info("             _               3:::::3");
		getLogger().info("            (_)              3:::::3");
		getLogger().info("    ____ ___ _       33333333:::::3");
		getLogger().info("   / ___)___) |      3:::::::::::3");
		getLogger().info("  | |  |___ | |      33333333:::::3");
		getLogger().info("  |_|  (___/|_|              3:::::3");
		getLogger().info("                             3:::::3");
		getLogger().info("                             3:::::3");
		getLogger().info("    ___  ____    3333333     3:::::3");
		getLogger().info("   / _ \\|  _ \\   3::::::33333::::::3 " + getDescription().getVersion().substring(1));
		getLogger().info("  | |_| | | | |  3:::::::::::::::33");
		getLogger().info("   \\___/|_| |_|   333333333333333");
		getLogger().info("  ");
		getLogger().info(" ...has enabled successfully!");
	}

	private boolean checkForCensoredLib()
	{
		// Check for CensoredLib
		Plugin check = Bukkit.getPluginManager().getPlugin("CensoredLib");
		if(check instanceof CensoredLibPlugin && check.getDescription().getVersion().startsWith(CENSORED_LIBRARY_VERSION)) return true;
		getLogger().severe("  ");
		getLogger().severe("  ");
		getLogger().severe("                  888        d8b   888");
		getLogger().severe("                  888        Y8P   888");
		getLogger().severe("                  888              888");
		getLogger().severe("        .d8888b   888        888   88888b.");
		getLogger().severe("       d88P\"      888        888   888 \"88b");
		getLogger().severe("       888        888        888   888  888");
		getLogger().severe("       Y88b.      888        888   888 d88P");
		getLogger().severe("        \"Y8888P   88888888   888   88888P\"");
		getLogger().severe("  ");
		getLogger().severe("  ");
		getLogger().severe(" CensoredLib was not found (or did not to load).");
		getLogger().severe(" Demigods cannot load without CensoredLib enabled!");
		getLogger().severe(" Please go to the BukkitDev project page to");
		getLogger().severe(" download and install CensoredLib.");
		getLogger().severe("  ");
		getLogger().severe("  ");
		getLogger().severe(" http://dev.bukkit.org/bukkit-plugins/censoredlib/");
		getLogger().severe("  ");
		getLogger().severe(" We are very sorry for this inconvenience, and we");
		getLogger().severe(" hope this is the only time you see this message.");
		getLogger().severe(" If you have any questions, please contact us on");
		getLogger().severe(" the BukkitDev page for Demigods. ");
		getLogger().severe("  ");
		getLogger().severe("  ");
		getLogger().severe(" ~~~~ _Alex & HmmmQuestionMark, CensoredSoftware");
		getLogger().severe("  ");
		getLogger().severe("  ");
		return false;
	}

	private void loadAddons()
	{
		// Unload all incorrectly installed plugins
		for(Plugin plugin : Bukkit.getPluginManager().getPlugins())
		{
			// Not soft-depend
			List<String> depends = plugin.getDescription().getDepend();
			if(depends != null && !depends.isEmpty() && depends.contains("Demigods"))
			{
				getLogger().warning(plugin.getName() + " was installed in the wrong directory.");
				getLogger().warning("Please place all Demigods addons into the");
				getLogger().warning(getDataFolder().getPath() + "\\addons\\ directory.");
				getLogger().warning("i.e. " + getDataFolder().getPath() + "\\addons\\" + plugin.getName());
				Bukkit.getPluginManager().disablePlugin(plugin);
			}
		}

		// Load Demigods plugins
		File pluginsFolder = new File(getDataFolder() + "/addons");
		if(!pluginsFolder.exists()) pluginsFolder.mkdirs();

		Collection<File> files = Collections2.filter(Sets.newHashSet(pluginsFolder.listFiles()), new Predicate<File>()
		{
			@Override
			public boolean apply(File file)
			{
				return file != null && file.getName().toLowerCase().endsWith(".jar");
			}
		});

		for(File file : files)
		{
			try
			{
				Messages.info(file.getName() + " loading.");
				Bukkit.getServer().getPluginManager().enablePlugin(Bukkit.getServer().getPluginManager().loadPlugin(file));
			}
			catch(Throwable errored)
			{
				errored.printStackTrace();
			}
		}
	}
}
