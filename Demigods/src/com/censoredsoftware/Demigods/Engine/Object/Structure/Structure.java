package com.censoredsoftware.Demigods.Engine.Object.Structure;

import org.bukkit.event.Listener;

public interface Structure
{
	public StructureInfo getInfo();

	public Listener getUniqueListener();

	public Structure getFromId(Long Id);

	public Structure getAll();
}
