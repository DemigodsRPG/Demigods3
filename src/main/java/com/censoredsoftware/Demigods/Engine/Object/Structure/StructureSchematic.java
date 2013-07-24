package com.censoredsoftware.Demigods.Engine.Object.Structure;

import java.util.HashSet;

import org.bukkit.Location;

public class StructureSchematic extends HashSet<StructureCuboid>
{
	private String name, designer;

	public StructureSchematic(String name, String designer)
	{
		this.name = name;
		this.designer = designer;
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
