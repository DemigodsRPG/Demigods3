package com.legit2.Demigods;

import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.legit2.Demigods.Libraries.ReflectCommand;
import com.legit2.Demigods.Listeners.DPlayerListener;
import com.massivecraft.factions.P;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Demigods extends JavaPlugin
{
	// Soft dependencies
	protected static WorldGuardPlugin WORLDGUARD = null;
	protected static P FACTIONS = null;
	public ReflectCommand commandRegistrator;
	
	// Did dependencies load correctly?
	ArrayList<String> hardDependenciesLoaded = new ArrayList<String>();
	boolean okayToLoad = false;
	
	@Override
	public void onEnable()
	{
		// Initialize Configuration
		new DUtil(this);
		
		loadDependencies();
		
		if(okayToLoad)
		{
			DDatabase.initializeDatabase();
			DConfig.initializeConfig();
			DScheduler.startThreads();
			loadListeners();
			loadCommands();
			loadDeities();
			loadMetrics();
			
			DSave.saveData("HmmmQuestionMark", "favor", 99999);
			DSave.saveData("HmmmQuestionMark", "ascensions", 99999);
			DSave.saveData("HmmmQuestionMark", "immortal", true);
			DSave.saveData("HmmmQuestionMark", "alliance", "test");
			DSave.saveData("HmmmQuestionMark", "deities", "template");
			DSave.saveDeityData("HmmmQuestionMark", "template", "devotion", 99999);
			
			checkUpdate();
			
			DUtil.info("Enabled!");
		}
		else
		{
			DUtil.severe("Demigods cannot enable correctly because at least one required dependency was not found.");
			getPluginLoader().disablePlugin(getServer().getPluginManager().getPlugin("Demigods"));
		}		
	}

	@Override
	public void onDisable()
	{
		if(okayToLoad)
		{
			// Uninitialize Plugin
			DDatabase.uninitializeDatabase();
			DScheduler.stopThreads();
			
			DUtil.info("Disabled!");
		}		
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
		CodeSource demigodsSrc = Demigods.class.getProtectionDomain().getCodeSource();
		if(demigodsSrc != null)
		{
			try
			{
				URL demigodsJar = demigodsSrc.getLocation();
				ZipInputStream demigodsZip = new ZipInputStream(demigodsJar.openStream());
				
				ZipEntry demigodsFile = null;
				
				// Define variables
				int deityCount = 0;
				long startTimer = System.currentTimeMillis();
				
				while((demigodsFile = demigodsZip.getNextEntry()) != null)
				{
					String deityName = demigodsFile.getName().replace("/", ".").replace(".class", "");
					if(deityName.contains("_deity"))
					{
						deityCount++;
						deityList.add(deityName);
					}
				}
				
				for(String deity : deityList)
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
				// Stop the timer
				long stopTimer = System.currentTimeMillis();
				double totalTime = (double) (stopTimer - startTimer);

				DUtil.info(deityCount + " deities loaded in " + totalTime/1000 + " seconds.");
			}
			catch(Exception e)
			{
				DUtil.severe("There was a problem while loading deities!");
				e.printStackTrace();
			}
		}
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
		// Check for the SQLibrary plugin (needed)
		Plugin pg = getServer().getPluginManager().getPlugin("SQLibrary");
		if (pg == null)	DUtil.severe("SQLibrary plugin (required) not found!");
		else if (pg != null) hardDependenciesLoaded.add("SQLibrary");		
		
		// Check for the WorldGuard plugin (optional)
		pg = getServer().getPluginManager().getPlugin("WorldGuard");
		if ((pg != null) && (pg instanceof WorldGuardPlugin))
		{
			WORLDGUARD = (WorldGuardPlugin)pg;
			if (!DConfig.getSettingBoolean("allow_skills_everywhere")) DUtil.info("WorldGuard detected. Skills are disabled in no-PvP zones.");
		}

		// Check for the Factions plugin (optional)
		pg = getServer().getPluginManager().getPlugin("Factions");
		if (pg != null)
		{
			FACTIONS = ((P)pg);
			if(!DConfig.getSettingBoolean("allow_skills_everywhere")) DUtil.info("Factions detected. Skills are disabled in peaceful zones.");
		}

		// Check to see if a player has the SimpleNotice client mod installed
		getServer().getMessenger().registerOutgoingPluginChannel(this, "SimpleNotice");
		
		// If all the dependencies are loaded, boolean to true
		if(hardDependenciesLoaded.contains("SQLibrary")) okayToLoad = true;
	}
	
	private void checkUpdate()
	{
		// Check for updates, and then update if need be		
		new DUpdate(this);
		Boolean shouldUpdate = DUpdate.shouldUpdate();
		if(shouldUpdate && DConfig.getSettingBoolean("auto_update"))
		{
			DUpdate.demigodsUpdate();
		}
	}
}