package com.censoredsoftware.Demigods.Engine.Structure;

import com.censoredsoftware.Demigods.Engine.Listener.StructureListener;

public interface Structure
{
	public StructureInfo getInfo();

	public StructureListener getListener();

	public Structure getFromId(Long Id);

	public Structure getAll();
}
