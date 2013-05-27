package com.censoredsoftware.Demigods.Engine.PlayerCharacter;

import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.API.DeityAPI;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Demigods;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.PlayerInventory;

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


    public static PlayerCharacterInventory createCharacterInventory(PlayerInventory inventory)
    {
        PlayerCharacterInventory charInventory = new PlayerCharacterInventory();
        charInventory.setSize(inventory.getSize());
        if(inventory.getHelmet() != null) charInventory.setHelmet(inventory.getHelmet());
        if(inventory.getChestplate() != null) charInventory.setChestplate(inventory.getChestplate());
        if(inventory.getLeggings() != null) charInventory.setLeggings(inventory.getLeggings());
        if(inventory.getBoots() != null) charInventory.setBoots(inventory.getBoots());
        charInventory.setItems(inventory);
        PlayerCharacterInventory.save(charInventory);
        return charInventory;
    }

}

