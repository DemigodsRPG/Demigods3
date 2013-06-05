package com.censoredsoftware.Demigods.Engine.Structure;

import org.bukkit.Location;

import redis.clients.johm.Attribute;
import redis.clients.johm.Id;
import redis.clients.johm.Model;
import redis.clients.johm.Reference;

import com.censoredsoftware.Demigods.Engine.Tracked.TrackedLocation;

@Model
public class StructureSave
{
	@Id
	private Long Id;
	@Attribute
	private String structureType;
	@Reference
	private TrackedLocation reference;

	public Location getReferenceLocation()
	{
		return reference.toLocation();
	}
}
