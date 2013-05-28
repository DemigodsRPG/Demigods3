package com.censoredsoftware.Demigods.Engine.PlayerCharacter;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.API.DeityAPI;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Demigods;

public class PlayerCharacterFactory
{
	public static PlayerCharacter createCharacter(final OfflinePlayer player, final String charName, final Deity deity, final int favor, final int maxFavor, final int devotion, final int ascensions, final int offense, final int defense, final int stealth, final int support, final int passive, final boolean immortal)
	{
		PlayerCharacter character = new PlayerCharacter();
		character.setPlayer(player);
		character.setName(charName);
		character.setDeity(deity);
		character.setImmortal(immortal);
		character.setHealth(20);
		character.setHunger(20);
		character.setExperience(0);
		character.setLevel(0);
		character.setLocation(player.getPlayer().getLocation());
		character.getMeta().setFavor(favor);
		character.getMeta().setMaxFavor(maxFavor);
		character.getMeta().setDevotion(devotion);
		character.getMeta().setAscensions(ascensions);
		character.getMeta().setLevel("OFFENSE", offense);
		character.getMeta().setLevel("DEFENSE", defense);
		character.getMeta().setLevel("STEALTH", stealth);
		character.getMeta().setLevel("SUPPORT", support);
		character.getMeta().setLevel("PASSIVE", passive);
		PlayerCharacter.save(character);
		return character;
	}

	public static PlayerCharacter createCharacter(OfflinePlayer player, String charName, String charDeity)
	{
		if(CharacterAPI.getCharByName(charName) == null)
		{
			// Create the Character
			return createCharacter(player, charName, DeityAPI.getDeity(charDeity), 0, 50, 0, 0, 0, 0, 0, 0, 0, true);
		}
		return null;
	}

	public static PlayerCharacterMeta createCharacterMeta()
	{
		PlayerCharacterMeta charMeta = new PlayerCharacterMeta();
		charMeta.setAscensions(Demigods.config.getSettingInt("character.default_ascensions"));
		charMeta.setDevotion(Demigods.config.getSettingInt("character.default_devotion"));
		charMeta.setFavor(Demigods.config.getSettingInt("character.default_favor"));
		charMeta.setMaxFavor(Demigods.config.getSettingInt("character.default_max_favor"));
		charMeta.initializeMaps();
		PlayerCharacterMeta.save(charMeta);
		return charMeta;
	}

	public static PlayerCharacterInventory createPlayerCharacterInventory(PlayerInventory inventory)
	{
		PlayerCharacterInventory trackedInventory = new PlayerCharacterInventory();
		if(inventory.getHelmet() != null) trackedInventory.setHelmet(inventory.getHelmet());
		if(inventory.getChestplate() != null) trackedInventory.setChestplate(inventory.getChestplate());
		if(inventory.getLeggings() != null) trackedInventory.setLeggings(inventory.getLeggings());
		if(inventory.getBoots() != null) trackedInventory.setBoots(inventory.getBoots());
		trackedInventory.setItems(inventory);
		PlayerCharacterInventory.save(trackedInventory);
		return trackedInventory;
	}

	public static PlayerCharacterInventory createEmptyCharacterInventory()
	{
		PlayerCharacterInventory charInventory = new PlayerCharacterInventory();
		charInventory.setHelmet(new ItemStack(Material.AIR));
		charInventory.setChestplate(new ItemStack(Material.AIR));
		charInventory.setLeggings(new ItemStack(Material.AIR));
		charInventory.setBoots(new ItemStack(Material.AIR));
		PlayerCharacterInventory.save(charInventory);
		return charInventory;
	}
}
