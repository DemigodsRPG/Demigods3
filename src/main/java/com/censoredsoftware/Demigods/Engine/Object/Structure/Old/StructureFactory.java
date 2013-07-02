package com.censoredsoftware.Demigods.Engine.Object.Structure.Old;

import com.censoredsoftware.Demigods.Engine.Object.PlayerCharacter.PlayerCharacter;
import org.bukkit.Location;

public class StructureFactory
{

	// TODO New structures.

	public static Altar createAltar(Location location)
	{
		Altar altar = new Altar();
		altar.setActive(true);
		altar.setCenter(location);
		Altar.save(altar);
		Altar.generate(altar, location);
		return altar;
	}

	public static Shrine createShrine(PlayerCharacter character, Location location)
	{
		Shrine shrine = new Shrine();
		shrine.setActive(true);
		shrine.setCenter(location);
		shrine.setOwner(character);
		shrine.setDeity(character.getDeity());
		Shrine.save(shrine);
		Shrine.generate(shrine, location);
		return shrine;
	}
}
