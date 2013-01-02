package com.legit2.Demigods;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.clashnia.ClashniaUpdate.DemigodsUpdate;
import com.legit2.Demigods.Deities.Gods.Zeus;
import com.legit2.Demigods.Listeners.DPlayerListener;
import com.massivecraft.factions.P;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Demigods extends JavaPlugin
{
	// Soft dependencies
	protected static WorldGuardPlugin WORLDGUARD = null;
	protected static P FACTIONS = null;
	
	@Override
	public void onEnable()
	{
		// Initialize Configuration
		new DUtil(this);
		
		DDatabase.initializeDatabase();
		DConfig.initializeConfig();
		DScheduler.startThreads();
		loadListeners();			
		loadCommands();		
		loadMetrics();
		loadDependencies();
		
		DSave.saveData("_Alex", "favor", 99999);
		DSave.saveData("_Alex", "ascensions", 99999);
		DSave.saveData("_Alex", "immortal", true);
		DSave.saveDeityData("_Alex", "zeus", "devotion", 99999);		
		
		checkUpdate();
		
		DUtil.info("Enabled!");
		//DUtil.serverMsg(ChatColor.GRAY + "This server is running Demigods version: " + DUtil.getPlugin().getDescription().getVersion());
	}

	@Override
	public void onDisable()
	{
		// Uninitialize Plugin
		DDatabase.uninitializeDatabase();
		//DScheduler.stopThreads();
		
		DUtil.info("Disabled!");
	}
	
	/*
	 *  loadCommands() : Loads all plugin commands and sets their executors.
	 */
	private void loadCommands()
	{
		// Define Main CommandExecutor
		ReflectCommand commandRegistrator = new ReflectCommand(this);
		commandRegistrator.register(DCommandExecutor.class);
		commandRegistrator.register(Zeus.class);
	}
	
	/*
	 *  loadListeners() : Loads all plugin listeners.
	 */
	private void loadListeners()
	{		
		// Start Listeners
		
		/* Player Listener */
		getServer().getPluginManager().registerEvents(new DPlayerListener(this), this);
		
		getServer().getPluginManager().registerEvents(new Zeus(), this);
	}
	
	/*
	 *  loadDeities() : Loads the metrics.
	 */
	private void loadMetrics()
	{
		new DMetrics(this);
		DMetrics.allianceStatsPastWeek();
		DMetrics.allianceStatsAllTime();
	}
	
	/*
	 *  loadDependencies() : Loads all dependencies.
	 */
	public void loadDependencies()
	{
		// Check for the WorldGuard plugin
		Plugin pg = getServer().getPluginManager().getPlugin("WorldGuard");
		if ((pg != null) && (pg instanceof WorldGuardPlugin))
		{
			WORLDGUARD = (WorldGuardPlugin)pg;
			if (!DConfig.getSettingBoolean("allow_skills_everywhere")) DUtil.info("WorldGuard detected. Skills are disabled in no-PvP zones.");
		}

		// Check for the Factions plugin
		pg = getServer().getPluginManager().getPlugin("Factions");
		if (pg != null)
		{
			FACTIONS = ((P)pg);
			if(!DConfig.getSettingBoolean("allow_skills_everywhere")) DUtil.info("Factions detected. Skills are disabled in peaceful zones.");
		}

		// Check to see if a player has the SimpleNotice client mod installed
		getServer().getMessenger().registerOutgoingPluginChannel(this, "SimpleNotice");
	}
	
	private void checkUpdate()
	{
		// Check for updates, and then update if need be		
		new DemigodsUpdate(this);
		Boolean shouldUpdate = DemigodsUpdate.shouldUpdate();
		if(shouldUpdate && DConfig.getSettingBoolean("auto_update"))
		{
			DemigodsUpdate.demigodsUpdate();
		}
	}
}