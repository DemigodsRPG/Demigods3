package com.demigodsrpg.demigods.engine;

import com.censoredsoftware.censoredlib.CensoredLibPlugin;
import com.censoredsoftware.censoredlib.helper.CensoredJavaPlugin;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
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
	static DemigodsPlugin INST;
	static boolean ready = false;

	public static CensoredJavaPlugin getInst()
	{
		return INST;
	}

	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		INST = this;

		if(!checkForCensoredLib())
		{
			getPluginLoader().disablePlugin(this);
			return;
		}

		loadAddons();

		// Load the game engine.
		if(!Demigods.getServer().init())
		{
			getPluginLoader().disablePlugin(this);
			return;
		}
		else ready = true;

		// Print success!
		message("enabled");
	}

	/**
	 * The Bukkit disable method.
	 */
	@Override
	public void onDisable()
	{
		Demigods.getServer().uninit();

		message("disabled");
	}

	public static boolean getReady()
	{
		return ready;
	}

	private void message(String status)
	{
		getLogger().info("     ____            _           _");
		getLogger().info("    |    \\ ___ _____|_|___ ___ _| |___");
		getLogger().info("    |  |  | -_|     | | . | . | . |_ -|");
		getLogger().info("    |____/|___|_|_|_|_|_  |___|___|___|");
		getLogger().info("        Battle of the |___| Chosen");
		getLogger().info("  ");
		getLogger().info(" ...version " + getDescription().getVersion() + " has " + status + " successfully!");
	}

	private boolean checkForCensoredLib()
	{
		// Check for CensoredLib
		boolean messageOption = true;
		Plugin check = Bukkit.getPluginManager().getPlugin("CensoredLib");
		if(check instanceof CensoredLibPlugin)
		{
			if(check.getDescription().getVersion().startsWith(CENSORED_LIBRARY_VERSION)) return true;
			messageOption = false;
		}
		getLogger().severe("  ");
		getLogger().severe("                  888        d8b   888");
		getLogger().severe("                  888              888");
		getLogger().severe("        .d8888b   888        888   88888b.");
		getLogger().severe("       d88P\"      888        888   888 \"88b");
		getLogger().severe("       Y88b.      888        888   888 d88P");
		getLogger().severe("        \"Y8888P   88888888   888   88888P\"");
		getLogger().severe("  ");
		getLogger().severe(messageOption ? " CensoredLib was not found (or did not to load)." : " The correct version of CensoredLib is missing.");
		getLogger().severe(" Demigods cannot load without CensoredLib enabled!");
		getLogger().severe(" Please go to the BukkitDev project page to");
		getLogger().severe(" download and install CensoredLib.");
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
		return false;
	}

	// FIXME Integrate zml2008's libcomponents (https://github.com/zml2008/libcomponents/tree/bukkit-pure) instead of this messy loading
	private void loadAddons()
	{
		// Unload all incorrectly installed plugins
		for(Plugin plugin : Bukkit.getPluginManager().getPlugins())
		{
			// Not soft-depend
			List<String> depends = plugin.getDescription().getDepend();
			if(depends != null && !depends.isEmpty() && depends.contains("Demigods"))
			{
				getLogger().warning(plugin.getName() + " was put in the wrong directory.");
				getLogger().warning("Please place Demigods addons in the");
				getLogger().warning(getDataFolder().getPath() + "\\addons\\ directory");
				getLogger().warning("(i.e. " + getDataFolder().getPath() + "\\addons\\" + plugin.getName() + ").");
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
				getLogger().info(file.getName() + " loading.");
				Bukkit.getServer().getPluginManager().enablePlugin(Bukkit.getServer().getPluginManager().loadPlugin(file));
			}
			catch(Exception errored)
			{
				getLogger().warning(errored.getMessage());
			}
		}
	}
}
