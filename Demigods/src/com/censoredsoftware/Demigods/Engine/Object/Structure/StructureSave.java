package com.censoredsoftware.Demigods.Engine.Object.Structure;

import java.util.Set;

import org.bukkit.Location;

import redis.clients.johm.Attribute;
import redis.clients.johm.Id;
import redis.clients.johm.Model;
import redis.clients.johm.Reference;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsLocation;
import com.censoredsoftware.Demigods.Engine.Utility.StructureUtility;

@Model
public class StructureSave
{
	@Id
	private Long Id;
	@Attribute
	private String structureType;
	@Reference
	private DemigodsLocation reference;

	public Location getReferenceLocation()
	{
		return reference.toLocation();
	}

	public Set<Location> getLocations()
	{
		return StructureUtility.getLocations(reference.toLocation(), getStructureInfo().getSchematics());
	}

	public StructureInfo getStructureInfo()
	{
		for(StructureInfo structure : Demigods.getLoadedStructures())
		{
			if(structure.getStructureType().equalsIgnoreCase(this.structureType)) return structure;
		}
		return null;
	}

	public void generate()
	{
		for(StructureSchematic schematic : getStructureInfo().getSchematics())
		{
			schematic.generate(this.reference.toLocation());
		}
	}
}
