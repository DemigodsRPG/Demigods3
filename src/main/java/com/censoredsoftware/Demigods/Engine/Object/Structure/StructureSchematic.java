package com.censoredsoftware.Demigods.Engine.Object.Structure;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;

import com.google.common.collect.Sets;

public class StructureSchematic extends HashSet<StructureCuboid>
{
	private String name, designer;

	public StructureSchematic(String name, String designer)
	{
		this.name = name;
		this.designer = designer;
	}

	public Set<Location> getLocations(Location reference)
	{
		Set<Location> locations = Sets.newHashSet();
		for(StructureCuboid cuboid : this)
			locations.addAll(cuboid.getBlockLocations(reference));
		return locations;
	}

	public void generate(Location reference)
	{
		for(StructureCuboid cuboid : this)
			cuboid.generate(reference);
	}

	public interface StructureDesign
	{
		public int getIndex();

		public String getName();
	}
}
