package com.censoredsoftware.demigods.exclusive.district;

import org.bukkit.World;

import com.censoredsoftware.censoredlib.data.location.Region;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;

public interface District
{
	public World getWorld();

	public Range<Region.X> rangeX();

	public Range<Region.Z> rangeZ();

	public ImmutableSet<Region> getRegions();

	public Flag getFlags();

	public interface Flag
	{
		public String name();
	}
}
