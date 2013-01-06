package com.legit2.Demigods;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;

public class DUtil
{
	public static Demigods plugin;
	
	// Define variables
	private static FileConfiguration customConfig = null;
	private static File customConfigFile = null;
	private static String plugin_name = "Demigods";
	private static Logger log = Logger.getLogger("Minecraft");
	
	public DUtil(Demigods instance)
	{
		plugin = instance;
	}

	/*
	 *  getPlugin() : Returns an instance of the plugin.
	 */
	public static Demigods getPlugin()
	{
		return plugin;
	}
	
	/*
	 *  getOnlinePlayers() : Returns a string array of all online players.
	 */
	public static Player[] getOnlinePlayers()
	{
		return Bukkit.getOnlinePlayers();
	}
	
	/*
	 *  definePlayer() : Defines the player from (CommandSender)sender.
	 */
	public static Player definePlayer(CommandSender sender)
	{
		// Define the Player
		Player player = null;
		if(sender instanceof Player) player = (Player) sender;
		
		return player;
	}
	
	/*
	 *  areAllied() : Returns true if (String)player is allied with (String)otherPlayer.
	 */
	public static boolean areAllied(String player, String otherPlayer)
	{
		String playerAlliance = getAlliance(player);
		String otherPlayerAlliance = getAlliance(otherPlayer);
		
		if(playerAlliance.equals(otherPlayerAlliance)) return true;
		else return false;
	}
	
	/*
	 *  getBindings() : Returns all bindings for (Player)player.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Material> getBindings(String username)
	{
		username = username.toLowerCase();
		
		ArrayList<Material> bindings = new ArrayList<Material>();
		
		if(DSave.hasData(username, "bindings"))
		{
			bindings = (ArrayList<Material>)DSave.getPlayerData(username, "bindings");
			return bindings;
		}
		else return null;
	}
	
	/*
	 *  setBound() : Sets (Material)material to be bound for (Player)player.
	 */
	public static boolean setBound(String username, Material material)
	{
		// Set variables
		Player player = Bukkit.getPlayer(username);
		username = username.toLowerCase();
		
		if(isBound(username, material))
		{
			player.sendMessage(ChatColor.YELLOW + "That item is already bound to a skill.");
			return false;
		}
		else if(material == Material.AIR)
		{
			player.sendMessage(ChatColor.YELLOW + "You cannot bind a skill to air.");
			return false;
		}
		else
		{			
			if(DSave.hasData(username, "bindings"))
			{
				ArrayList<Material> bindings = getBindings(username);
				
				if(!bindings.contains(material)) bindings.add(material);
				
				DSave.savePlayerData(username, "bindings", bindings);
			}
			else
			{
				ArrayList<Material> bindings = new ArrayList<Material>();
				
				bindings.add(material);
				DSave.savePlayerData(username, "bindings", bindings);
			}
			
			return true;
		}
	}
	
	/*
	 *  isBound() : Checks if (Material)material is bound for (Player)player.
	 */
	public static boolean isBound(String username, Material material)
	{
		if(DSave.hasDataEqualTo(username.toLowerCase(), "bindings", material)) return true;
		else return false;
	}
	
	/*
	 *  removeBind() : Checks if (Material)material is bound for (Player)player.
	 */
	public static boolean removeBind(String username, Material material)
	{
		username = username.toLowerCase();

		if(DSave.hasData(username, "bindings"))
		{
			ArrayList<Material> bindings = getBindings(username);
			
			if(bindings != null && bindings.contains(material)) bindings.remove(material);
			
			DSave.savePlayerData(username, "bindings", bindings);
		}
		
		return true;
	}
	
	/*
	 *  getImmortal() : Gets if the player is immortal or not.
	 */
	public static boolean isImmortal(String username)
	{
		// Set variables
		username = username.toLowerCase();
		
		if(getPlayerData(username, "immortal") == null) return false;
		else return Boolean.parseBoolean(getPlayerData(username, "immortal").toString());
	}
	
	/*
	 *  getImmortalList() : Gets if the player is immortal or not.
	 */
	public static ArrayList<String> getImmortalList()
	{		
		// Define variables
		ArrayList<String> immortalList = new ArrayList<String>();
		HashMap<String, HashMap<String, Object>> players = getAllData();
		
		for(Map.Entry<String, HashMap<String, Object>> player : players.entrySet())
		{
			String username = player.getKey().toLowerCase();
			if(getDeities(username) != null) immortalList.add(username);
		}
		
		return immortalList;
	}
	
	/*
	 *  getFavor() : Returns the (String)username's favor.
	 */
	public static int getFavor(String username)
	{
		if(getPlayerData(username, "favor") != null) return Integer.parseInt(getPlayerData(username, "favor").toString());
		else return -1;
	}
	
	/*
	 *  setFavor() : Sets the (String)username's favor to (int)amount.
	 */
	public static void setFavor(String username, int amount)
	{
		setPlayerData(username, "favor", amount);
	}
	
	/*
	 *  subtractFavor() : Subtracts (int)amount from the (String)username's favor.
	 */
	public static void subtractFavor(String username, int amount)
	{
		setFavor(username, getFavor(username) - amount);
	}
	
	/*
	 *  giveFavor() : Gives (int)amount favor to (String)username.
	 */
	public static void giveFavor(String username, int amount)
	{
		setPlayerData(username, "favor", getFavor(username) + amount);
	}

	/*
	 *  getAscensions() : Returns the (String)username's ascensions.
	 */
	public static int getAscensions(String username)
	{
		if(getPlayerData(username, "ascensions") != null) return Integer.parseInt(getPlayerData(username, "ascensions").toString());
		else return -1;
	}
	
	/*
	 *  setAscensions() : Sets the (String)username's ascensions to (int)amount.
	 */
	public static void setAscensions(String username, int amount)
	{
		setPlayerData(username, "ascensions", amount);
	}

	/*
	 *  subtractAscensions() : Subtracts (int)amount from the (String)username's ascensions.
	 */
	public static void subtractAscensions(String username, int amount)
	{
		setAscensions(username, getAscensions(username) - amount);
	}
	
	/*
	 *  giveAscensions() : Gives (int)amount ascensions to (String)username.
	 */
	public static void giveAscensions(String username, int amount)
	{
		setPlayerData(username, "ascensions", getAscensions(username) + amount);
	}
	
	/*
	 *  getDevotion() : Returns the (String)username's devotion for (String)deity.
	 */
	public static int getDevotion(String username, String deity)
	{
		if(getDeityData(username, deity, "devotion") != null) return Integer.parseInt(getDeityData(username, deity, "devotion").toString());
		return -1;
	}
	
	/*
	 *  setDevotion() : Sets the (String)username's devotion to (int)amount for (String)deity.
	 */
	public static void setDevotion(String username, String deity, int amount)
	{
		setDeityData(username, deity, "devotion", amount);
	}

	/*
	 *  subtractDevotion() : Subtracts (int)amount from the (String)username's (String)deity devotion.
	 */
	public static void subtractDevotion(String username, String deity, int amount)
	{
		setDevotion(username, deity, getDevotion(username, deity) - amount);
	}
	
	/*
	 *  giveDevotion() : Gives (int)amount devotion to (String)username for (String)deity.
	 */
	public static void giveDevotion(String username, String deity, int amount)
	{
		setDeityData(username, deity, "devotion", getDevotion(username, deity) + amount);
	}
	
	/*
	 *  getAlliance() : Returns the alliance of a player.
	 */
	public static String getAlliance(String username)
	{
		return (String)getPlayerData(username, "alliance");
	}
	
	/*
	 *  setAlliance() : Sets the (String)username's alliance to (String)alliance.
	 */
	public static void setAlliance(String username, String alliance)
	{
		setPlayerData(username, "alliance", alliance);
	}
	
	/*
	 *  getKills() : Returns (int)kills for (String)username.
	 */
	public static int getKills(String username)
	{
		if(getPlayerData(username, "kills") != null) return Integer.parseInt(getPlayerData(username, "kills").toString());
		return -1;
	}
	
	/*
	 *  setKills() : Sets the (String)username's kills to (int)amount.
	 */
	public static void setKills(String username, int amount)
	{
		setPlayerData(username, "kills", amount);
	}
	
	/*
	 *  addKill() : Gives (String)username 1 kill.
	 */
	public static void addKill(String username)
	{
		setPlayerData(username, "kills", getKills(username) + 1);
	}
	
	/*
	 *  getDeaths() : Returns (int)deaths for (String)username.
	 */
	public static int getDeaths(String username)
	{
		if(getPlayerData(username, "deaths") != null) return Integer.parseInt(getPlayerData(username, "deaths").toString());
		return -1;
	}
	
	/*
	 *  setDeaths() : Sets the (String)username's kills to (int)amount.
	 */
	public static void setDeaths(String username, int amount)
	{
		setPlayerData(username, "deaths", amount);
	}
	
	/*
	 *  addDeath() : Gives (String)username 1 death.
	 */
	public static void addDeath(String username)
	{
		setPlayerData(username, "deaths", getKills(username) + 1);
	}
	
	/*
	 *  getDeites() : Returns an ArrayList<String> of (Player)player's deities.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getDeities(String username)
	{		
		// Set variables
		username = username.toLowerCase();
		//ArrayList<String> deities = new ArrayList<String>();

		if(DSave.hasData(username, "deities"))
		{
			return (ArrayList<String>) DSave.getPlayerData(username, "deities");
		}
		else return null;
	}
	
	/*
	 *  hasDeity() : Checks if (Player)player has (String)deity.
	 */
	public static boolean hasDeity(String username, String deity)
	{
		if(getDeities(username) != null && getDeities(username).contains(deity.toLowerCase())) return true;
		else return false;
	}
	
	/*
	 *  giveDeity() : Gives (String)username the (String)deity.
	 */
	public static boolean giveDeity(String username, String deity)
	{	
		// Set variables
		username = username.toLowerCase();
		deity.toLowerCase();
			
		if(DSave.hasData(username, "deities"))
		{
			ArrayList<String> deities = getDeities(username);
			
			if(deities == null || !deities.contains(deity)) deities.add(deity.toLowerCase());
			
			setPlayerData(username, "deities", deities);
		}
		else
		{
			ArrayList<String> deities = new ArrayList<String>();
			deities.add(deity.toLowerCase());
			setPlayerData(username, "deities", (ArrayList<String>)deities);
		}
		
		return true;
	}

	/*
	 *  getPlayerData() : Returns the data with id (String)key for (Player)player.
	 */
	public static Object getPlayerData(String username, String id)
	{
		return DSave.getPlayerData(username, id);
	}
	
	/*
	 *  setPlayerData() : Sets the data with id (String)key for (Player)player.
	 */
	public static boolean setPlayerData(String username, String id, Object data)
	{
		return DSave.savePlayerData(username, id, data);
	}
	
	/*
	 *  getDeityData() : Returns the deity data with id (String)key for (Player)player.
	 */
	public static Object getDeityData(String username, String deity, String id)
	{
		return DSave.getDeityData(username.toLowerCase(), deity.toLowerCase(), id.toLowerCase());
	}
	
	/*
	 *  setDeityData() : Sets the data with id (String)key for (Player)player's (String)deity.
	 */
	public static boolean setDeityData(String username, String deity, String id, Object data)
	{
		return DSave.saveDeityData(username.toLowerCase(), deity.toLowerCase(), id.toLowerCase(), data);
	}
	
	/*
	 *  removeDeityData() : Sets the data with id (String)key for (Player)player's (String)deity.
	 */
	public static boolean removeDeityData(String username, String deity, String id)
	{
		return DSave.removeDeityData(username.toLowerCase(), deity.toLowerCase(), id.toLowerCase());
	}
	
	
	/*
	 *  getAllData() : Returns all saved HashMaps.
	 */
	public static HashMap<String, HashMap<String, Object>> getAllData()
	{
		return DSave.getAllData();
	}
	
	/*
	 *  toInteger() : Returns an object as an integer.
	 */
	public static int toInteger(Object object)
	{
		return Integer.parseInt(object.toString());
	}
	
	/*
	 *  toBoolean() : Returns an object as a boolean.
	 */
	public static boolean toBoolean(Object object)
	{
		return Boolean.parseBoolean(object.toString());
	}
	
	/*
	 *  customDamage() : Creates custom damage for (LivingEntity)target from (LivingEntity)source with ammount (int)amount.
	 */
	public static void customDamage(LivingEntity source, LivingEntity target, int amount)
	{
		if(target instanceof Player)
		{
			if(source instanceof Player)
			{
				target.setLastDamageCause(new EntityDamageByEntityEvent(source, target, DamageCause.CUSTOM, amount));
			}
			else target.damage(amount);
		}
		else target.damage(amount);
	}
	
	/*
	 *  taggedMessage() : Sends tagged message (String)msg to the (CommandSender)sender.
	 */
	public static void taggedMessage(CommandSender sender, String msg)
	{
		sender.sendMessage(ChatColor.YELLOW + "[" + plugin_name + "] " + ChatColor.RESET + msg);
	}
	
	/*
	 *  info() : Sends console message with "info" tag.
	 */
	public static void info(String msg)
	{
		log.info("[" + plugin_name + "] " + msg);
	}
	
	/*
	 *  warning() : Sends console message with "warning" tag.
	 */
	public static void warning(String msg)
	{
		log.warning("[" + plugin_name + "] " + msg);
	}
	
	/*
	 *  severe() : Sends console message with "severe" tag.
	 */
	public static void severe(String msg)
	{
		log.severe("[" + plugin_name + "] " + msg);
	}
	
	/*
	 *  serverMsg() : Send (String)msg to the server chat.
	 */
	public static void serverMsg(String msg)
	{
		plugin.getServer().broadcastMessage(msg);
	}

	/*
	 *  saveCustomConfig() : Saves the custom configuration (String)name to file system.
	 */
	public static void saveCustomConfig(String name)
	{
		if(customConfig == null || customConfigFile == null)
		{
			return;
		}

		try
		{
			getCustomConfig(name).save(customConfigFile);
		}
		catch(IOException e)
		{
			severe("There was an error when saving file: " + name + ".yml");
			severe("Error: " + e);
		}
	}

	/*
	 *  saveDefaultCustomConfig() : Saves the defaults for custom configuration (String)name to file system.
	 */
	public static void saveDefaultCustomConfig(String name)
	{
		customConfigFile = new File(plugin.getDataFolder(), name + ".yml");
		if(!customConfigFile.exists())
		{
			plugin.saveResource(name + ".yml", false);
		}
	}
	
	/*
	 *  getCustomConfig() : Grabs the custom configuration file from file system.
	 */
	public static FileConfiguration getCustomConfig(String name)
	{
		if(customConfig == null)
		{
			reloadCustomConfig(name);
		}
		return customConfig;
	}
	
	/*
	 *  reloadCustomConfig() : Reloads the custom configuration (String)name to refresh values.
	 */
	public static void reloadCustomConfig(String name)
	{
		if(customConfigFile == null)
		{
			customConfigFile = new File(plugin.getDataFolder(), name + ".yml");
		}
		customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

		// Look for defaults in the jar
		InputStream defConfigStream = plugin.getResource(name + ".yml");
		if(defConfigStream != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			customConfig.setDefaults(defConfig);
		}
	}
	
	/*
	 *  hasPermission() : Checks if (Player)player has permission (String)permission.
	 */
	public static boolean hasPermission(Player player, String permission)
	{
		if(player == null) return true;
		return player.hasPermission(permission);
	}
	
	/*
	 *  hasPermissionOrOP() : Checks if (Player)player has permission (String)permission, or is OP.
	 */
	public static boolean hasPermissionOrOP(Player player, String permission)
	{
		if(player == null) return true;
		if(player.isOp()) return true;
		return player.hasPermission(permission);
	}
	
	/*
	 *  noPermission() : Command sender does not have permission to run command.
	 */
	public static boolean noPermission(Player player)
	{
		player.sendMessage(ChatColor.RED + "You do not have permission to run this command.");
		return true;
	}
	
	/*
	 *  noConsole() : Sends a permission denial message to the console.
	 */
	public static boolean noConsole(CommandSender sender)
	{
		sender.sendMessage("This command can only be executed by a player.");
		return true;
	}
	
	/*
	 *  noPlayer() : Sends a permission denial message to the console.
	 */
	public static boolean noPlayer(CommandSender sender)
	{
		sender.sendMessage("This command can only be executed by the console.");
		return true;
	}
	
    /*
     *  WORLDGUARD SUPPORT START
     */
    @SuppressWarnings("static-access")
    public static boolean canWorldGuardPVP(Location location)
    {
	    if(DConfig.getSettingBoolean("allow_skills_anywhere")) return true;
	    if(plugin.WORLDGUARD == null) return true;
	    
	    ApplicableRegionSet set = plugin.WORLDGUARD.getRegionManager(location.getWorld()).getApplicableRegions(location);
	    return set.allows(DefaultFlag.PVP);
    }

    @SuppressWarnings("static-access")
    public static boolean canWorldGuardBuild(Player player, Location location)
    {
        if(plugin.WORLDGUARD == null) return true;
        
        return plugin.WORLDGUARD.canBuild(player, location);
    }

    @SuppressWarnings("static-access")
    public static boolean canWorldGuardDamage(Location location)
    {
        if(plugin.WORLDGUARD == null) return true;
        
        ApplicableRegionSet set = plugin.WORLDGUARD.getRegionManager(location.getWorld()).getApplicableRegions(location);
        return !set.allows(DefaultFlag.INVINCIBILITY);
    }
            
    /*
     *  WORLDGUARD SUPPORT END
     */
    @SuppressWarnings("static-access")
    public static boolean canFactionsPVP(Location location)
    {
        if(DConfig.getSettingBoolean("allow_skills_anywhere")) return true;
        
        if(plugin.FACTIONS == null) return true;
        Faction faction = Board.getFactionAt(new FLocation(location.getBlock()));
        return !(faction.isPeaceful() || faction.isSafeZone());
    }
    public static boolean canPVP(Location location)
    {
        if(DConfig.getSettingBoolean("allow_skills_anywhere")) return true;
        
        return (canWorldGuardPVP(location)&&canFactionsPVP(location));
    }
}