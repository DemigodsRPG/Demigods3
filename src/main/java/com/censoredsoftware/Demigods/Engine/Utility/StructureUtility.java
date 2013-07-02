package com.censoredsoftware.Demigods.Engine.Utility;

import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureInfo;
import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureSave;
import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureSchematic;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class StructureUtility
{
	public static boolean partOfStructure(Location location)
	{
		for(StructureSave structureSave : getAllStructureSaves())
		{
			if(structureSave.getLocations().contains(location)) return true;
		}
		return false;
	}

	public static boolean partOfStructureWithFlag(Location location, StructureInfo.Flag flag)
	{
		for(StructureSave structureSave : getAllStructureSaves())
		{
			if(structureSave.getLocations().contains(location) && structureSave.getStructureInfo().getFlags().contains(flag)) return true;
		}
		return false;
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
