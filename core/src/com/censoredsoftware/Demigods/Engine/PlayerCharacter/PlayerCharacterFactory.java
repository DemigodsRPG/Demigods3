package com.censoredsoftware.Demigods.Engine.PlayerCharacter;

import org.bukkit.OfflinePlayer;

import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Demigods;

public class PlayerCharacterFactory
{
	public static PlayerCharacter createCharacter(OfflinePlayer player, String charName, boolean active, Deity deity, int favor, int maxFavor, int devotion, int ascensions, int offense, int defense, int stealth, int support, int passive, boolean immortal)
	{
		PlayerCharacter character = new PlayerCharacter();
		character.setPlayer(player);
		character.setName(charName);
		character.setActive(active);
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
}
