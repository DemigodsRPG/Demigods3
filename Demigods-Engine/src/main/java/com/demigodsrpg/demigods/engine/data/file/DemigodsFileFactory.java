package com.demigodsrpg.demigods.engine.data.file;

import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.demigodsrpg.demigods.engine.data.DataType;
import com.demigodsrpg.demigods.engine.data.IdType;
import org.bukkit.configuration.ConfigurationSection;

public class DemigodsFileFactory
{
	private DemigodsFileFactory()
	{}

	@SuppressWarnings("unchecked")
	public static DemigodsFile create(DataType type, String filePath)
	{
		return create(type.getIdType(), type.getDataClass(), type.getAbbreviation(), filePath);
	}

	public static <K, V extends DataAccess<K, V>> DemigodsFile<K, V> create(final IdType idType, final Class<V> dataClass, String abbr, String filePath)
	{
		if(IdType.VOID.equals(idType)) return null;
		return new DemigodsFile<K, V>(abbr, ".demi", filePath)
		{
			@Override
			public V create(K id, ConfigurationSection conf, String... args)
			{
				try
				{
					return dataClass.getConstructor(new Class[] { idType.getCastClass().getClass(), ConfigurationSection.class, String[].class }).newInstance(id, conf, args);
				}
				catch(Exception e)
				{
					throw new RuntimeException("Demigods can't manage it's own data for some reason.", e);
				}
			}

			@Override
			public K convertFromString(String stringId)
			{
				return idType.fromString(stringId);
			}
		};
	}
}
