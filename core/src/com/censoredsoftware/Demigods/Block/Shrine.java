package com.censoredsoftware.Demigods.Block;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacterClass;
import com.censoredsoftware.Demigods.Tracked.TrackedBlock;
import com.censoredsoftware.Demigods.Tracked.TrackedLocation;

public class Shrine
{
	private int id, owner;
	private String deity;
	private TrackedBlock block;
	private TrackedLocation location;

	public Shrine(int id, PlayerCharacterClass character, Location location)
	{
		this.id = id;
		this.location = new TrackedLocation(location, null);
		this.owner = character.getID();
		this.deity = character.getClassName();

		// Generate the Shrine
		generate();

		save();
	}

	/*
	 * save() : Saves the Shrine to a HashMap.
	 */
	private void save() // TODO This won't work with saving to a file, will probably have to convert this over like I did the PlayerCharacter object.
	{
		DemigodsData.shrineData.saveData(this.id, this);
	}

	/*
	 * remove() : Removes the Shrine.
	 */
	public synchronized void remove()
	{
		DemigodsData.shrineData.removeData(this.id);

		Location location = this.location.toLocation();
		location.getBlock().setType(Material.AIR);

		Location locToMatch = location.add(0.5, 1.0, 0.5);
		for(Entity entity : location.getWorld().getEntities())
		{
			if(entity.getLocation().equals(locToMatch))
			{
				entity.remove();
			}
		}
	}

	/*
	 * getID() : Returns the ID for the Shrine.
	 */
	public int getID()
	{
		return this.id;
	}

	/*
	 * getOwner() : Returns the owner ID for the Shrine.
	 */
	public PlayerCharacterClass getOwner()
	{
		return CharacterAPI.getChar(this.owner);
	}

	/*
	 * getDeity() : Returns the deity for the Shrine.
	 */
	public String getDeity()
	{
		return this.deity;
	}

	/*
	 * getLocation() : Returns the location of this Shrine.
	 */
	public Location getLocation()
	{
		return this.location.toLocation();
	}

	/**
	 * Generates the physical Shrine structure.
	 */
	public synchronized void generate()
	{
		Location location = this.getLocation();

		// Remove entity to be safe
		Location locToMatch = this.getLocation().add(0.5, 1.0, 0.5);
		for(Entity entity : location.getWorld().getEntities())
		{
			if(entity.getLocation().equals(locToMatch))
			{
				entity.remove();
			}
		}

		// Set bedrock
		this.block = new TrackedBlock(location, "shrine", Material.BEDROCK);

		// Spawn the Entity
		location.getWorld().spawnEntity(location.add(0.5, 0.0, 0.5), EntityType.ENDER_CRYSTAL);
	}

	@Override
	public boolean equals(Object object)
	{
		return !(object == null || !(object instanceof Shrine)) && this.id == parse(object).getID();
	}

	@Override
	public String toString()
	{
		return "Shrine{id=" + this.id + ",owner=" + this.owner + ",location=" + location.toLocation().getWorld().getName() + "," + location.toLocation().getX() + "," + location.toLocation().getY() + "," + location.toLocation().getZ() + "}";
	}

	/**
	 * Parses the save object into a new Shrine object and returns it.
	 * 
	 * @param object the save to parse.
	 * @return Shrine
	 */
	public static Shrine parse(Object object)
	{
		if(object instanceof Shrine) return (Shrine) object;
		else if(object instanceof String)
		{
			// Cast the object into a string
			String string = (String) object;

			// Validate that it's a Shrine save
			if(!string.startsWith("Shrine{id=")) return null;

			// Begin splitting the string into the different variables to parse with
			string = string.substring(9).replace("}", "");
			String[] data = string.split(",");

			// Parse the location
			String[] locs = data[2].substring(9).split(",");
			Location location = new Location(Bukkit.getWorld(locs[0]), Integer.parseInt(locs[1]), Integer.parseInt(locs[2]), Integer.parseInt(locs[3]));

			// Build the object
			Shrine shrine = new Shrine(Integer.parseInt(data[0]), CharacterAPI.getChar(Integer.parseInt(data[1])), location);

			// Return the new Shrine
			return shrine;
		}

		return null;
	}
}
