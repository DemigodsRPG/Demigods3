package com.censoredsoftware.Demigods.Tracked;

import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;

public class TrackedBlock
{
	private Map<String, Object> blockData;
	private TrackedLocation location;

	public TrackedBlock(Location location, String type, Material material)
	{
		saveData("BLOCK_ID", generateInt(5));
		saveData("BLOCK_TYPE", type);
		saveData("BLOCK_MATERIAL", material.getId());
		saveData("BLOCK_PREVIOUS_MATERIAL", location.getBlock().getTypeId());
		saveData("BLOCK_MATERIAL_BYTE", (byte) 0);
		this.location = new TrackedLocation(location, null);

		// Create the actual block
		location.getBlock().setType(material);
	}

	public TrackedBlock(Location location, String type, Material material, byte matByte)
	{
		saveData("BLOCK_ID", generateInt(5));
		saveData("BLOCK_TYPE", type);
		saveData("BLOCK_MATERIAL", material.getId());
		saveData("BLOCK_PREVIOUS_MATERIAL", location.getBlock().getTypeId());
		saveData("BLOCK_MATERIAL_BYTE", matByte);
		this.location = new TrackedLocation(location, null);

		// Create the actual block
		location.getBlock().setType(material);
		location.getBlock().setData(matByte, true);
	}

	/**
	 * Checks if the blockData Map contains <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return True if blockData contains the key.
	 */
	boolean containsKey(String key)
	{
		return blockData.get(key) != null && blockData.containsKey(key);
	}

	/**
	 * Retrieve the Object data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Object data.
	 */
	Object getData(String key)
	{
		if(containsKey(key)) return blockData.get(key);
		return null; // Should never happen, always check with containsKey before getting the data.
	}

	/**
	 * Save the Object <code>data</code> for int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @param data The Object being saved.
	 */
	void saveData(String key, Object data)
	{
		blockData.put(key, data);
	}

	/**
	 * Remove the data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 */
	void removeData(String key)
	{
		if(!containsKey(key)) return;
		blockData.remove(key);
	}

	/*
	 * remove() : Removes the block.
	 */
	public void remove()
	{
		location.toLocation().getBlock().setTypeId(Integer.parseInt(getData("BLOCK_PREVIOUS_MATERIAL").toString()));
	}

	/*
	 * getID() : Returns the ID of the block.
	 */
	public int getID()
	{
		return Integer.parseInt(getData("BLOCK_ID").toString());
	}

	/*
	 * getMaterial() : Returns the material of the block.
	 */
	public Material getMaterial()
	{
		return Material.getMaterial(Integer.parseInt(getData("BLOCK_MATERIAL").toString()));
	}

	public byte getMaterialByte()
	{
		return Byte.parseByte(getData("BLOCK_MATERIAL_BYTE").toString());
	}

	public Location getLocation()
	{
		return location.toLocation();
	}

	private static int generateInt(int length)
	{
		// Set allowed characters - Create new string to fill - Generate the string - Return string
		char[] chars = "0123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for(int i = 0; i < length; i++)
		{
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return Integer.parseInt(sb.toString());
	}
}
