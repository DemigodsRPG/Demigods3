package com.censoredsoftware.demigods.exclusive.data;

import com.censoredsoftware.demigods.engine.data.Data;
import org.bukkit.configuration.ConfigurationSection;

public class ExData
{
	public static final Data.DemigodsFile<String, ExDPlayer> EXCLUSIVE_PLAYER = new Data.DemigodsFile<String, ExDPlayer>("exclusiveplayers.yml")
	{
		@Override
		public ExDPlayer create(String mojangAccount, ConfigurationSection conf)
		{
			return new ExDPlayer(mojangAccount, conf);
		}

		@Override
		public String convertFromString(String stringId)
		{
			return stringId;
		}
	};

	public static Data.DemigodsFile[] values()
	{
		return new Data.DemigodsFile[] { EXCLUSIVE_PLAYER };
	}

	public static void init()
	{
		for(Data.DemigodsFile data : values())
			data.loadToData();
	}

	private ExData()
	{}

	public static void save()
	{
		for(Data.DemigodsFile data : values())
			data.saveToFile();
	}

	public static void flushData()
	{
		// Clear the data
		for(Data.DemigodsFile data : values())
			data.clear();

		// TODO Does not flush world data... should it?

		save();
	}
}