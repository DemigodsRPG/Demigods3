package com.censoredsoftware.Demigods.Tracked;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.censoredsoftware.Demigods.DemigodsData;
import com.censoredsoftware.Modules.Data.DataStubModule;

public class TrackedLocation implements DataStubModule
{
	private Map<String, Object> locationData;

	public TrackedLocation(Map map)
	{
		setMap(map);
		save(this);
	}

	public TrackedLocation(int id, String world, double X, double Y, double Z, float pitch, float yaw, String name)
	{
		locationData = new HashMap<String, Object>();
		saveData("LOC_ID", id);
		saveData("WORLD", world);
		saveData("X", X);
		saveData("Y", Y);
		saveData("Z", Z);
		saveData("PITCH", pitch);
		saveData("YAW", yaw);
		if(name != null) saveData("NAME", name);

		save(this);
	}

	public TrackedLocation(int id, Location location, String name)
	{
		locationData = new HashMap<String, Object>();
		saveData("LOC_ID", id);
		saveData("WORLD", location.getWorld().getName());
		saveData("X", location.getX());
		saveData("Y", location.getY());
		saveData("Z", location.getZ());
		saveData("PITCH", location.getPitch());
		saveData("YAW", location.getYaw());
		if(name != null) saveData("NAME", name);

		save(this);
	}

	public static void save(TrackedLocation location) // TODO This belongs somewhere else.
	{
		DemigodsData.locationData.saveData(location.getID(), location);
	}

	public boolean hasName()
	{
		return locationData.containsKey("NAME");
	}

	public String getName()
	{
		return getData("NAME").toString();
	}

	public synchronized void setName(String name)
	{
		saveData("NAME", name);
	}

	public Location toLocation() throws NullPointerException
	{
		return new Location(Bukkit.getServer().getWorld(getData("WORLD").toString()), Double.parseDouble(getData("X").toString()), Double.parseDouble(getData("Y").toString()), Double.parseDouble(getData("Z").toString()), Float.parseFloat(getData("YAW").toString()), Float.parseFloat(getData("PITCH").toString()));
	}

	public boolean containsKey(String key)
	{
		return locationData.get(key) != null && locationData.containsKey(key);
	}

	public Object getData(String key)
	{
		return locationData.get(key);
	}

	public void saveData(String key, Object data)
	{
		locationData.put(key, data);
	}

	public void removeData(String key)
	{
		locationData.remove(key);
	}

	@Override
	public int getID()
	{
		return Integer.parseInt(getData("LOC_ID").toString());
	}

	@Override
	public Map getMap()
	{
		return locationData;
	}

	@Override
	public void setMap(Map map)
	{
		locationData = map;
	}
}
