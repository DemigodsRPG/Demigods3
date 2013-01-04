package com.legit2.Demigods;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
	 *  areAllied() : Returns true if (String)player1 is allied with (String)player2.
	 */
	public static boolean areAllied(String player, String otherPlayer)
	{
		String playerAlliance = getAlliance(player);
		String otherPlayerAlliance = getAlliance(otherPlayer);
		
		if(playerAlliance.equals(otherPlayerAlliance)) return true;
		else return false;
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
				
				DSave.saveData(username, "bindings", bindings);
			}
			else
			{
				ArrayList<Material> bindings = new ArrayList<Material>();
				
				bindings.add(material);
				DSave.saveData(username, "bindings", bindings);
			}
			
			return true;
		}
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
			
			DSave.saveData(username, "bindings", bindings);
		}
		
		return true;
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
			bindings = (ArrayList<Material>)DSave.getData(username, "bindings");
			return bindings;
		}
		else return null;
	}
	
	/*
	 *  getImmortal() : Gets if the player is immortal or not.
	 */
	public static boolean isImmortal(String username)
	{
		// Set variables
		username = username.toLowerCase();
		
		if(getData(username, "immortal") == null) return false;
		else return (Boolean)getData(username, "immortal");
	}
	
	/*
	 *  setFavor() : Sets the (String)username's favor to (int)amount.
	 */
	public static void setFavor(String username, int amount)
	{
		setData(username, "favor", amount);
	}
	
	/*
	 *  getFavor() : Returns the (String)username's favor.
	 */
	public static int getFavor(String username)
	{
		if(getData(username, "favor") != null) return Integer.parseInt(getData(username, "favor").toString());
		else return -1;
	}
	
	/*
	 *  setAscensions() : Sets the (String)username's ascensions to (int)amount.
	 */
	public static void setAscensions(String username, int amount)
	{
		setData(username, "ascensions", amount);
	}
	
	/*
	 *  getAscensions() : Returns the (String)username's ascensions.
	 */
	public static int getAscensions(String username)
	{
		return Integer.parseInt(getData(username, "ascensions").toString());
	}
	
	/*
	 *  setDevotion() : Sets the (String)username's devotions to (int)amount.
	 */
	public static void setDevotion(String username, String deity, int amount)
	{
		setDeityData(username, deity, "devotion", amount);
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
	 *  getAlliance() : Returns the alliance of a player.
	 */
	public static String getAlliance(String username)
	{
		return (String)getData(username, "alliance");
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
			
			setData(username, "deities", deities);
		}
		else
		{
			ArrayList<String> deities = new ArrayList<String>();
			deities.add(deity.toLowerCase());
			setData(username, "deities", deities);
		}
		
		return true;
	}
	
	/*
	 *  getDeites() : Returns an ArrayList<String> of (Player)player's deities.
	 */
	public static ArrayList<String> getDeities(String username)
	{		
		// Set variables
		username = username.toLowerCase();
		
		if(DSave.hasData(username, "deities"))
		{
			ArrayList<String> deities = new ArrayList<String>(Arrays.asList(((String) DSave.getData(username, "deities")).split(",")));
			return deities;
		}
		else return null;
	}

	/*
	 *  getData() : Returns the data with id (String)key for (Player)player.
	 */
	public static Object getData(String username, String id)
	{
		return DSave.getData(username, id);
	}
	
	/*
	 *  setData() : Sets the data with id (String)key for (Player)player.
	 */
	public static boolean setData(String username, String id, Object data)
	{
		return DSave.saveData(username, id, data);
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