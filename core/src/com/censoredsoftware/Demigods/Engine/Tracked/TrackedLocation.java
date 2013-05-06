package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import redis.clients.johm.Attribute;
import redis.clients.johm.Id;
import redis.clients.johm.Indexed;
import redis.clients.johm.Model;

import com.censoredsoftware.Demigods.Engine.DemigodsData;

@Model
public class TrackedLocation
{
	@Id
	private long id;
	@Attribute
	@Indexed
	private String world;
	@Attribute
	private double X;
	@Attribute
	private double Y;
	@Attribute
	private double Z;
	@Attribute
	private float pitch;
	@Attribute
	private float yaw;

	void setWorld(String world)
	{
		this.world = world;
	}

	void setX(double X)
	{
		this.X = X;
	}

	void setY(double Y)
	{
		this.Y = Y;
	}

	void setZ(double Z)
	{
		this.Z = Z;
	}

	void setYaw(float yaw)
	{
		this.yaw = yaw;
	}

	void setPitch(float pitch)
	{
		this.pitch = pitch;
	}

	public static void save(TrackedLocation location)
	{
		DemigodsData.jOhm.save(location);
	}

	public static TrackedLocation load(long id) // TODO This belongs somewhere else.
	{
		return DemigodsData.jOhm.get(TrackedLocation.class, id);
	}

	public static Set<TrackedLocation> loadAll()
	{
		return DemigodsData.jOhm.getAll(TrackedLocation.class);
	}

	public static TrackedLocation getTracked(Location location) // TODO: Determine if this should be the default for getting TrackedLocations, or if it is too intensive on the DB for constant use.
	{
		for(TrackedLocation tracked : loadAll())
		{
			if(location.equals(tracked)) return tracked;
		}
		return TrackedModelFactory.createTrackedLocation(location);
	}

	public Location toLocation() throws NullPointerException
	{
		return new Location(Bukkit.getServer().getWorld(this.world), this.X, this.Y, this.Z, this.yaw, this.pitch);
	}

	public long getId()
	{
		return this.id;
	}

	@Override
	public boolean equals(Object object)
	{
		if(object instanceof Location) return this.toLocation().distance((Location) object) < 1;
		else if(object instanceof TrackedLocation) return this.toLocation().distance(((TrackedLocation) object).toLocation()) < 1;
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
