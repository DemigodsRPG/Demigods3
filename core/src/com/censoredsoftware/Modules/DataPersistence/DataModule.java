package com.censoredsoftware.Modules.DataPersistence;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Abstract class for all data based modules to extend.
 */
public abstract class DataModule implements Listener
{
	private Map map = new HashMap();
	private String dataName = "example";

	Map grabMap()
	{
		return map;
	}

	protected void overrideMap(Map map)
	{
		try
		{
			this.map = map;
		}
		catch(Exception ignored)
		{}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	void onLoadYAML(LoadYAMLEvent event)
	{
		// Override the data inside of this module with the loaded data if the data name is the same
		if(this.dataName.equals(event.getDataName())) overrideMap(event.getData());
	}

}
