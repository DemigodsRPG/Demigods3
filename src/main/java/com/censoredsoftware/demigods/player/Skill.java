package com.censoredsoftware.demigods.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.censoredsoftware.demigods.Demigods;
import com.censoredsoftware.demigods.battle.Battle;
import com.censoredsoftware.demigods.battle.Participant;
import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.language.Translation;
import com.censoredsoftware.demigods.util.Configs;

public class Skill implements ConfigurationSerializable
{
	private UUID id;
	private String type;
	private int points;
	private int level;

	public enum Type
	{
		OFFENSE(true), DEFENSE(true), STEALTH(true), SUPPORT(true), PASSIVE(true), ULTIMATE(true), FAVOR_REGEN(false);

		private boolean isDefault;

		private Type(boolean isDefault)
		{
			this.isDefault = isDefault;
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

	public int getLevel()
	{
		return (level > 0) ? level : 1;
	}

	public void addLevels(int levels)
	{
		setLevel(getLevel() + levels);
	}

	public boolean addPoints(int points)
	{
		// Set variable to check leveling later
		boolean didLevel = false;

		// Add points 1 at a time
		for(int i = 0; i < points; i++) // TODO: This is inefficient but it gets the job done easily. :)
		{
			// Adding 1 point at a time
			this.points++;

			if(getPoints() >= getRequiredPointsForLevel(getLevel() + 1))
			{
				// Add a level
				addLevels(1);

				// Reset points
				setPoints(0);

				// Return true for leveling
				didLevel = true;
			}
		}

		// Return if they leveled or not
		return didLevel;
	}

	public int getRequiredPoints()
	{
		return getRequiredPointsForLevel(getLevel() + 1) - getPoints();
	}

	public int getRequiredPointsForLevel(int level)
	{
		switch(getType())
		{
			case ULTIMATE:
			case PASSIVE:
			case STEALTH:
			case OFFENSE:
			case DEFENSE:
			case SUPPORT:
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
		public static Skill createSkill(Skill.Type type)
		{
			Skill skill = new Skill();
			skill.generateId();
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
				if(participant.getRelatedCharacter() == null) continue;

				// Get related character
				DCharacter character = participant.getRelatedCharacter();

				// Define all variables used for skill point calculation
				int mvpScore = battle.getMVPs().contains(participant) ? Configs.getSettingInt("bonuses.mvp_skill_points") : 1;
				int kills = battle.getKills(participant);
				int deaths = battle.getDeaths(participant);

				// Calculate skill points
				int skillPoints = (int) Math.ceil(Configs.getSettingDouble("multipliers.skill_points") * (mvpScore * (kills + 1) - (deaths / 2))); // TODO: We can tweak this as we go, as with most of the equations we use for stuff like this. It'll just take some experimenting to balance.

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
