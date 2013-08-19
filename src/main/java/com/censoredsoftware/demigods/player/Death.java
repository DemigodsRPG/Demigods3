package com.censoredsoftware.demigods.player;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.helper.ConfigFile;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Death implements ConfigurationSerializable
{
	private UUID id;
	private long deathTime;
	private UUID killed, attacking;

	public Death(DCharacter killed)
	{
		this.deathTime = System.currentTimeMillis();
		this.id = UUID.randomUUID();
		this.killed = killed.getId();
		Util.save(this);
	}

	public Death(DCharacter killed, DCharacter attacking)
	{
		this.deathTime = System.currentTimeMillis();
		this.id = UUID.randomUUID();
		this.killed = killed.getId();
		this.attacking = attacking.getId();
		Util.save(this);
	}

	public Death(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		this.deathTime = conf.getLong("deathTime");
		this.killed = UUID.fromString(conf.getString("killed"));
		if(conf.isString("attacking")) this.attacking = UUID.fromString(conf.getString("attacking"));
	}

	@Override
	public Map<String, Object> serialize()
	{
		return new HashMap<String, Object>()
		{
			{
				put("deathTime", deathTime);
				put("killed", killed.toString());
				if(attacking != null) put("attacking", attacking.toString());
			}
		};
	}

	public UUID getId()
	{
		return id;
	}

	public long getDeathTime()
	{
		return getDeathTime();
	}

	public UUID getKilled()
	{
		return killed;
	}

	public UUID getAttacking()
	{
		return attacking;
	}

	public static class File extends ConfigFile
	{
		private static String SAVE_PATH;
		private static final String SAVE_FILE = "deaths.yml";

		public File()
		{
			super(Demigods.plugin);
			SAVE_PATH = Demigods.plugin.getDataFolder() + "/data/";
		}

		@Override
		public ConcurrentHashMap<UUID, Death> loadFromFile()
		{
			final FileConfiguration data = getData(SAVE_PATH, SAVE_FILE);
			return new ConcurrentHashMap<UUID, Death>()
			{
				{
					for(String stringId : data.getKeys(false))
						put(UUID.fromString(stringId), new Death(UUID.fromString(stringId), data.getConfigurationSection(stringId)));
				}
			};
		}

		@Override
		public boolean saveToFile()
		{
			FileConfiguration saveFile = getData(SAVE_PATH, SAVE_FILE);
			Map<UUID, Death> currentFile = loadFromFile();

			for(UUID id : DataManager.deaths.keySet())
				if(!currentFile.keySet().contains(id) || !currentFile.get(id).equals(DataManager.deaths.get(id))) saveFile.createSection(id.toString(), Util.load(id).serialize());

			for(UUID id : currentFile.keySet())
				if(!DataManager.deaths.keySet().contains(id)) saveFile.set(id.toString(), null);

			return saveFile(SAVE_PATH, SAVE_FILE, saveFile);
		}
	}

	public static class Util
	{
		public static Death load(UUID id)
		{
			return DataManager.deaths.get(id);
		}

		public static void save(Death death)
		{
			DataManager.deaths.put(death.id, death);
		}

		public static void delete(UUID id)
		{
			DataManager.deaths.remove(id);
		}

		public static Set<Death> getRecentDeaths(int seconds)
		{
			final long time = System.currentTimeMillis() - (seconds * 1000);
			return Sets.newHashSet(Iterables.filter(Iterables.concat(Collections2.transform(DCharacter.Util.getOnlineCharacters(), new Function<DCharacter, Collection<Death>>()
			{
				@Override
				public Collection<Death> apply(DCharacter character)
				{
					try
					{
						return character.getDeaths();
					}
					catch(Exception ignored)
					{}
					return null;
				}
			})), new Predicate<Death>()
			{
				@Override
				public boolean apply(Death death)
				{
					return death.getDeathTime() >= time;
				}
			}));
		}

		public static Collection<Death> getRecentDeaths(DCharacter character, int seconds)
		{
			final long time = System.currentTimeMillis() - (seconds * 1000);
			return Collections2.filter(character.getDeaths(), new Predicate<Death>()
			{
				@Override
				public boolean apply(Death death)
				{
					return death.getDeathTime() >= time;
				}
			});
		}
	}
}
