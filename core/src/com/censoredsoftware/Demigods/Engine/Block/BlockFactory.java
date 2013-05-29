package com.censoredsoftware.Demigods.Engine.Block;

import org.bukkit.Location;

import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

public class BlockFactory
{
	public static Altar createAltar(Location location)
	{
		Altar altar = new Altar();
		altar.setActive(true);
		altar.setCenter(location);
		Altar.generateNewBlocks(altar, location);
		Altar.save(altar);
		return altar;
	}

	public static Shrine createShrine(PlayerCharacter character, Location location)
	{
		Shrine shrine = new Shrine();
		shrine.setActive(true);
		shrine.setLocation(location);
		shrine.setOwner(character);
		shrine.setDeity(character.getDeity());
		shrine.generate();
		Shrine.save(shrine);
		return shrine;
	}
}
