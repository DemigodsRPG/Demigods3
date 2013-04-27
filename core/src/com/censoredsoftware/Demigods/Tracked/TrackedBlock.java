package com.censoredsoftware.Demigods.Tracked;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;

import com.censoredsoftware.Demigods.API.LocationAPI;
import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Modules.Data.DataStubModule;

public class TrackedBlock implements DataStubModule
{
	private Map<String, Object> blockData;

	public TrackedBlock(Map map)
	{
		setMap(map);
		save(this);
	}

	public TrackedBlock(int id, Location location, String type, Material material)
	{
		blockData = new HashMap<String, Object>();

		saveData("BLOCK_ID", id);
		saveData("BLOCK_TYPE", type);
		saveData("BLOCK_MATERIAL", material.getId());
		saveData("BLOCK_PREVIOUS_MATERIAL", location.getBlock().getTypeId());
		saveData("BLOCK_MATERIAL_BYTE", (byte) 0);
		saveData("BLOCK_LOCATION", new TrackedLocation(DemigodsData.generateInt(5), location, null).getID());

		// Create the actual block
		location.getBlock().setType(material);

		save(this);
	}

	public TrackedBlock(int id, Location location, String type, Material material, byte matByte)
	{
		blockData = new HashMap<String, Object>();

		saveData("BLOCK_ID", id);
		saveData("BLOCK_TYPE", type);
		saveData("BLOCK_MATERIAL", material.getId());
		saveData("BLOCK_PREVIOUS_MATERIAL", location.getBlock().getTypeId());
		saveData("BLOCK_MATERIAL_BYTE", matByte);
		saveData("BLOCK_LOCATION", new TrackedLocation(DemigodsData.generateInt(5), location, null).getID());

		// Create the actual block
		location.getBlock().setType(material);
		location.getBlock().setData(matByte, true);

		save(this);
	}

	public static void save(TrackedBlock block) // TODO This belongs somewhere else.
	{
		DemigodsData.trackedBlockData.saveData(block.getID(), block);
	}

	/**
	 * Checks if the blockData Map contains <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return True if blockData contains the key.
	 */
	@Override
	public boolean containsKey(String key)
	{
		return blockData.get(key) != null && blockData.containsKey(key);
	}

	/**
	 * Retrieve the Object data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Object data.
	 */
	@Override
	public Object getData(String key)
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
	@Override
	public void saveData(String key, Object data)
	{
		blockData.put(key, data);
	}

	/**
	 * Remove the data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 */
	@Override
	public void removeData(String key)
	{
		if(!containsKey(key)) return;
		blockData.remove(key);
	}

	/*
	 * remove() : Removes the block.
	 */
	public void remove()
	{
		getLocation().getBlock().setTypeId(Integer.parseInt(getData("BLOCK_PREVIOUS_MATERIAL").toString()));
	}

	/*
	 * getID() : Returns the ID of the block.
	 */
	@Override
	public int getID()
	{
		return Integer.parseInt(getData("BLOCK_ID").toString());
	}

	@Override
	public Map getMap()
	{
		return blockData;
	}

	@Override
	public void setMap(Map map)
	{
		blockData = map;
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
		return LocationAPI.getLocation((Integer.parseInt(getData("BLOCK_LOCATION").toString()))).toLocation();
	}
}
