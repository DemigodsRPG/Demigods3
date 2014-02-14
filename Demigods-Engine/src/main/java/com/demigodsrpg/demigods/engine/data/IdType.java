package com.demigodsrpg.demigods.engine.data;

import com.google.common.base.Function;

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
			return java.util.UUID.fromString(s);
		}
	}, java.util.UUID.class),
	/**
	 * Void (invalid).
	 */
	VOID(new Function<String, Void>()
	{
		@Override
		public Void apply(@Nullable String s)
		{
			return null;
		}
	}, java.lang.Void.class);

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
}
