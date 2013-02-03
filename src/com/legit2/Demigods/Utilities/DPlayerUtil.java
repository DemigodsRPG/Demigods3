package com.legit2.Demigods.Utilities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.legit2.Demigods.Database.DDatabase;

public class DPlayerUtil
{
	/*
	 *  createPlayer() : Adds (Player)player to the database.
	 */
	public static boolean createNewPlayer(Player player)
	{
		// First check to see if the player exists
		if(!DDataUtil.newPlayer(player))
		{
			DMiscUtil.info(player.getName() + " already exists in the database.");
			return false;
		}
		
		DMiscUtil.info("Adding new player (" + player.getName() + ") to the database.");
		
		// Define variables
		int playerID = DObjUtil.generateInt(5);

		// Create their HashMap data
		DDataUtil.addPlayer(player, playerID);
		DDataUtil.savePlayerData(player, "player_id", playerID);
		DDataUtil.savePlayerData(player, "player_kills", 0);
		DDataUtil.savePlayerData(player, "player_deaths", 0);
		DDataUtil.savePlayerData(player, "player_characters", null);
		
		// Add them to the database
		try
		{
			DDatabase.addPlayerToDB(player);
		}
		catch(SQLException e)
		{
			player.kickPlayer("Please contact an admin and give them this error code: " + ChatColor.RED + "2001");
		}
		
		return true;
	}
	
	/*
	 *  getPlayerID() : Returns the ID of (Player)player.
	 */
	public static int getPlayerID(OfflinePlayer player)
	{
		return DObjUtil.toInteger(DDataUtil.getPlayerData(player, "player_id"));
	}
	
	/*
	 *  getPlayerFromID() : Returns the (Player)player for (int)player_id.
	 */
	public static OfflinePlayer getPlayerFromID(int playerID)
	{
		for(Entry<String, HashMap<String, Object>> player : DDataUtil.getAllPlayers().entrySet())
		{
			if(player.getValue().get("player_id").equals(playerID)) return Bukkit.getOfflinePlayer(player.getKey());
		}
		return null;
	}
	
	/*
	 *  definePlayer() : Defines a Player from sender.
	 */
	public static OfflinePlayer definePlayer(String name)
	{
		return Bukkit.getOfflinePlayer(name);
	}
	
	/*
	 *  getCurrentChar() : Returns the current charID for (Player)player.
	 */
	public static int getCurrentChar(OfflinePlayer player)
	{
		try
		{
			return DObjUtil.toInteger(DDataUtil.getPlayerData(player, "current_char").toString());
		}
		catch (Exception e)
		{
			return -1;
		}
	}
	
	/*
	 *  getCurrentAlliance() : Returns the current alliance for (Player)player.
	 */
	public static String getCurrentAlliance(OfflinePlayer player)
	{
		return DDataUtil.getCharData(getCurrentChar(player), "char_alliance").toString();
	}
	
	/*
	 *  getChars() : Returns an ArrayList of (Player)player's characters.
	 */
	public static ArrayList<String> getChars(OfflinePlayer player)
	{
		if(DDataUtil.getPlayerData(player, "player_characters") != null) 
		{
			String playerChars = (String) DDataUtil.getPlayerData(player, "player_characters");
			ArrayList<String> chars = new ArrayList<String>(Arrays.asList(playerChars.split(",")));
			
			return chars;
		}
		else return null;
	}
	
	/*
	 *  hasCharID() : Checks to see if (OfflinePlayer)player has (int)charID.
	 */
	public static boolean hasCharID(OfflinePlayer player, int charID)
	{
		if(getChars(player) != null && getChars(player).contains(charID)) return true;
		return false;
	}
	
	/*
	 *  hasCharName() : Checks to see if (OfflinePlayer)player has (String)charName.
	 */
	public static boolean hasCharName(OfflinePlayer player, String charName)
	{
		if(DCharUtil.getID(charName) != -1) return true;
		return false;
	}
	
	/*
	 *  togglePraying() : Toggles prayer status for player.
	 */
	public static void togglePraying(OfflinePlayer player, boolean option)
	{
		if(!option)	DDataUtil.removePlayerData(player, "temp_praying");
		else DDataUtil.savePlayerData(player, "temp_praying", option);
	}
	
	/*
	 *  isPraying() : Returns a boolean for if the player is currently praying.
	 */
	public static boolean isPraying(OfflinePlayer player)
	{
		if(DDataUtil.getPlayerData(player, "temp_praying") == null) return false;
		else return DObjUtil.toBoolean(DDataUtil.getPlayerData(player, "temp_praying"));
	}
	
	/*
     *  regenerateAllFavor() : Regenerates favor for every player based on their stats.
     */
	public static void regenerateAllFavor()
	{
		ArrayList<Player> onlinePlayers = getOnlinePlayers();
		
		for(Player player : onlinePlayers)
		{
			int charID = DPlayerUtil.getCurrentChar(player);
			int regenRate = (int) Math.ceil(DConfigUtil.getSettingDouble("global_favor_multiplier") * DCharUtil.getAscensions(charID));
			if (regenRate < 1) regenRate = 1;
			DCharUtil.giveFavor(charID, regenRate);
		}
	}
	
	/*
	 *  getNumberOfSouls() : Returns the number of souls (Player)player has in their inventory.
	 *
	public static int getNumberOfSouls(OfflinePlayer player)
	{
		// Define inventory contents & other variables
		ItemStack[] inventory = player.getInventory().getContents();
		ArrayList<ItemStack> allSouls = DSouls.returnAllSouls();
		int numberOfSouls = 0;
		
		for(ItemStack soul : allSouls)
		{
			for(ItemStack inventoryItem : inventory)
			{
				if(inventoryItem != null && inventoryItem.isSimilar(soul))
				{
					// Find amount of souls and subtract 1 upon use
					int amount = inventoryItem.getAmount();
					
					numberOfSouls = numberOfSouls + amount;
				}
			}
		}
		return numberOfSouls;
	}

	
	/*
	 *  useSoul() : Uses first soul found in (Player)player's inventory.
	 *
	public static ItemStack useSoul(OfflinePlayer player)
	{	
		if(getNumberOfSouls(player) == 0) return null;
		// Define inventory contents
		ItemStack[] inventory = player.getInventory().getContents();
		ArrayList<ItemStack> allSouls = DSouls.returnAllSouls();
		
		for(ItemStack soul : allSouls)
		{
			for(ItemStack inventoryItem : inventory)
			{
				if(inventoryItem != null && inventoryItem.isSimilar(soul))
				{
					// Find amount of souls and subtract 1 upon use
					int amount = inventoryItem.getAmount();
					player.getInventory().removeItem(inventoryItem);
					inventoryItem.setAmount(amount - 1);
					player.getInventory().addItem(inventoryItem);
					
					return inventoryItem;
				}
			}
		}
		return null;
	}
	*/
	
	/*
	 *  getKills() : Returns (int)kills for (Player)player.
	 */
	public static int getKills(OfflinePlayer player)
	{
		if(DDataUtil.getPlayerData(player, "player_kills") != null) return Integer.parseInt(DDataUtil.getPlayerData(player, "player_kills").toString());
		return -1;
	}
	
	/*
	 *  setKills() : Sets the (Player)player's kills to (int)amount.
	 */
	public static void setKills(OfflinePlayer player, int amount)
	{
		DDataUtil.savePlayerData(player, "player_kills", amount);
	}
	
	/*
	 *  addKill() : Gives (Player)player 1 kill.
	 */
	public static void addKill(OfflinePlayer player)
	{
		DDataUtil.savePlayerData(player, "player_kills", getKills(player) + 1);
	}
	
	/*
	 *  getDeaths() : Returns (int)deaths for (Player)player.
	 */
	public static int getDeaths(OfflinePlayer player)
	{
		if(DDataUtil.getPlayerData(player, "player_deaths") != null) return Integer.parseInt(DDataUtil.getPlayerData(player, "player_deaths").toString());
		return -1;
	}
	
	/*
	 *  setDeaths() : Sets the (Player)player's deaths to (int)amount.
	 */
	public static void setDeaths(OfflinePlayer player, int amount)
	{
		DDataUtil.savePlayerData(player, "player_deaths", amount);
	}
	
	/*
	 *  addDeath() : Gives (Player)player 1 death.
	 */
	public static void addDeath(OfflinePlayer player)
	{
		DDataUtil.savePlayerData(player, "player_deaths", getDeaths(player) + 1);
	}
	
	/*
	 *  getOnlinePlayers() : Returns a string array of all online players.
	 */
	public static ArrayList<Player> getOnlinePlayers()
	{
		ArrayList<Player> toReturn = new ArrayList<Player>();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			toReturn.add(player);
		}
		return toReturn;
	}
	
	/*
	 *  getOfflinePlayers() : Returns a string array of all offline players.
	 */
	public static ArrayList<OfflinePlayer> getOfflinePlayers()
	{
		ArrayList<OfflinePlayer> toReturn = getAllPlayers();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			toReturn.remove((OfflinePlayer) player);
		}
		return toReturn;
	}
	
	/*
	 *  getAllPlayers() : Returns a string array of all offline players.
	 */
	public static ArrayList<OfflinePlayer> getAllPlayers()
	{
		ArrayList<OfflinePlayer> toReturn = new ArrayList<OfflinePlayer>();
		for(String playerName : DDataUtil.getAllPlayers().keySet())
		{
			toReturn.add(Bukkit.getServer().getOfflinePlayer(playerName));
		}
		return toReturn;
	}
}
