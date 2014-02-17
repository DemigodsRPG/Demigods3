package com.demigodsrpg.demigods.engine.data;

import com.censoredsoftware.library.serializable.yaml.SimpleYamlFile;
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

@SuppressWarnings("unchecked")
public class DemigodsWorld extends SimpleYamlFile<DemigodsWorld>
{
	private final String worldName, worldDataFolder;
	private final DemigodsWorldFile<UUID, DemigodsLocation> location;
	private final DemigodsWorldFile<UUID, DemigodsStructure> structure;

	DemigodsWorld(final String worldName, String worldDataFolder)
	{
		this.worldName = worldName;
		this.worldDataFolder = worldDataFolder;

		location = new DemigodsWorldFile<UUID, DemigodsLocation>("l", ".demi", this.worldDataFolder)
		{
			@Override
			public DemigodsLocation valueFromData(UUID uuid, ConfigurationSection conf)
			{
				return new DemigodsLocation(uuid, conf, worldName);
			}

			@Override
			public UUID keyFromString(String stringId)
			{
				return UUID.fromString(stringId);
			}
		};

		structure = new DemigodsWorldFile<UUID, DemigodsStructure>("s", ".demi", this.worldDataFolder)
		{
			@Override
			public DemigodsStructure valueFromData(UUID uuid, ConfigurationSection conf)
			{
				return new DemigodsStructure(uuid, conf, worldName);
			}

			@Override
			public UUID keyFromString(String stringId)
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

	public DemigodsWorld valueFromData(ConfigurationSection conf)
	{
		// TODO
		return this;
	}

	@Override
	public String getDirectoryPath()
	{
		return worldDataFolder;
	}

	@Override
	public String getFullFileName()
	{
		return "b.demi";
	}

	@Override
	public void loadDataFromFile() // TODO Put this in the parent class.
	{
		getCurrentFileData();
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

	public void loadData()
	{
		loadDataFromFile();
		location.loadDataFromFile();
		structure.loadDataFromFile();
	}

	public boolean saveData()
	{
		location.saveDataToFile();
		structure.saveDataToFile();
		return saveDataToFile();
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
