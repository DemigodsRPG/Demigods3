package com.demigodsrpg.demigods.engine.data.file;

import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.demigodsrpg.demigods.engine.data.DataType;
import com.demigodsrpg.demigods.engine.data.IdType;
import com.demigodsrpg.demigods.engine.data.Register;
import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Constructor;

/**
 * Factory for constructing DemigodsFiles in a safe and generic way.
 */
@SuppressWarnings("unchecked")
public class DemigodsFileFactory
{
	/**
	 * Private constructor for this factory.
	 */
	private DemigodsFileFactory()
	{
	}

	/**
	 * Create a Demigods File from just the DataType and file path.
	 *
	 * @param type     The DataType this file will be persisting.
	 * @param filePath The path to the file directory.
	 * @return A new DemigodsFile object.
	 */
	public static DemigodsFile create(DataType type, String filePath)
	{
		return create(type.getIdType(), type.getDataClass(), type.getAbbreviation(), filePath);
	}

	/**
	 * Create a Demigods File from the IdType, data class, abbreviation, and file path.
	 *
	 * @param idType    The IdType this file will be using.
	 * @param dataClass The class this file will be persisting data for.
	 * @param abbr      The abbreviation of the data type.
	 * @param filePath  The path to the file directory.
	 * @return A new DemigodsFile object.
	 */
	public static <K extends Comparable, V extends DataAccess<K, V>> DemigodsFile<K, V> create(final IdType idType, final Class<V> dataClass, String abbr, String filePath)
	{
		// Check for void type.
		if(IdType.VOID.equals(idType)) return null;

		// Construct a new Demigods File from the abbreviation, file extension, and file directory path.
		return new DemigodsFile<K, V>(abbr, ".demi", filePath)
		{
			// Overridden method to create an new data object from the file data.
			@Override
			public V valueFromData(String stringId, ConfigurationSection conf)
			{
				try
				{
					// Look over all constructors in the data class.
					for(Constructor<V> constructor : (Constructor<V>[]) dataClass.getConstructors())
					{
						// Attempt to find a registered constructor.
						Register dataConstructor = constructor.getAnnotation(Register.class);

						// Is the constructor suitable for use?
						if(dataConstructor == null || !idType.equals(dataConstructor.idType())) continue;

						// So far so good, now we double check the params.
						Class<?>[] params = constructor.getParameterTypes();
						if(params.length < 2 || !params[0].equals(idType.getCastClass()) || !params[1].equals(ConfigurationSection.class))
							// No good.
							throw new RuntimeException("The defined constructor for a data file is invalid.");

						// Everything looks perfect so far. Last thing to do is construct a new instance.
						return constructor.newInstance(keyFromString(stringId), conf);
					}
					throw new RuntimeException("Demigods was unable to find a constructor for one of its data types.");
				}
				catch(Exception e)
				{
					throw new RuntimeException("Demigods can't manage it's own data for some reason.", e);
				}
			}

			@Override
			public K keyFromString(String stringId)
			{
				return idType.fromString(stringId);
			}
		};
	}
}
