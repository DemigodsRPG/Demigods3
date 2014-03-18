package com.censoredsoftware.library.serializable.yaml;

/**
 * A yaml file that has convertible key-value types.
 * 
 * @param <K> Key type.
 * @param <V> Value type.
 */
public abstract class YamlConvertible<K, V>
{
	/**
	 * Convert a key from a string.
	 * 
	 * @param stringKey The provided string.
	 * @return The converted key.
	 */
	public abstract K keyFromString(String stringKey);

	/**
	 * Convert to a value from a number of objects representing the data.
	 * 
	 * @param data The provided data objects.
	 * @return The converted value.
	 */
	public abstract V valueFromData(Object... data);
}
