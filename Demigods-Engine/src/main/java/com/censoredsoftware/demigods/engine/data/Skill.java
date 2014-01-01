package com.censoredsoftware.demigods.engine.data;

import com.censoredsoftware.demigods.engine.language.English;
import com.censoredsoftware.demigods.engine.util.Configs;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Skill implements ConfigurationSerializable
{
	private UUID id;
	private UUID character;
	private String type;
	private int points;
	private int level;

	public enum Type
	{
		/*
		 * OFFENSE
		 */
		OFFENSE("Offense", "Offensive power.", new Permission("demigods.skill.offense", "Allows the player to obtain the Offense skill type.", PermissionDefault.TRUE), true, true),
		/*
		 * DEFENSE
		 */
		DEFENSE("Defense", "Defensive power.", new Permission("demigods.skill.defense", "Allows the player to obtain the Defense skill type.", PermissionDefault.TRUE), true, true),
		/*
		 * SUPPORT
		 */
		SUPPORT("Support", "Support power.", new Permission("demigods.skill.support", "Allows the player to obtain the Support skill type.", PermissionDefault.TRUE), true, true),
		/*
		 * ULTIMATE
		 */
		ULTIMATE("Ultimate", "Ultimate power.", new Permission("demigods.skill.ultimate", "Allows the player to obtain the Ultimate skill type.", PermissionDefault.TRUE), true, true),
		/*
		 * PASSIVE
		 */
		PASSIVE("Passive", "Passive power.", new Permission("demigods.skill.passive", "Cannot be levelled.", PermissionDefault.TRUE), true, false),
		/*
		 * FAVOR REGEN
		 */
		FAVOR_REGEN("Favor Regen", "Favor regeneration bonus.", new Permission("demigods.skill.favorregen", "Allows the player to obtain the Favor Regeneration skill type.", PermissionDefault.TRUE), true, true);

		private String name;
		private String description;
		private Permission permission;
		private boolean isDefault, levelable;

		private Type(String name, String description, Permission permission, boolean isDefault, boolean levelable)
		{
			this.name = name;
			this.description = description;
			this.permission = permission;
			this.isDefault = isDefault;
			this.levelable = levelable;
		}

		public String getName()
		{
			return name;
		}

		public String getDescription()
		{
			return description;
		}

		public Permission getPermission()
		{
			return permission;
		}

		public boolean isDefault()
		{
			return isDefault;
		}

		public boolean isLevelable()
		{
			return levelable;
		}
	}

	public Skill()
	{}

	public Skill(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		character = UUID.fromString(conf.getString("character"));
		type = conf.getString("type");
		points = conf.getInt("points");
		level = conf.getInt("level");
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<>();
		map.put("type", type);
		map.put("points", points);
		map.put("level", level);
		map.put("character", character.toString());
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

	void setPoints(int points)
	{
		this.points = points;
	}

	void setLevel(int level)
	{
		this.level = level;
	}

	void setCharacter(DCharacter character)
	{
		this.character = character.getId();
	}

	public DCharacter getCharacter()
	{
		return DCharacter.Util.load(character);
	}

	public UUID getId()
	{
		return id;
	}

	public Type getType()
	{
		return Type.valueOf(type);
	}

	public int getPoints()
	{
		return points;
	}

	public boolean hasMetCap()
	{
		return getLevel() >= getCharacter().getMeta().getIndividualSkillCap();
	}

	public int getLevel()
	{
		return (level > 0) ? level : 1;
	}

	public void addLevels(int levels)
	{
		setLevel(getLevel() + levels);
	}

	public void addPoints(int points)
	{
		// Add points 1 at a time
		for(int i = 0; i < points; i++)
		{
			// Adding 1 point at a time
			this.points++;

			if(getPoints() >= getRequiredPointsForLevel(getLevel() + 1))
			{
				// If they've met the max level cap then stop the addition
				if(getLevel() + 1 >= getCharacter().getMeta().getIndividualSkillCap()) return;

				// Add a level
				addLevels(1);

				// Reset points
				setPoints(0);
			}
		}
	}

	public int getRequiredPoints()
	{
		return getRequiredPointsForLevel(getLevel() + 1) - getPoints();
	}

	public int getRequiredPointsForLevel(int level)
	{
		switch(getType())
		{
			case OFFENSE:
			case DEFENSE:
			case SUPPORT:
			case ULTIMATE:
			case FAVOR_REGEN:
				return (int) Math.ceil((level * Math.pow(level, 1.4)) + 5);
		}

		return -1;
	}

	public static class Util
	{
		public static Skill createSkill(DCharacter character, Skill.Type type)
		{
			Skill skill = new Skill();
			skill.generateId();
			skill.setCharacter(character);
			skill.setType(type);
			skill.setLevel(1);
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
				// Get related character
				DCharacter character = participant.getRelatedCharacter();

				// Define all variables used for skill point calculation
				int mvpBonus = battle.getMVPs().contains(participant.getRelatedCharacter()) ? Configs.getSettingInt("bonuses.mvp_skill_points") : 1;
				int kills = battle.getKills(participant);
				int deaths = battle.getDeaths(participant);

				// Calculate skill points
				int skillPoints = (int) Math.ceil(Configs.getSettingDouble("multipliers.skill_points") * ((kills + 1) - (deaths / 2))) + mvpBonus;

				// Apply the points and notify the player
				character.getMeta().addSkillPoints(skillPoints);

				if(character.getOfflinePlayer().isOnline())
				{
					for(String string : English.NOTIFICATION_SKILL_POINTS_RECEIVED.getLines())
						character.getOfflinePlayer().getPlayer().sendMessage(string.replace("{skillpoints}", "" + skillPoints));
				}
			}
		}
	}
}
