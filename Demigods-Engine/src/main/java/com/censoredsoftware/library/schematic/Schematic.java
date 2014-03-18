package com.censoredsoftware.library.schematic;

import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.Set;

public class Schematic extends ArrayList<Selection>
{
	private final String name;
	private final String designer;
	private int radius;

	public Schematic(String name, String designer, int groundRadius)
	{
		this.name = name;
		this.designer = designer;
		this.radius = groundRadius;
	}

	public String getName()
	{
		return name;
	}

	public String getDesigner()
	{
		return designer;
	}

	public Set<Location> getLocations(Location reference)
	{
		Set<Location> locations = Sets.newHashSet();
		for(Selection cuboid : this)
			locations.addAll(cuboid.getBlockLocations(reference));
		return locations;
	}

	public int getGroundRadius()
	{
		return this.radius;
	}

	public void generate(final Location reference)
	{
		for(Selection cuboid : this)
			cuboid.generate(reference);
		for(Item drop : reference.getWorld().getEntitiesByClass(Item.class))
			if(reference.distance(drop.getLocation()) <= (getGroundRadius() * 3)) drop.remove();
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
