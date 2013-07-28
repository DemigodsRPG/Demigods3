package com.censoredsoftware.Demigods.Engine.Object;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.common.collect.DiscreteDomain;

public class Region
{
	private int x;
	private int z;

	public Region(int x, int z)
	{
		this.x = x;
		this.z = z;
	}

	public int getX()
	{
		return x;
	}

	public int getZ()
	{
		return z;
	}

	public Location getCenter(World world)
	{
		return new Location(world, x, 128, z);
	}

	public static class Util
	{
		public static Region getRegion(Location location)
		{
			return new Region(getRegionCoordinate(location.getBlockX()), getRegionCoordinate(location.getBlockZ()));
		}

		public static Region getRegion(int X, int Z)
		{
			return new Region(getRegionCoordinate(X), getRegionCoordinate(Z));
		}

		private static int getRegionCoordinate(int number)
		{
			int temp = number % 64;
			if(temp >= 32) return number + 64 - temp;
			return number - temp;
		}

		public static Size size()
		{
			return new Size();
		}

		private static class Size extends DiscreteDomain<Integer>
		{
			@Override
			public Integer next(Integer integer)
			{
				return integer + 64;
			}

			@Override
			public Integer previous(Integer integer)
			{
				return integer - 64;
			}

			@Override
			public long distance(Integer integer, Integer integer2)
			{
				return Math.abs((getRegionCoordinate(integer) / 64) - (getRegionCoordinate(integer2) / 64));
			}
		}
	}
}
