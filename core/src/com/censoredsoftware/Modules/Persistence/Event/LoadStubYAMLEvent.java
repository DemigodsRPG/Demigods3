package com.censoredsoftware.Modules.Persistence.Event;

import java.util.Map;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event that is triggered when data is loaded from a YAML file.
 */
public class LoadStubYAMLEvent extends Event implements Cancellable
{
	private String pluginName, path, dataName;
	private int id;
	private Map map;
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;

	/**
	 * Constructor for the LoadStubYAMLEvent.
	 * 
	 * @param pluginName Name of the plugin the data belongs to.
	 * @param dataName Name of the data set being loaded.
	 * @param id The ID of the stub.
	 * @param map The data that was loaded.
	 */
	public LoadStubYAMLEvent(String pluginName, String path, String dataName, int id, Map map)
	{
		this.pluginName = pluginName;
		this.path = path;
		this.dataName = dataName;
		this.id = id;
		this.map = map;
	}

	/**
	 * Returns the plugin name of the loaded file.
	 * 
	 * @return The plugin name.
	 */
	public String getPluginName()
	{
		return this.pluginName;
	}

	/**
	 * Returns the path of the loaded file.
	 * 
	 * @return The path.
	 */
	public String getPath()
	{
		return this.path;
	}

	/**
	 * Returns the data name of the loaded file.
	 * 
	 * @return The data name.
	 */
	public String getDataName()
	{
		return this.dataName;
	}

	/**
	 * Returns the id of the stub.
	 * 
	 * @return ID.
	 */
	public int getID()
	{
		return this.id;
	}

	/**
	 * Returns the data that was loaded.
	 * 
	 * @return The map data.
	 */
	public Map getData()
	{
		return this.map;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	@Override
	public boolean isCancelled()
	{
		return this.cancelled;
	}

	@Override
	public synchronized void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
}
