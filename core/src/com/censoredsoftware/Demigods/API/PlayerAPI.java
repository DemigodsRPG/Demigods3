package com.censoredsoftware.Demigods.API;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Objects.Character.PlayerCharacter;
import com.censoredsoftware.Objects.Character.PlayerCharacterInventory;

public class PlayerAPI
{
	/**
	 * Adds the <code>player</code> to the database/filesystem.
	 * 
	 * @param player the new player to add.
	 * @return boolean based on the success or failure of adding the player.
	 */
	public static void createNewPlayer(Player player)
	{
		Demigods.message.info("Saving new player: " + player.getName());

		// Create their HashMap data
		DemigodsData.playerData.saveData(player, "player_kills", 0);
		DemigodsData.playerData.saveData(player, "player_deaths", 0);
		DemigodsData.playerData.saveData(player, "current_char", null);
		DemigodsData.playerData.saveData(player, "previous_char", null);
	}

	/**
	 * Returns the current character of the <code>player</code>.
	 * 
	 * @param player the player to check.
	 * @return PlayerCharacter
	 */
	public static PlayerCharacter getCurrentChar(OfflinePlayer player)
	{
		try
		{
			return CharacterAPI.getChar(DemigodsData.playerData.getDataInt(player, "current_char"));
		}
		catch(Exception e)
		{
			return null;
		}
	}

	/**
	 * Returns the current alliance for <code>player</code>.
	 * 
	 * @param player the player to check.
	 * @return String
	 */
	public static String getCurrentAlliance(OfflinePlayer player)
	{
		PlayerCharacter character = getCurrentChar(player);
		if(character == null || !character.getCharacterClass().isActive()) return "Mortal";
		return character.getCharacterClass().getTeam();
	}

	/**
	 * Returns true if <code>player1</code> is allied with <code>player2</code> based
	 * on their current alliances.
	 * 
	 * @param player1 the first player to check.
	 * @param player2 the second player to check.
	 * @return boolean
	 */
	public static boolean areAllied(Player player1, Player player2)
	{
		String player1Alliance = getCurrentAlliance(player1);
		String player2Alliance = getCurrentAlliance(player2);

		return player1Alliance.equalsIgnoreCase(player2Alliance);
	}

	/**
	 * Returns a List of all of <code>player</code>'s characters.
	 * 
	 * @param player the player to check.
	 * @return List the list of all character IDs.
	 */
	public static List<PlayerCharacter> getChars(OfflinePlayer player)
	{
		List<PlayerCharacter> characters = new ArrayList<PlayerCharacter>();
		for(PlayerCharacter character : CharacterAPI.getAllChars())
		{
			if(character.getOwner() == player) characters.add(character);
		}
		return characters;
	}

	/**
	 * Changes the <code>offlinePlayer</code>'s current character to <code>charID</code>.
	 * 
	 * @param offlinePlayer the player whose character to change.
	 * @param charID the character ID to switch to.
	 * @return boolean based on if the change was successful or not.
	 */
	public boolean changeCurrentChar(OfflinePlayer offlinePlayer, int charID) // TODO Most of this should be in a listener, not here in the API.
	{
		// Define variables
		Player player = offlinePlayer.getPlayer();

		if(!getChars(offlinePlayer).contains(charID))
		{
			player.sendMessage(ChatColor.RED + "You can't do that.");
			return false;
		}

		// Disable prayer just to be safe
		togglePraying(player, false);

		// Update the current character (if it exists)
		PlayerCharacter currentChar = getCurrentChar(player);
		if(currentChar != null)
		{
			// Save info
			currentChar.setHealth(player.getHealth());
			currentChar.setFoodLevel(player.getFoodLevel());
			currentChar.setLevel(player.getLevel());
			currentChar.setExp(player.getExp());
			currentChar.setLocation(player.getLocation());
			currentChar.saveInventory();

			// Set them to inactive
			currentChar.toggleActive(false);
		}

		// Everything is good, let's switch
		DemigodsData.playerData.saveData(player, "previous_char", currentChar.getID());
		DemigodsData.playerData.saveData(player, "current_char", charID);
		PlayerCharacter character = CharacterAPI.getChar(charID);
		character.toggleActive(true);

		// If it's their first character save their inventory
		if(getChars(player).size() <= 1) character.saveInventory();

		// Update inventory
		player.getInventory().clear();
		player.getInventory().setBoots(new ItemStack(Material.AIR));
		player.getInventory().setChestplate(new ItemStack(Material.AIR));
		player.getInventory().setLeggings(new ItemStack(Material.AIR));
		player.getInventory().setBoots(new ItemStack(Material.AIR));

		if(character.getInventory() != null)
		{
			PlayerCharacterInventory charInv = character.getInventory();
			charInv.setToPlayer(player);
		}

		// Update health and experience
		player.setHealth(character.getHealth());
		player.setFoodLevel(character.getFoodLevel());
		player.setExp(character.getExp());

		// Teleport them
		player.teleport(character.getLocation());

		// Enable movement and chat to be safe
		togglePlayerChat(player, true);
		togglePlayerMovement(player, true);

		return true;
	}

	/**
	 * Returns true if the <code>player</code> is currently immortal.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public boolean isImmortal(OfflinePlayer player)
	{
		PlayerCharacter character = getCurrentChar(player);
		return character != null && character.getCharacterClass().isActive();
	}

	/**
	 * Returns true if <code>player</code> has a character with the id <code>charID</code>.
	 * 
	 * @param player the player to check.
	 * @param charID the charID to check with.
	 * @return boolean
	 */
	public boolean hasCharID(OfflinePlayer player, int charID)
	{
		return getChars(player) != null && getChars(player).contains(charID);
	}

	/**
	 * Returns true if <code>player</code> has a character with the name <code>charName</code>.
	 * 
	 * @param player the player to check.
	 * @param charName the charName to check with.
	 * @return boolean
	 */
	public boolean hasCharName(OfflinePlayer player, String charName)
	{
		List<PlayerCharacter> characters = getChars(player);

		for(PlayerCharacter character : characters)
		{
			if(character == null) continue;
			if(character.getName().equalsIgnoreCase(charName)) return true;
		}
		return false;
	}

	/**
	 * Returns true if the <code>player</code> is currently praying.
	 * 
	 * @param player the player to check.
	 * @return boolean
	 */
	public boolean isPraying(OfflinePlayer player)
	{
		return DemigodsData.tempPlayerData.getDataBool(player, "temp_praying");
	}

	/**
	 * Regenerates favor for all currently online players.
	 */
	public synchronized void regenerateAllFavor() // TODO Does this really belong here?
	{
		ArrayList<Player> onlinePlayers = getOnlinePlayers();
		for(Player player : onlinePlayers)
		{
			PlayerCharacter character = getCurrentChar(player);
			if(character == null || !character.getCharacterClass().isActive()) continue;
			int regenRate = (int) Math.ceil(Demigods.config.getSettingDouble("global_favor_multiplier") * character.getCharacterClass().getAscensions());
			if(regenRate < 1) regenRate = 1;
			character.getCharacterClass().giveFavor(regenRate);
		}
	}

	/**
	 * Returns the number of total kills for <code>player</code>.
	 * 
	 * @param player the player to check.
	 * @return int
	 */
	public int getKills(OfflinePlayer player)
	{
		return DemigodsData.playerData.getDataInt(player, "player_kills");
	}

	/**
	 * Sets the amount of kills for <code>player</code> to <code>amount</code>.
	 * 
	 * @param player the player to manipulate.
	 * @param amount the amount of kills to set to.
	 */
	public void setKills(OfflinePlayer player, int amount)
	{
		DemigodsData.playerData.saveData(player, "player_kills", amount);
	}

	/**
	 * Adds 1 kill to <code>player</code>.
	 * 
	 * @param player the player to manipulate.
	 */
	public void addKill(OfflinePlayer player)
	{
		DemigodsData.playerData.saveData(player, "player_kills", getKills(player) + 1);
	}

	/**
	 * Returns the number of deaths for <code>player</code>.
	 * 
	 * @param player the player to check.
	 * @return int
	 */
	public int getDeaths(OfflinePlayer player)
	{
		return DemigodsData.playerData.getDataInt(player, "player_deaths");
	}

	/**
	 * Sets the number of deaths for <code>player</code> to <code>amount</code>.
	 * 
	 * @param player the player to manipulate.
	 * @param amount the amount of deaths to set.
	 */
	public void setDeaths(OfflinePlayer player, int amount)
	{
		DemigodsData.playerData.saveData(player, "player_deaths", amount);
	}

	/**
	 * Adds a death to <code>player</code>.
	 * 
	 * @param player the player to manipulate.
	 */
	public void addDeath(OfflinePlayer player)
	{
		DemigodsData.playerData.saveData(player, "player_deaths", getDeaths(player) + 1);
	}

	/**
	 * Returns an ArrayList of all online admins.
	 * 
	 * @return ArrayList
	 */
	public static ArrayList<Player> getOnlineAdmins() // TODO Does this belong here?
	{
		ArrayList<Player> toReturn = new ArrayList<Player>();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(Demigods.permission.hasPermissionOrOP(player, "demigods.admin")) toReturn.add(player);
		}
		return toReturn;
	}

	/**
	 * Returns an ArrayList of all online players.
	 * 
	 * @return ArrayList
	 */
	public static ArrayList<Player> getOnlinePlayers() // TODO Is this even needed?
	{
		ArrayList<Player> toReturn = new ArrayList<Player>();
		Collections.addAll(toReturn, Bukkit.getOnlinePlayers());
		return toReturn;
	}

	/**
	 * Returns an ArrayList of all offline players.
	 * 
	 * @return ArrayList
	 */
	public static ArrayList<OfflinePlayer> getOfflinePlayers() // TODO Is this even needed?
	{
		ArrayList<OfflinePlayer> toReturn = getAllPlayers();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			toReturn.remove(player);
		}
		return toReturn;
	}

	/**
	 * Returns an ArrayList of all players, offline and online.
	 * 
	 * @return ArrayList
	 */
	public static ArrayList<OfflinePlayer> getAllPlayers()
	{
		ArrayList<OfflinePlayer> toReturn = new ArrayList<OfflinePlayer>();
		for(OfflinePlayer player : DemigodsData.playerData.listTiers())
		{
			toReturn.add(player);
		}
		return toReturn;
	}

	/**
	 * Changes prayer status for <code>player</code> to <code>option</code>.
	 * 
	 * @param player the player the manipulate.
	 * @param option the boolean to set to.
	 */
	public void togglePraying(OfflinePlayer player, boolean option)
	{
		if(option)
		{
			togglePlayerChat(player, false);
			togglePlayerMovement(player, false);
			DemigodsData.tempPlayerData.saveData(player, "temp_praying", option);
		}
		else
		{
			togglePlayerChat(player, true);
			togglePlayerMovement(player, true);
			DemigodsData.tempPlayerData.removeData(player, "temp_praying");
		}
	}

	/**
	 * Enables or disables player movement for <code>player</code> based on <code>option</code>.
	 * 
	 * @param player the player to manipulate.
	 * @param option the boolean to set to.
	 */
	public void togglePlayerMovement(OfflinePlayer player, boolean option)
	{
		if(DemigodsData.tempPlayerData.containsKey(player, "temp_player_hold") && option) DemigodsData.tempPlayerData.removeData(player, "temp_player_hold");
		else DemigodsData.tempPlayerData.saveData(player, "temp_player_hold", true);
	}

	/**
	 * Enables or disables player chat for <code>player</code> based on <code>option</code>.
	 * 
	 * @param player the player to manipulate.
	 * @param option the boolean to set to.
	 */
	public void togglePlayerChat(OfflinePlayer player, boolean option)
	{
		if(DemigodsData.tempPlayerData.containsKey(player, "temp_no_chat") && option) DemigodsData.tempPlayerData.removeData(player, "temp_no_chat");
		else DemigodsData.tempPlayerData.saveData(player, "temp_no_chat", true);
	}
}
