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

	public TrackedLocation(String world, double X, double Y, double Z, float yaw, float pitch)
	{
		this.world = world;
		this.X = X;
		this.Y = Y;
		this.Z = Z;
		this.yaw = yaw;
		this.pitch = pitch;

		save();
	}

	public TrackedLocation(Location location)
	{
		this.world = location.getWorld().getName();
		this.X = location.getX();
		this.Y = location.getY();
		this.Z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();

		save();
	}

	public void save()
	{
		DemigodsData.jOhm.save(this);
	}

	public static TrackedLocation load(long id) // TODO This belongs somewhere else.
	{
		return DemigodsData.jOhm.get(TrackedLocation.class, id);
	}

	public static Set<TrackedLocation> loadAll()
	{
		return DemigodsData.jOhm.getAll(TrackedLocation.class);
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
