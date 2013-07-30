package com.censoredsoftware.demigods.engine.element.Structure;

import org.bukkit.Location;

public interface StandaloneStructure extends Structure
{
	public Location getClickableBlock(Location reference);

	public Schematic getDesign(String name);
}
