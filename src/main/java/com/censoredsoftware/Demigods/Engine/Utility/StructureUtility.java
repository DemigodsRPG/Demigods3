package com.censoredsoftware.Demigods.Engine.Utility;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;

import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureInfo;
import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureSave;
import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureSchematic;

public class StructureUtility
{
	public static StructureSave getStructure(Location location)
	{
		for(StructureSave structureSave : getAllStructureSaves())
		{
			if(!structureSave.getReferenceLocation().getWorld().equals(location.getWorld())) continue;
			if(structureSave.getLocations().contains(location)) return structureSave;
		}
		return null;
	}

	public static boolean partOfStructureWithType(Location location, String structureType)
	{
		for(StructureSave structureSave : getAllStructureSaves())
		{
			if(!structureSave.getReferenceLocation().getWorld().equals(location.getWorld())) continue;
			if(structureSave.getLocations().contains(location) && structureSave.getStructureInfo().getStructureType().equals(structureType)) return true;
		}
		return false;
	}

	public static boolean partOfStructureWithFlag(Location location, StructureInfo.Flag flag)
	{
		for(StructureSave structureSave : getAllStructureSaves())
		{
			if(!structureSave.getReferenceLocation().getWorld().equals(location.getWorld())) continue;
			if(structureSave.getLocations().contains(location) && structureSave.getStructureInfo().getFlags().contains(flag)) return true;
		}
		return false;
	}

	public static boolean isCenterBlockWithFlag(Location location, StructureInfo.Flag flag)
	{
		for(StructureSave structureSave : getAllStructureSaves())
		{
			if(!structureSave.getReferenceLocation().getWorld().equals(location.getWorld())) continue;
			if(structureSave.getClickableBlock().equals(location) && structureSave.getStructureInfo().getFlags().contains(flag)) return true;
		}
		return false;
	}

	public static boolean isInRadiusWithFlag(Location location, StructureInfo.Flag flag)
	{
		return getInRadiusWithFlag(location, flag) != null;
	}

	public static StructureSave getInRadiusWithFlag(Location location, StructureInfo.Flag flag)
	{
		for(StructureSave structureSave : getAllStructureSaves())
		{
			if(!structureSave.getReferenceLocation().getWorld().equals(location.getWorld())) continue;
			if(structureSave.getReferenceLocation().distance(location) <= structureSave.getStructureInfo().getRadius() && structureSave.getStructureInfo().getFlags().contains(flag)) return structureSave;
		}
		return null;
	}

	public static void regenerateStructures()
	{
		for(StructureSave save : getAllStructureSaves())
		{
			save.generate();
		}
	}

	public static Set<StructureSave> getAllStructureSaves()
	{
		return DataUtility.jOhm.getAll(StructureSave.class);
	}

	public static Set<Location> getLocations(final Location reference, final Set<StructureSchematic> schematics)
	{
		return new HashSet<Location>()
		{
			{
				for(StructureSchematic schematic : schematics)
				{
					addAll(schematic.getBlockLocations(reference));
				}
			}
		};
	}
}
