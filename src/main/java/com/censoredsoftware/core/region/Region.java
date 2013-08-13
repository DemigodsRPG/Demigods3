package com.censoredsoftware.core.region;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.common.base.Objects;

public class Region
{
	public final static int REGION_LENGTH = 8;
	public final static int HALF_REGION_LENGTH = REGION_LENGTH / 2;

	private final int x;
	private final int z;
	private final String world;

	private Region(int x, int z, String world)
	{
		this.x = x;
		this.z = z;
		this.world = world;
	}

	public int getX()
	{
		return x;
	}

	public int getZ()
	{
		return z;
	}

	public String getWorld()
	{
		return world;
	}

	public Location getCenter()
	{
		return new Location(Bukkit.getWorld(world), x, 128, z);
	}

	public Region[] getSurroundingRegions()
	{
		Region[] area = new Region[9];
		area[0] = new Region(x - REGION_LENGTH, z - REGION_LENGTH, world);
		area[1] = new Region(x - REGION_LENGTH, z, world);
		area[2] = new Region(x - REGION_LENGTH, z + REGION_LENGTH, world);
		area[3] = new Region(x, z - REGION_LENGTH, world);
		area[4] = this;
		area[5] = new Region(x, z + REGION_LENGTH, world);
		area[6] = new Region(x + REGION_LENGTH, z - REGION_LENGTH, world);
		area[7] = new Region(x + REGION_LENGTH, z, world);
		area[8] = new Region(x + REGION_LENGTH, z + REGION_LENGTH, world);
		return area;
	}

	@Override
	public String toString()
	{
		return Objects.toStringHelper(this).add("world", world).add("x", x).add("z", z).toString();
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(world, x, z);
	}

	@Override
	public boolean equals(Object object)
	{
		return Objects.equal(this, object);
	}

	public static class Util
	{
		public static Region getRegion(Location location)
		{
			return new Region(getRegionCoordinate(location.getBlockX()), getRegionCoordinate(location.getBlockZ()), location.getWorld().getName());
		}

		public static Region getRegion(int X, int Z, String world)
		{
			return new Region(getRegionCoordinate(X), getRegionCoordinate(Z), world);
		}

		private static int getRegionCoordinate(int number)
		{
			int temp = number % REGION_LENGTH;
			if(temp >= HALF_REGION_LENGTH) return number + REGION_LENGTH - temp;
			return number - temp;
		}
	}
}
