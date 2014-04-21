package com.demigodsrpg.demigods.engine.data;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

import javax.annotation.Nullable;

public enum IdType
{
	/**
	 * String.
	 */
	STRING(new Function<String, String>()
	{
		@Override
		public String apply(@Nullable String s)
		{
			return s;
		}
	}, java.lang.String.class),
	/**
	 * UUID.
	 */
	UUID(new Function<String, java.util.UUID>()
	{
		@Override
		public java.util.UUID apply(@Nullable String s)
		{
			if(s.contains("-")) return java.util.UUID.fromString(s);
			return toUUID(s);
		}
	}, java.util.UUID.class),
	/**
	 * Void (invalid).
	 */
	VOID(new Function<String, Comparable>()
	{
		@Override
		public Comparable apply(@Nullable String s)
		{
			return null;
		}
	}, Comparable.class);

	private Function<String, ?> fromString;
	private Class cast;

	/**
	 * Id data for a data type.
	 *
	 * @param fromString Convert the id from a string.
	 */
	private <K> IdType(Function<String, K> fromString, Class<K> cast)
	{
		this.fromString = fromString;
		this.cast = cast;
	}

	@Override
	public String toString()
	{
		return name();
	}

	@SuppressWarnings("unchecked")
	public <K> K fromString(String string)
	{
		return (K) fromString.apply(string);
	}

	@SuppressWarnings("unchecked")
	public <K> K cast(Object o)
	{
		return (K) cast.cast(o);
	}

	public Class getCastClass()
	{
		return cast;
	}

	public static java.util.UUID toUUID(String mojangId)
	{
		// Check that it is long enough.
		if(mojangId.length() != 32) return null;

		// Grab the components from the UUID.
		String component1 = mojangId.substring(0, 8),
				component2 = mojangId.substring(8, 12),
				component3 = mojangId.substring(12, 16),
				component4 = mojangId.substring(16, 20),
				component5 = mojangId.substring(20);

		// Create the new String.
		String fullId = Joiner.on('-').join(component1, component2, component3, component4, component5);

		// Transform to UUID
		return java.util.UUID.fromString(fullId);
	}
}
