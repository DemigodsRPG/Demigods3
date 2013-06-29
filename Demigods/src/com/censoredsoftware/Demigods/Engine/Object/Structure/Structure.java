package com.censoredsoftware.Demigods.Engine.Object.Structure;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.event.Listener;

public interface Structure
{
	public StructureInfo getInfo();

	public Listener getUniqueListener();

	public Structure getFromId(Long Id);

	public Set<Location> getLocations(Long Id);

	public Structure getAll();

	public void createNew(Location reference, boolean generate);
}
