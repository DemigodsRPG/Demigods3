package com.censoredsoftware.Demigods.Engine.Tracked;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.common.base.Objects;

public class ComparableLocation extends Location
{
	World world;
	Double X;
	Double Y;
	Double Z;
	Float yaw;
	Float pitch;

	public ComparableLocation(World world, double x, double y, double z, float yaw, float pitch)
	{
		super(world, x, y, z, yaw, pitch);
		this.world = world;
		this.X = x;
		this.Y = y;
		this.Z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public ComparableLocation(Location location)
	{
		super(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		this.world = location.getWorld();
		this.X = location.getX();
		this.Y = location.getY();
		this.Z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if(this == obj) return true;
		if(obj == null) return false;
		if(obj instanceof ComparableLocation)
		{
			final ComparableLocation other = (ComparableLocation) obj;
			return Objects.equal(this.world, other.world) && Objects.equal(this.X, other.X) && Objects.equal(this.Y, other.Y) && Objects.equal(this.Z, other.Z) && Objects.equal(this.yaw, other.yaw) && Objects.equal(this.pitch, other.pitch);
		}
		if(obj instanceof TrackedBlock)
		{
			final TrackedBlock other = (TrackedBlock) obj;
			return Objects.equal(this.world.getName(), other.location.world) && Objects.equal(this.X, other.location.X) && Objects.equal(this.Y, other.location.Y) && Objects.equal(this.Z, other.location.Z) && Objects.equal(this.yaw, other.location.yaw) && Objects.equal(this.pitch, other.location.pitch);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(world.getName(), X, Y, Z, yaw, pitch);
	}
}
