package com.censoredsoftware.Demigods.Engine.Utility;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureInfo;
import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureSave;
import com.censoredsoftware.Demigods.Engine.Object.Structure.StructureSchematic;

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
		Demigods.message.broadcast("Looking for structures...");
		for(StructureSave structureSave : getAllStructureSaves())
		{
			if(structureSave.getLocations().contains(location) && structureSave.getStructureInfo().getFlags().contains(flag)) return true;
		}
		Demigods.message.broadcast("Not found.");
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
