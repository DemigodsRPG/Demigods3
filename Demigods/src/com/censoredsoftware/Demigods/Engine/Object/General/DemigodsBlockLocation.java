package com.censoredsoftware.Demigods.Engine.Object.General;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.common.base.Objects;

public class DemigodsBlockLocation extends Location
{
	World world;
	Double X;
	Double Y;
	Double Z;
	Float yaw;
	Float pitch;

	public DemigodsBlockLocation(Location location)
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
		if(obj instanceof DemigodsBlockLocation)
		{
			final DemigodsBlockLocation other = (DemigodsBlockLocation) obj;
			return Objects.equal(this.world, other.world) && Objects.equal(this.X, other.X) && Objects.equal(this.Y, other.Y) && Objects.equal(this.Z, other.Z) && Objects.equal(this.yaw, other.yaw) && Objects.equal(this.pitch, other.pitch);
		}
		if(obj instanceof DemigodsBlock)
		{
			final DemigodsBlock other = (DemigodsBlock) obj;
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
