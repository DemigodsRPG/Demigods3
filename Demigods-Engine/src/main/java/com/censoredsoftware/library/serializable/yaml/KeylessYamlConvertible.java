package com.censoredsoftware.library.serializable.yaml;

/**
 * Convenience abstract class for all YamlConvertible child classes that don't use a key-value system.
 */
public abstract class KeylessYamlConvertible<V> extends YamlConvertible<Comparable, V>
{
	@Override
	public Comparable keyFromString(String string)
	{
		return null;
	}
}
