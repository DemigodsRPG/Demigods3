package com.censoredsoftware.Demigods.Engine.Block;

import org.bukkit.Location;

import com.censoredsoftware.Demigods.Engine.Tracked.TrackedLocation;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedModelFactory;

public class BlockFactory
{
	public static Altar createAltar(Location location)
	{
		TrackedLocation center = TrackedModelFactory.createTrackedLocation(location);

		Altar altar = new Altar();
		altar.setActive(true);
		altar.setCenter(center);
		Altar.save(altar);
		Altar.generateNewBlocks(altar, location);
		return altar;
	}
}
