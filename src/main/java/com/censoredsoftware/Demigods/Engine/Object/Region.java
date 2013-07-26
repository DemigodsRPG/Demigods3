package com.censoredsoftware.Demigods.Engine.Object;

import org.bukkit.Location;

public class Region
{
	private int x;
	private int y;

	public Region(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public static class Util
	{
		public static Region getRegion(Location location)
		{
			return new Region(getRegionCoordinate(location.getBlockX()), getRegionCoordinate(location.getBlockX()));
		}

		public static Region getRegion(int X, int Y)
		{
			return new Region(getRegionCoordinate(X), getRegionCoordinate(Y));
		}

		private static int getRegionCoordinate(int number)
		{
			int round = ((number + 64) / 128);
			if(round == 0) return 0;
			if(round == 1) return 128;
			if(number % 128 > 64) return 128 * round;
			return 128 * (round - 1);
		}
	}
}
