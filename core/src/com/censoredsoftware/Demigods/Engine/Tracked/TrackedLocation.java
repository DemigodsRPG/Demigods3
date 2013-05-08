package com.censoredsoftware.Demigods.Engine.Tracked;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import redis.clients.johm.Attribute;
import redis.clients.johm.Id;
import redis.clients.johm.Indexed;
import redis.clients.johm.Model;

import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.google.common.base.Objects;

@Model
public class TrackedLocation
{
	@Id
	private Long id;
	@Attribute
	@Indexed
	private String world;
	@Attribute
	private Double X;
	@Attribute
	private Double Y;
	@Attribute
	private Double Z;
	@Attribute
	private Float pitch;
	@Attribute
	private Float yaw;

	void setWorld(String world)
	{
		this.world = world;
	}

	void setX(Double X)
	{
		this.X = X;
	}

	void setY(Double Y)
	{
		this.Y = Y;
	}

	void setZ(Double Z)
	{
		this.Z = Z;
	}

	void setYaw(Float yaw)
	{
		this.yaw = yaw;
	}

	void setPitch(Float pitch)
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
		return TrackedModelFactory.createUnsavedTrackedLocation(location);
	}

	public Location toLocation() throws NullPointerException
	{
		return new Location(Bukkit.getServer().getWorld(this.world), this.X, this.Y, this.Z, this.yaw, this.pitch);
	}

	public Long getId()
	{
		return this.id;
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if(this == obj) return true;
		if(obj == null || getClass() != obj.getClass()) return false;
		final Altar other = (Altar) obj;
		return Objects.equal(this.id, other.id) && Objects.equal(this.center, other.center) && Objects.equal(this.active, other.active) && Objects.equal(this.blocks, other.blocks);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(center, active, blocks);
	}
}
