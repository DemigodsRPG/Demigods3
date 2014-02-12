package com.demigodsrpg.demigods.exclusive.district;

import com.demigodsrpg.demigods.engine.location.DemigodsRegion;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import org.bukkit.World;

public interface District
{
	public World getWorld();

	public Range<DemigodsRegion.X> rangeX();

	public Range<DemigodsRegion.Z> rangeZ();

	public ImmutableSet<DemigodsRegion> getRegions();

	public Flag getFlags();

	public interface Flag
	{
		public String name();
	}
}
