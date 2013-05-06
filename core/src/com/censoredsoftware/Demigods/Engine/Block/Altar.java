package com.censoredsoftware.Demigods.Engine.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import com.censoredsoftware.Demigods.API.BlockAPI;
import com.censoredsoftware.Demigods.API.LocationAPI;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedBlock;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedLocation;

public class Altar
{
	private Map<String, Object> altarData;

	public Altar(Map map)
	{
		setMap(map);
		DemigodsData.altarData.saveData(getID(), this);
	}

	public Altar(int id, Location location)
	{
		altarData = new HashMap<String, Object>();

		// Set the variables
		saveData("ALTAR_ID", id);
		saveData("ALTAR_CENTER", new TrackedLocation(DemigodsData.generateInt(5), location, null).getID());
		saveData("ALTAR_ACTIVE", true);

		// Generate the Altar
		generate();

		DemigodsData.altarData.saveData(getID(), this); // TODO This needs a better home, maybe as part of a factory.
	}

	/**
	 * Removes the Altar completely.
	 */
	public void remove()
	{
		DemigodsData.altarData.removeData(getID());
		for(Integer block : (List<Integer>) getData("BLOCKS"))
		{
			BlockAPI.getBlock(block).remove();
		}
	}

	public boolean containsKey(String key)
	{
		return altarData.get(key) != null && altarData.containsKey(key);
	}

	public Object getData(String key)
	{
		return altarData.get(key);
	}

	public void saveData(String key, Object data)
	{
		altarData.put(key, data);
	}

	public void removeData(String key)
	{
		altarData.remove(key);
	}

	/**
	 * Returns the id for the Altar.
	 * 
	 * @return Integer
	 */
	public int getID()
	{
		return Integer.parseInt(getData("ALTAR_ID").toString());
	}

	public Map getMap()
	{
		return altarData;
	}

	public void setMap(Map map)
	{
		altarData = map;
	}

	/**
	 * Returns the location of the Altar.
	 * 
	 * @return Location
	 */
	public Location getLocation()
	{
		return LocationAPI.getLocation(Integer.parseInt(getData("ALTAR_CENTER").toString())).toLocation();
	}

	/**
	 * Returns true if the Altar is marked as active.
	 * 
	 * @return boolean
	 */
	public boolean isActive()
	{
		return Boolean.parseBoolean(getData("ALTAR_ACTIVE").toString());
	}

	/**
	 * Sets the active status of this Altar to <code>option</code>.
	 * 
	 * @param option the option to set.
	 */
	public void toggleActive(boolean option)
	{
		saveData("ALTAR_ACTIVE", option);
	}

	/**
	 * Returns true if the <code>location</code> matches a location within the Altar.
	 * 
	 * @param location the location to check.
	 * @return boolean
	 */
	public boolean locationMatches(Location location)
	{
		for(Integer block : (List<Integer>) getData("BLOCKS"))
		{
			if(BlockAPI.getBlock(block).getLocation().equals(location)) return true;
		}
		return false;
	}

	/**
	 * Generates a full Altar structure.
	 */
	public void generate()
	{
		List<Integer> blocks = new ArrayList<Integer>();
		Location location = getLocation();

		// Remove the emerald block
		location.getBlock().setTypeId(0);

		// Split the location so we can build off of it
		double locX = location.getX();
		double locY = location.getY();
		double locZ = location.getZ();
		World locWorld = location.getWorld();

		// Create the enchantment table
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX, locY + 2, locZ), "altar", Material.ENCHANTMENT_TABLE).getID());

		// Create magical table stand
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX, locY + 1, locZ), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 2, locY + 4, locZ + 2), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 2, locY + 4, locZ - 2), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 2, locY + 4, locZ - 2), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 2, locY + 4, locZ + 2), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 2, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 2, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 2, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 2, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX, locY + 6, locZ), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 1, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 1, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 1, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 1, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 1, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 1, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX, locY + 5, locZ), "altar", Material.getMaterial(5), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX, locY + 5, locZ - 1), "altar", Material.getMaterial(5), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX, locY + 5, locZ + 1), "altar", Material.getMaterial(5), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 3, locY, locZ + 3), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 3, locY, locZ - 3), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 3, locY, locZ - 3), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 3, locY, locZ + 3), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 2, locY + 3, locZ + 2), "altar", Material.getMaterial(44), (byte) 13).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 2, locY + 3, locZ - 2), "altar", Material.getMaterial(44), (byte) 13).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 2, locY + 3, locZ - 2), "altar", Material.getMaterial(44), (byte) 13).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 2, locY + 3, locZ + 2), "altar", Material.getMaterial(44), (byte) 13).getID());

		// Left beam
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 1, locY + 4, locZ - 2), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX, locY + 4, locZ - 2), "altar", Material.getMaterial(98), (byte) 3).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 1, locY + 4, locZ - 2), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 1, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 1, locY + 5, locZ - 2), "altar", Material.getMaterial(126), (byte) 1).getID());

		// Right beam
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 1, locY + 4, locZ + 2), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX, locY + 4, locZ + 2), "altar", Material.getMaterial(98), (byte) 3).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 1, locY + 4, locZ + 2), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 1, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 1, locY + 5, locZ + 2), "altar", Material.getMaterial(126), (byte) 1).getID());

		// Top beam
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 2, locY + 4, locZ + 1), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 2, locY + 4, locZ), "altar", Material.getMaterial(98), (byte) 3).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 2, locY + 4, locZ - 1), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 2, locY + 5, locZ + 1), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 2, locY + 5, locZ), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + 2, locY + 5, locZ - 1), "altar", Material.getMaterial(126), (byte) 1).getID());

		// Bottom beam
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 2, locY + 4, locZ + 1), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 2, locY + 4, locZ), "altar", Material.getMaterial(98), (byte) 3).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 2, locY + 4, locZ - 1), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 2, locY + 5, locZ + 1), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 2, locY + 5, locZ), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - 2, locY + 5, locZ - 1), "altar", Material.getMaterial(126), (byte) 1).getID());

		// Set locations to use for building
		Location topLeft = new Location(locWorld, locX + 2, locY + 1, locZ - 2);
		Location topRight = new Location(locWorld, locX + 2, locY + 1, locZ + 2);
		Location botLeft = new Location(locWorld, locX - 2, locY + 1, locZ - 2);
		Location botRight = new Location(locWorld, locX - 2, locY + 1, locZ + 2);

		// Top left of platform
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topLeft, "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topLeft.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topLeft.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topLeft.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5).getID());

		// Top right of platform
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topRight, "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topRight.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topRight.subtract(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topRight.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5).getID());

		// Bottom left of platform
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botLeft, "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botLeft.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botLeft.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botLeft.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5).getID());

		// Bottom right of platform
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botRight, "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botRight.subtract(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botRight.add(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botRight.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5).getID());

		// Create central structure of platform
		for(int i = 1; i < 3; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX, locY + 1, locZ + i), "altar", Material.getMaterial(44), (byte) 5).getID());
		for(int i = 1; i < 3; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX, locY + 1, locZ - i), "altar", Material.getMaterial(44), (byte) 5).getID());
		for(int i = 1; i < 3; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX - i, locY + 1, locZ), "altar", Material.getMaterial(44), (byte) 5).getID());
		for(int i = 1; i < 3; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), new Location(locWorld, locX + i, locY + 1, locZ), "altar", Material.getMaterial(44), (byte) 5).getID());

		// Build steps on all sides.
		Location leftSteps = new Location(locWorld, locX + 2, locY, locZ - 4);
		Location rightSteps = new Location(locWorld, locX + 2, locY, locZ + 4);
		Location topSteps = new Location(locWorld, locX + 4, locY, locZ - 2);
		Location botSteps = new Location(locWorld, locX - 4, locY, locZ - 2);

		// Create left steps
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), leftSteps, "altar", Material.getMaterial(44), (byte) 5).getID());
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), leftSteps.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), leftSteps.add(0, 0, 1), "altar", Material.getMaterial(98)).getID());
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), leftSteps.add(1, 0, 0), "altar", Material.getMaterial(98)).getID());

		// Create right steps
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), rightSteps, "altar", Material.getMaterial(44), (byte) 5).getID());
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), rightSteps.subtract(1, 0, 0), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), rightSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)).getID());
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), rightSteps.add(1, 0, 0), "altar", Material.getMaterial(98)).getID());

		// Create top steps
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topSteps, "altar", Material.getMaterial(44), (byte) 5).getID());
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topSteps.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topSteps.subtract(1, 0, 0), "altar", Material.getMaterial(98)).getID());
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)).getID());

		// Create bottom steps
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botSteps, "altar", Material.getMaterial(44), (byte) 5).getID());
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botSteps.add(0, 0, 1), "altar", Material.getMaterial(44), (byte) 5).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botSteps.add(1, 0, 0), "altar", Material.getMaterial(98)).getID());
		for(int i = 1; i < 5; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botSteps.subtract(0, 0, 1), "altar", Material.getMaterial(98)).getID());

		// Create left step towers
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), leftSteps.add(0, 1, 0), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), leftSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), leftSteps.subtract(4, 0, 0), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), leftSteps, "altar", Material.getMaterial(126), (byte) 1).getID());
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), leftSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)).getID());

		// Create right step towers
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), rightSteps.add(0, 1, 0), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), rightSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), rightSteps.subtract(4, 0, 0), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), rightSteps, "altar", Material.getMaterial(126), (byte) 1).getID());
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), rightSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)).getID());

		// Create top step towers
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topSteps.add(0, 1, 0), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topSteps.add(0, 0, 4), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topSteps, "altar", Material.getMaterial(126), (byte) 1).getID());
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), topSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)).getID());

		// Create bottom step towers
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botSteps.add(0, 1, 0), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botSteps.add(0, 1, 0), "altar", Material.getMaterial(126), (byte) 1).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botSteps.add(0, 0, 4), "altar", Material.getMaterial(98)).getID());
		blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botSteps, "altar", Material.getMaterial(126), (byte) 1).getID());
		for(int i = 0; i < 3; i++)
			blocks.add(new TrackedBlock(DemigodsData.generateInt(5), botSteps.subtract(0, 1, 0), "altar", Material.getMaterial(98)).getID());

		saveData("BLOCKS", blocks);
	}

	@Override
	public boolean equals(Object object)
	{
		return !(object == null || !(object instanceof Altar)) && getID() == parse(object).getID();
	}

	@Override
	public String toString()
	{
		return "Altar{id=" + getID() + ",active=" + isActive() + ",center=" + getLocation().getWorld().getName() + "," + getLocation().getX() + "," + getLocation().getY() + "," + getLocation().getZ() + "}";
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
			altar.toggleActive(Boolean.parseBoolean(data[1].substring(7)));

			// Return the new Altar
			return altar;
		}

		return null;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
