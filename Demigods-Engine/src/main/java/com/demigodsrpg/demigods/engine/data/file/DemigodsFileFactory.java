package com.demigodsrpg.demigods.engine.data.file;

import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.demigodsrpg.demigods.engine.data.DataType;
import com.demigodsrpg.demigods.engine.data.IdType;
import com.demigodsrpg.demigods.engine.data.Register;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Constructor;

@SuppressWarnings("unchecked")
public class DemigodsFileFactory
{
	private DemigodsFileFactory()
	{}

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
			public V create(String stringId, ConfigurationSection conf, String... args)
			{
				try
				{
					for(Constructor<V> constructor : (Constructor<V>[]) dataClass.getConstructors())
					{
						Register dataConstructor = constructor.getAnnotation(Register.class);
						if(dataConstructor == null || !idType.equals(dataConstructor.idType())) continue;
						Class<?>[] params = constructor.getParameterTypes();
						if(params.length < 2 || !params[0].equals(idType.getCastClass()) || !params[1].equals(ConfigurationSection.class)) throw new RuntimeException("The defined constructor for a data file is invalid.");
						if(params.length == 3 && params[2].equals(String[].class)) return constructor.newInstance(idType.fromString(stringId), conf, args);
						return constructor.newInstance(idType.fromString(stringId), conf);
					}
					throw new RuntimeException("Demigods was unable to find a constructor for one of its data types.");
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
