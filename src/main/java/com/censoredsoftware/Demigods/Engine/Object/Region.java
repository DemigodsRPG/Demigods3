package com.censoredsoftware.Demigods.Engine.Object;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.common.collect.DiscreteDomain;

public class Region
{
	public final static int REGION_LENGTH = 46;
	public final static int HALF_REGION_LENGTH = REGION_LENGTH / 2;

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
			int temp = number % REGION_LENGTH;
			if(temp >= HALF_REGION_LENGTH) return number + REGION_LENGTH - temp;
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
				return integer + REGION_LENGTH;
			}

			@Override
			public Integer previous(Integer integer)
			{
				return integer - REGION_LENGTH;
			}

			@Override
			public long distance(Integer integer, Integer integer2)
			{
				return Math.abs((getRegionCoordinate(integer) / REGION_LENGTH) - (getRegionCoordinate(integer2) / REGION_LENGTH));
			}
		}
	}
}
