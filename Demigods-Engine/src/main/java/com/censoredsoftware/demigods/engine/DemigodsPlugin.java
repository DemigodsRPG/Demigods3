package com.censoredsoftware.demigods.engine;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

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
		if(!checkForCensoredLib()) return;

		loadAddons();

		// Load the game engine.
		if(!Demigods.load())
		{
			getPluginLoader().disablePlugin(this);
			return;
		}

		// Print success!
		Messages.info("Successfully enabled.");
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
		}
		catch(Throwable ignored)
		{}

		Messages.info("Successfully disabled.");
	}

	private boolean checkForCensoredLib()
	{
		// Check for CensoredLib
		Plugin check = Bukkit.getPluginManager().getPlugin("CensoredLib");
		if(check instanceof CensoredLibPlugin && check.getDescription().getVersion().startsWith(CENSORED_LIBRARY_VERSION)) return true;
		// TODO Auto-download/update.
		getLogger().severe("Demigods cannot load without CensoredLib installed.");
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
				getLogger().warning(plugin.getName() + " v" + plugin.getDescription().getVersion() + " was installed in the wrong directory.");
				getLogger().warning("Please put all addons for Demigods in the correct folder.");
				getLogger().warning("Like this: " + getDataFolder().getPath() + "/addons/" + plugin.getName() + ".jar");
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