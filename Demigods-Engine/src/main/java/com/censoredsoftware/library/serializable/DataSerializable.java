package com.censoredsoftware.library.serializable;

import java.util.Map;

/**
 * Simple method to signify a class for holding the data.
 */
public interface DataSerializable
{
	/**
	 * Serialize the data held in the child class.
	 * 
	 * @return Map of serialized data for the child class's current instance.
	 */
	Map<String, Object> serialize();
}
