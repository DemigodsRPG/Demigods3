package com.censoredsoftware.Modules.Data;

import java.util.Map;

public interface DataStubModule
{
	/**
	 * Grab the DataStubModule ID.
	 * 
	 * @return ID.
	 */
	public int getID();

	/**
	 * Grab the Map in it's entirely.
	 * 
	 * @return Map.
	 */
	public Map getMap();

	/**
	 * Set the Map.
	 * 
	 * @param map
	 */
	public void setMap(Map map);
}
