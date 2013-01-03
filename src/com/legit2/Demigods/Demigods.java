package com.legit2.Demigods;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.clashnia.ClashniaUpdate.DemigodsUpdate;
import com.legit2.Demigods.Listeners.DPlayerListener;
import com.massivecraft.factions.P;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Demigods extends JavaPlugin
{
	// Soft dependencies
	protected static WorldGuardPlugin WORLDGUARD = null;
	protected static P FACTIONS = null;
	public ReflectCommand commandRegistrator;
	
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
		loadDeities();
		loadMetrics();
		loadDependencies();
		
		DSave.saveData("_Alex", "favor", 99999);
		DSave.saveData("_Alex", "ascensions", 99999);
		DSave.saveData("_Alex", "immortal", true);
		DSave.saveDeityData("_Alex", "zeus", "devotion", 99999);		
		
		checkUpdate();
		
		DUtil.info("Enabled!");
	}

	@Override
	public void onDisable()
	{
		// Uninitialize Plugin
		DDatabase.uninitializeDatabase();
		DScheduler.stopThreads();
		
		DUtil.info("Disabled!");
	}
	
	/*
	 *  loadCommands() : Loads all plugin commands and sets their executors.
	 */
	private void loadCommands()
	{
		// Define Main CommandExecutor
		commandRegistrator = new ReflectCommand(this);
		commandRegistrator.register(DCommandExecutor.class);
	}
	
	/*
	 *  loadListeners() : Loads all plugin listeners.
	 */
	private void loadListeners()
	{		
		/* Player Listener */
		getServer().getPluginManager().registerEvents(new DPlayerListener(this), this);
	}
	
	/*
	 *  loadDeities() : Loads the deities.
	 */
	@SuppressWarnings("rawtypes")
	public void loadDeities()
	{
		DUtil.info("Loading deities...");
		ArrayList<String> deityList = new ArrayList<String>();
		
		// Find all deities
		deityList.add("Deities.Gods.Zeus");
		
		for(String deity : deityList)
		{
			 try  
			 {  
				// No Paramaters
				Class noparams[] = {};
				
				Object obj = Class.forName(deity, true, this.getClass().getClassLoader()).newInstance();
				 
				// Load Deity commands
				commandRegistrator.register(Class.forName(deity, true, this.getClass().getClassLoader()));
				 
				// Load everything else for the Deity (Listener, etc.)
				Method loadDeity = Class.forName(deity, true, this.getClass().getClassLoader()).getMethod("loadDeity", noparams);
				String deityMessage = (String)loadDeity.invoke(obj, (Object[])null);
				 
				// Display the success message
				DUtil.info(deityMessage);
			 }
			 catch(Exception e)
			 {
				 DUtil.severe("Something went wrong while loading Deities!");
				 e.printStackTrace();
			 }
		}
		DUtil.info("Deities loaded!");
	}
	
	/*
	 *  loadMetrics() : Loads the metrics.
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