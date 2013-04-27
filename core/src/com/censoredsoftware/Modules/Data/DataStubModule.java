package com.censoredsoftware.Modules.Data;

import java.util.Map;

public interface DataStubModule
{

	/**
	 * Checks if the characterData Map contains <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return True if characterData contains the key.
	 */
	public boolean containsKey(String key);

	/**
	 * Retrieve the Object data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @return Object data.
	 */
	public Object getData(String key);

	/**
	 * Save the Object <code>data</code> for int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 * @param data The Object being saved.
	 */
	public void saveData(String key, Object data);

	/**
	 * Remove the data from int <code>key</code>.
	 * 
	 * @param key The key in the save.
	 */
	public void removeData(String key);

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
