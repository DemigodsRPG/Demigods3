package com.demigodsrpg.demigods.engine.data;

import com.censoredsoftware.censoredlib.helper.ConfigFile2;
import com.demigodsrpg.demigods.engine.data.file.DemigodsFile;
import com.demigodsrpg.demigods.engine.location.DemigodsLocation;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructure;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.UUID;

public class DemigodsWorld extends ConfigFile2
{
	private final String worldName, worldFolder;
	private final DemigodsFile<UUID, DemigodsLocation> location;
	private final DemigodsFile<UUID, DemigodsStructure> structure;

	DemigodsWorld(final String worldName, String worldFolder)
	{
		this.worldName = worldName;
		this.worldFolder = worldFolder + "/demigods/";

		location = new DemigodsFile<UUID, DemigodsLocation>("l.demi", this.worldFolder)
		{
			@Override
			public DemigodsLocation create(UUID uuid, ConfigurationSection conf)
			{
				return new DemigodsLocation(worldName, uuid, conf);
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}
		};

		structure = new DemigodsFile<UUID, DemigodsStructure>("s.demi", this.worldFolder)
		{
			@Override
			public DemigodsStructure create(UUID uuid, ConfigurationSection conf)
			{
				return new DemigodsStructure(worldName, uuid, conf);
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}
		};
	}

	public World getBukkitWorld()
	{
		return Bukkit.getWorld(worldName);
	}

	public String getName()
	{
		return worldName;
	}

	public DemigodsWorld unserialize(ConfigurationSection conf)
	{
		// TODO
		return this;
	}

	@Override
	public String getSavePath()
	{
		return worldFolder;
	}

	@Override
	public String getSaveFile()
	{
		return "b.demi";
	}

	public Map<String, Object> serialize()
	{
		// TODO
		return Maps.newHashMap();
	}

	// -- UTILITY METHODS -- //

	public static DemigodsWorld of(World world)
	{
		return WorldDataManager.getWorld(world.getName());
	}

	public static DemigodsWorld of(Location location)
	{
		return of(location.getWorld());
	}

	// -- DATA TYPES -- //

	void loadData()
	{
		loadFromFile();
		location.loadToData();
		structure.loadToData();
	}

	void saveData()
	{
		saveToFile();
		location.saveToFile();
		structure.saveToFile();
	}

	DemigodsFile<UUID, DemigodsLocation> locations()
	{
		return location;
	}

	DemigodsFile<UUID, DemigodsStructure> structures()
	{
		return structure;
	}
}
