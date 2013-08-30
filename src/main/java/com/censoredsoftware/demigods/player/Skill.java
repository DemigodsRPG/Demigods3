package com.censoredsoftware.demigods.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.battle.Battle;
import com.censoredsoftware.demigods.battle.Participant;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.helper.ConfigFile;
import com.censoredsoftware.demigods.language.Translation;

public class Skill implements ConfigurationSerializable
{
	private UUID id;
	private String type;
	private int exp;
	private int level;

	public enum Type
	{
		OFFENSE, DEFENSE, STEALTH, SUPPORT, PASSIVE, ULTIMATE
	}

	public Skill()
	{}

	public Skill(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		type = conf.getString("type");
		exp = conf.getInt("exp");
		level = conf.getInt("level");
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("exp", exp);
		map.put("level", level);
		return map;
	}

	public void generateId()
	{
		id = UUID.randomUUID();
	}

	void setType(Type type)
	{
		this.type = type.toString();
	}

	void setExp(int exp)
	{
		this.exp = exp;
	}

	void setLevel(int level)
	{
		this.level = level;
	}

	public UUID getId()
	{
		return id;
	}

	public Type getType()
	{
		return Type.valueOf(type);
	}

	public int getExp()
	{
		return exp;
	}

	public int getLevel()
	{
		return (level > 0) ? level : 1;
	}

	public boolean addExp(int exp)
	{
		// Set the exp
		setExp(getExp() + exp);

		// If the exp has passed the requirement, level them
		if(getExp() > getRequiredExp()) // TODO: This will break in the event that someone applies enough exp to level multiple times
		{
			// Add a level
			addLevels(1);

			// Return true for leveling
			return true;
		}
		return false;
	}

	public void addLevels(int levels)
	{
		setLevel(getLevel() + levels);
	}

	public int getRequiredExp()
	{
		return (int) (exp + Math.pow(level + 1, 1.2));
	}

	public int getRequiredExp(int level)
	{
		return (int) (exp + Math.pow(level, 1.2));
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}

	public static class File extends ConfigFile
	{
		private static String SAVE_PATH;
		private static final String SAVE_FILE = "skill.yml";

		public File()
		{
			super(Demigods.plugin);
			SAVE_PATH = Demigods.plugin.getDataFolder() + "/data/";
		}

		@Override
		public ConcurrentHashMap<UUID, Skill> loadFromFile()
		{
			final FileConfiguration data = getData(SAVE_PATH, SAVE_FILE);
			ConcurrentHashMap<UUID, Skill> map = new ConcurrentHashMap<UUID, Skill>();
			for(String stringId : data.getKeys(false))
				map.put(UUID.fromString(stringId), new Skill(UUID.fromString(stringId), data.getConfigurationSection(stringId)));
			return map;
		}

		@Override
		public boolean saveToFile()
		{
			FileConfiguration saveFile = getData(SAVE_PATH, SAVE_FILE);
			Map<UUID, Skill> currentFile = loadFromFile();

			for(UUID id : DataManager.skills.keySet())
				if(!currentFile.keySet().contains(id) || !currentFile.get(id).equals(DataManager.skills.get(id))) saveFile.createSection(id.toString(), Util.loadSkill(id).serialize());

			for(UUID id : currentFile.keySet())
				if(!DataManager.skills.keySet().contains(id)) saveFile.set(id.toString(), null);

			return saveFile(SAVE_PATH, SAVE_FILE, saveFile);
		}
	}

	public static class Util
	{
		public static Skill createSkill(Skill.Type type)
		{
			Skill skill = new Skill();
			skill.generateId();
			skill.setType(type);
			skill.setLevel(Demigods.config.getSettingInt("character.defaults." + type.name().toLowerCase()));
			save(skill);
			return skill;
		}

		public static void save(Skill skill)
		{
			DataManager.skills.put(skill.getId(), skill);
		}

		public static Skill loadSkill(UUID id)
		{
			return DataManager.skills.get(id);
		}

		/**
		 * Processes a completed battle and distributes appropriate skill points.
		 */
		public static void processBattle(Battle battle)
		{
			// Loop through all participants and apply appropriate updates
			for(Participant participant : battle.getParticipants())
			{
				if(participant.getRelatedCharacter() == null) continue;

				// Get related character
				DCharacter character = participant.getRelatedCharacter();

				// Define all variables used for skill point calculation
				int mvpScore = battle.getMVPs().contains(participant) ? Demigods.config.getSettingInt("bonuses.mvp_skill_points") : 1;
				int kills = battle.getKills(participant);
				int deaths = battle.getDeaths(participant);

				// Calculate skill points
				int skillPoints = (int) Math.ceil(Demigods.config.getSettingDouble("multipliers.skill_points") * (mvpScore * (kills + 1) - (deaths / 2))); // TODO: We can tweak this as we go, as with most of the equations we use for stuff like this. It'll just take some experimenting to balance.

				// Apply the points and notify the player
				character.getMeta().addSkillPoints(skillPoints);

				if(character.getOfflinePlayer().isOnline())
				{
					for(String string : Demigods.language.getTextBlock(Translation.Text.NOTIFICATION_SKILL_POINTS_RECEIVED))
						character.getOfflinePlayer().getPlayer().sendMessage(string.replace("{skillpoints}", "" + skillPoints));
				}
			}
		}
	}
}
