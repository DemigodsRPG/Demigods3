package com.censoredsoftware.demigods.exclusive.district;

import com.censoredsoftware.censoredlib.data.location.Region;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import org.bukkit.World;

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
