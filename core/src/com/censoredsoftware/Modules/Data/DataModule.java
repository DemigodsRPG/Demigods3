package com.censoredsoftware.Modules.Data;

import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.censoredsoftware.Modules.Persistence.Event.LoadFileEvent;

/**
 * Interface for all data based modules to extend.
 */
public interface DataModule
{
	public Map getMap();

	public void setMap(Map map);

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLoadYAML(LoadFileEvent event);
}
