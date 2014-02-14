package com.demigodsrpg.demigods.engine.data;

import com.censoredsoftware.censoredlib.helper.ConfigFile2;
import com.demigodsrpg.demigods.engine.data.file.DemigodsWorldFile;
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
	private final DemigodsWorldFile<UUID, DemigodsLocation> location;
	private final DemigodsWorldFile<UUID, DemigodsStructure> structure;

	DemigodsWorld(final String worldName, String worldFolder)
	{
		this.worldName = worldName;
		this.worldFolder = worldFolder + "/demigods/";

		location = new DemigodsWorldFile<UUID, DemigodsLocation>("l", ".demi", this.worldFolder)
		{
			@Override
			public DemigodsLocation create(UUID uuid, ConfigurationSection conf, String... args)
			{
				return new DemigodsLocation(uuid, conf, worldName);
			}

			@Override
			public UUID convertFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}
		};

		structure = new DemigodsWorldFile<UUID, DemigodsStructure>("s", ".demi", this.worldFolder)
		{
			@Override
			public DemigodsStructure create(UUID uuid, ConfigurationSection conf, String... args)
			{
				return new DemigodsStructure(uuid, conf, worldName);
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

	DemigodsWorldFile<UUID, DemigodsLocation> locations()
	{
		return location;
	}

	DemigodsWorldFile<UUID, DemigodsStructure> structures()
	{
		return structure;
	}
}
