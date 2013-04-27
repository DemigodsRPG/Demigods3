package com.censoredsoftware.Demigods.Block;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Demigods.Tracked.TrackedBlock;
import com.censoredsoftware.Demigods.Tracked.TrackedLocation;

public class Altar
{
	private int id;
	private boolean active;
	private TrackedLocation center;
	private ArrayList<TrackedBlock> blocks;

	public Altar(int id, Location location)
	{
		// Set the variables
		this.id = id;
		this.center = new TrackedLocation(location, null);
		this.active = true;

		// Generate the Altar
		generate();
	}

	/**
	 * Removes the Altar completely.
	 */
	public void remove()
	{
		DemigodsData.altarData.removeData(this.id);
		for(TrackedBlock block : blocks)
		{
			block.remove();
		}
	}

	/**
	 * Returns the id for the Altar.
	 * 
	 * @return Integer
	 */
	public int getID()
	{
		return this.id;
	}

	/**
	 * Returns the location of the Altar.
	 * 
	 * @return Location
	 */
	public Location getLocation()
	{
		return this.center.toLocation();
	}

	/**
	 * Returns true if the Altar is marked as active.
	 * 
	 * @return boolean
	 */
	public boolean isActive()
	{
		return this.active;
	}

	/**
	 * Sets the active status of this Altar to <code>option</code>.
	 * 
	 * @param option the option to set.
	 */
	public void setActive(boolean option)
	{
		this.active = option;
	}

	/**
	 * Returns true if the <code>location</code> matches a location within the Altar.
	 * 
	 * @param location the location to check.
	 * @return boolean
	 */
	public boolean locationMatches(Location location)
	{
		for(TrackedBlock block : blocks)
		{
			if(block.getLocation().equals(location)) return true;
		}
		return false;
	}

	/**
	 * Generates a full Altar structure.
	 */
	public void generate()
	{
		ArrayList<TrackedBlock> blocks = new ArrayList<TrackedBlock>();
		Location location = this.getLocation();

		// Remove the emerald block
		location.getBlock().setTypeId(0);

		// Split the location so we can build off of it
		double locX = location.getX();
		double locY = location.getY();
		double locZ = location.getZ();
		World locWorld = location.getWorld();

		// Create the enchantment table
		blocks.add(new TrackedBlock(new Location(locWorld, locX, locY + 2, locZ), "altar", Material.ENCHANTMENT_TABLE));

		// Create magical table stand
		blocks.add(new TrackedBlock(new Location(locWorld, locX, locY + 1, locZ), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 2, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 2, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 2, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 2, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 2, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 2, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 2, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 2, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX, locY + 6, locZ), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 1, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 1, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 1, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 1, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 1, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 1, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 3, locY, locZ + 3), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 3, locY, locZ - 3), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 3, locY, locZ - 3), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 3, locY, locZ + 3), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 2, locY + 3, locZ + 2), "altar", Material.getMaterial(44), (byte) 13));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 2, locY + 3, locZ - 2), "altar", Material.getMaterial(44), (byte) 13));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 2, locY + 3, locZ - 2), "altar", Material.getMaterial(44), (byte) 13));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 2, locY + 3, locZ + 2), "altar", Material.getMaterial(44), (byte) 13));

		// Left beam
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 1, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(new Location(locWorld, locX, locY + 4, locZ - 2), "altar", Material.getMaterial(98), (byte) 3));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 1, locY + 4, locZ - 2), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 1, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 1, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1));

		// Right beam
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 1, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(new Location(locWorld, locX, locY + 4, locZ + 2), "altar", Material.getMaterial(98), (byte) 3));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 1, locY + 4, locZ + 2), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 1, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 1, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1));

		// Top beam
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 2, locY + 4, locZ + 1), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 2, locY + 4, locZ), "altar", Material.getMaterial(98), (byte) 3));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 2, locY + 4, locZ - 1), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 2, locY + 5, locZ + 1), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 2, locY + 5, locZ), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX + 2, locY + 5, locZ - 1), "altar", Material.getMaterial(126), (byte) 1));

		// Bottom beam
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 2, locY + 4, locZ + 1), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 2, locY + 4, locZ), "altar", Material.getMaterial(98), (byte) 3));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 2, locY + 4, locZ - 1), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 2, locY + 5, locZ + 1), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 2, locY + 5, locZ), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(new Location(locWorld, locX - 2, locY + 5, locZ - 1), "altar", Material.getMaterial(126), (byte) 1));

		// Set locations to use for building
		Location topLeft = new Location(locWorld, locX + 2, locY + 1, locZ - 2);
		Location topRight = new Location(locWorld, locX + 2, locY + 1, locZ + 2);
		Location botLeft = new Location(locWorld, locX - 2, locY + 1, locZ - 2);
		Location botRight = new Location(locWorld, locX - 2, locY + 1, locZ + 2);

		// Top left of platform
		blocks.add(new TrackedBlock(topLeft, "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(topLeft.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(topLeft.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(topLeft.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));

		// Top right of platform
		blocks.add(new TrackedBlock(topRight, "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(topRight.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(topRight.subtract(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(topRight.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));

		// Bottom left of platform
		blocks.add(new TrackedBlock(botLeft, "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(botLeft.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(botLeft.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(botLeft.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));

		// Bottom right of platform
		blocks.add(new TrackedBlock(botRight, "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(botRight.subtract(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(botRight.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(botRight.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));

		// Create central structure of platform
		for(int i = 1; i < 3; i++)
			blocks.add(new TrackedBlock(new Location(locWorld, locX, locY + 1, locZ + i), "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 3; i++)
			blocks.add(new TrackedBlock(new Location(locWorld, locX, locY + 1, locZ - i), "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 3; i++)
			blocks.add(new TrackedBlock(new Location(locWorld, locX - i, locY + 1, locZ), "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 3; i++)
			blocks.add(new TrackedBlock(new Location(locWorld, locX + i, locY + 1, locZ), "altar", Material.getMaterial(44), (byte) 5));

		// Build steps on all sides.
		Location leftSteps = new Location(locWorld, locX + 2, locY, locZ - 4);
		Location rightSteps = new Location(locWorld, locX + 2, locY, locZ + 4);
		Location topSteps = new Location(locWorld, locX + 4, locY, locZ - 2);
		Location botSteps = new Location(locWorld, locX - 4, locY, locZ - 2);

		// Create left steps
		blocks.add(new TrackedBlock(leftSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(leftSteps.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(leftSteps.add(0, 0, 1), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(leftSteps.add(1, 0, 0), "altar", Material.getMaterial(98)));

		// Create right steps
		blocks.add(new TrackedBlock(rightSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(rightSteps.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(rightSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(rightSteps.add(1, 0, 0), "altar", Material.getMaterial(98)));

		// Create top steps
		blocks.add(new TrackedBlock(topSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(topSteps.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(topSteps.subtract(1, 0, 0), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(topSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)));

		// Create bottom steps
		blocks.add(new TrackedBlock(botSteps, "altar", Material.getMaterial(44), (byte) 5));
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(botSteps.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5));
		blocks.add(new TrackedBlock(botSteps.add(1, 0, 0), "altar", Material.getMaterial(98)));
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(botSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)));

		// Create left step towers
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(leftSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(leftSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(leftSteps.subtract(4, 0, 0), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(leftSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(leftSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));

		// Create right step towers
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(rightSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(rightSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(rightSteps.subtract(4, 0, 0), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(rightSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(rightSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));

		// Create top step towers
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(topSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(topSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(topSteps.add(0, 0, 4), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(topSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(topSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));

		// Create bottom step towers
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(botSteps.add(0, 1, 0), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(botSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1));
		blocks.add(new TrackedBlock(botSteps.add(0, 0, 4), "altar", Material.getMaterial(98)));
		blocks.add(new TrackedBlock(botSteps, "altar", Material.getMaterial(126), (byte) 1));
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(botSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)));

		this.blocks = blocks;
	}

	@Override
	public boolean equals(Object object)
	{
		return !(object == null || !(object instanceof Altar)) && this.id == parse(object).getID();
	}

	@Override
	public String toString()
	{
		return "Altar{id=" + this.id + ",active=" + this.active + ",center=" + center.toLocation().getWorld().getName() + "," + center.toLocation().getX() + "," + center.toLocation().getY() + "," + center.toLocation().getZ() + "}";
	}

	/**
	 * Parses the save object into a new Altar object and returns it.
	 * 
	 * @param object the save to parse.
	 * @return Altar
	 */
	public static Altar parse(Object object)
	{
		if(object instanceof Altar) return (Altar) object;
		else if(object instanceof String)
		{
			// Cast the object into a string
			String string = (String) object;

			// Validate that it's an Altar save
			if(!string.startsWith("Altar{id=")) return null;

			// Begin splitting the string into the different variables to parse with
			string = string.substring(9).replace("}", "");
			String[] data = string.split(",");

			// Parse the location
			String[] locs = data[2].substring(7).split(",");
			Location location = new Location(Bukkit.getWorld(locs[0]), Integer.parseInt(locs[1]), Integer.parseInt(locs[2]), Integer.parseInt(locs[3]));

			// Build the object
			Altar altar = new Altar(Integer.parseInt(data[0]), location);
			altar.setActive(Boolean.parseBoolean(data[1].substring(7)));

			// Return the new Altar
			return altar;
		}

		return null;
	}
}
