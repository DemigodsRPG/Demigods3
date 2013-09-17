package com.censoredsoftware.demigods.player;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.battle.Battle;
import com.censoredsoftware.demigods.battle.Participant;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.language.Translation;
import com.censoredsoftware.demigods.util.Configs;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

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
		OFFENSE("Offense", "Offensive power.", "demigods.skill.offense", true), DEFENSE("Defense", "Defensive power.", "demigods.skill.defense", true), SUPPORT("Support", "Support power.", "demigods.skill.support", true), ULTIMATE("Ultimate", "Ultimate power.", "demigods.skill.ultimate", true), FAVOR_REGEN("Favor Regeneration", "Favor regeneration bonus.", "demigods.skill.favorregen", true);

		private String name;
		private String description;
		private String permission;
		private boolean isDefault;

		private Type(String name, String description, String permission, boolean isDefault)
		{
			this.name = name;
			this.description = description;
			this.permission = permission;
			this.isDefault = isDefault;
		}

		public String getName()
		{
			return name;
		}

		public String getDescription()
		{
			return description;
		}

		public String getPermission()
		{
			return permission;
		}

		public boolean isDefault()
		{
			return isDefault;
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
		Map<String, Object> map = new HashMap<String, Object>();
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

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
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
				int mvpBonus = battle.getMVPs().contains(participant) ? Configs.getSettingInt("bonuses.mvp_skill_points") : 1;
				int kills = battle.getKills(participant);
				int deaths = battle.getDeaths(participant);

				// Calculate skill points
				int skillPoints = (int) Math.ceil(Configs.getSettingDouble("multipliers.skill_points") * ((kills + 1) - (deaths / 2))) + mvpBonus;

				// Apply the points and notify the player
				character.getMeta().addSkillPoints(skillPoints);

				if(character.getOfflinePlayer().isOnline())
				{
					for(String string : Demigods.LANGUAGE.getTextBlock(Translation.Text.NOTIFICATION_SKILL_POINTS_RECEIVED))
						character.getOfflinePlayer().getPlayer().sendMessage(string.replace("{skillpoints}", "" + skillPoints));
				}
			}
		}
	}
}
