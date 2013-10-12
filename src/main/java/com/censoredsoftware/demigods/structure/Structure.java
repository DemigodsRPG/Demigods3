package com.censoredsoftware.demigods.structure;

import org.bukkit.Location;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.Set;

public interface Structure
{
	public String getName();

	public Design getDesign(final String name);

	public Set<StructureData.Flag> getFlags();

	public Listener getUniqueListener();

	public int getRadius();

	public Collection<StructureData> getAll();

	public StructureData createNew(Location reference, boolean generate);

	public interface Design
	{
		public String getName();

		public Set<Location> getClickableBlocks(Location reference);

		public Schematic getSchematic();
	}
}
