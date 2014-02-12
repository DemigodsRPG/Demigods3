package com.demigodsrpg.demigods.engine.entity.player.attribute;

import com.demigodsrpg.demigods.engine.Demigods;
import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.demigodsrpg.demigods.engine.deity.Ability;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.location.DemigodsLocation;
import com.demigodsrpg.demigods.engine.util.Configs;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.material.MaterialData;

import java.util.*;

public class DemigodsCharacterMeta extends DataAccess<UUID, DemigodsCharacterMeta>
{
	private static boolean LEVEL_SEPERATE_SKILLS = Demigods.getMythos().levelSeperateSkills();

	private UUID id;
	private UUID character;
	private int favor;
	private int maxFavor;
	private int skillPoints;
	private Set<String> notifications;
	private Map<String, Object> binds;
	private Map<String, Object> skillData;
	private Map<String, Object> warps;
	private Map<String, Object> invites;

	public DemigodsCharacterMeta()
	{}

	public DemigodsCharacterMeta(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		favor = conf.getInt("favor");
		maxFavor = conf.getInt("maxFavor");
		skillPoints = conf.getInt("skillPoints");
		notifications = Sets.newHashSet(conf.getStringList("notifications"));
		character = UUID.fromString(conf.getString("character"));
		if(conf.getConfigurationSection("skillData") != null) skillData = conf.getConfigurationSection("skillData").getValues(false);
		if(conf.getConfigurationSection("binds") != null) binds = conf.getConfigurationSection("binds").getValues(false);
		if(conf.getConfigurationSection("warps") != null) warps = conf.getConfigurationSection("warps").getValues(false);
		if(conf.getConfigurationSection("invites") != null) invites = conf.getConfigurationSection("invites").getValues(false);
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = Maps.newHashMap();
		map.put("character", character.toString());
		map.put("favor", favor);
		map.put("maxFavor", maxFavor);
		map.put("skillPoints", skillPoints);
		map.put("notifications", Lists.newArrayList(notifications));
		map.put("binds", binds);
		map.put("skillData", skillData);
		map.put("warps", warps);
		map.put("invites", invites);
		return map;
	}

	public void generateId()
	{
		id = UUID.randomUUID();
	}

	public void setCharacter(DemigodsCharacter character)
	{
		this.character = character.getId();
	}

	public void initialize()
	{
		notifications = Sets.newHashSet();
		warps = Maps.newHashMap();
		invites = Maps.newHashMap();
		skillData = Maps.newHashMap();
		binds = Maps.newHashMap();
	}

	public UUID getId()
	{
		return id;
	}

	public DemigodsCharacter getCharacter()
	{
		return DemigodsCharacter.get(character);
	}

	public void setSkillPoints(int skillPoints)
	{
		this.skillPoints = skillPoints;
	}

	public int getSkillPoints()
	{
		return skillPoints;
	}

	public void addSkillPoints(int skillPoints)
	{
		setSkillPoints(getSkillPoints() + skillPoints);
	}

	public void subtractSkillPoints(int skillPoints)
	{
		setSkillPoints(getSkillPoints() - skillPoints);
	}

	public void addNotification(Notification notification)
	{
		getNotifications().add(notification.getId().toString());
		save();
	}

	public void removeNotification(Notification notification)
	{
		getNotifications().remove(notification.getId().toString());
		save();
	}

	public Set<String> getNotifications()
	{
		if(this.notifications == null) this.notifications = Sets.newHashSet();
		return this.notifications;
	}

	public void clearNotifications()
	{
		notifications.clear();
	}

	public boolean hasNotifications()
	{
		return !notifications.isEmpty();
	}

	public void addWarp(String name, Location location)
	{
		warps.put(name.toLowerCase(), DemigodsLocation.track(location).getId().toString());
		save();
	}

	public void removeWarp(String name)
	{
		getWarps().remove(name.toLowerCase());
		save();
	}

	public Map<String, Object> getWarps()
	{
		if(this.warps == null) this.warps = Maps.newHashMap();
		return this.warps;
	}

	public void clearWarps()
	{
		getWarps().clear();
	}

	public boolean hasWarps()
	{
		return !this.warps.isEmpty();
	}

	public void addInvite(String name, Location location)
	{
		getInvites().put(name.toLowerCase(), DemigodsLocation.track(location).getId().toString());
		save();
	}

	public void removeInvite(String name)
	{
		getInvites().remove(name.toLowerCase());
		save();
	}

	public Map<String, Object> getInvites()
	{
		if(this.invites == null) this.invites = Maps.newHashMap();
		return this.invites;
	}

	public void clearInvites()
	{
		invites.clear();
	}

	public boolean hasInvites()
	{
		return !this.invites.isEmpty();
	}

	public void resetSkills()
	{
		getRawSkills().clear();
		for(Skill.Type type : Skill.Type.values())
			if(type.isDefault()) addSkill(Skill.create(getCharacter(), type));
	}

	public void cleanSkills()
	{
		List<String> toRemove = Lists.newArrayList();

		// Locate obsolete skills
		for(String skillName : getRawSkills().keySet())
		{
			try
			{
				// Attempt to find the value of the skill name
				Skill.Type.valueOf(skillName.toUpperCase());
			}
			catch(Exception ignored)
			{
				// There was an error. Catch it and remove the skill.
				toRemove.add(skillName);
			}
		}

		// Remove the obsolete skills
		for(String skillName : toRemove)
			getRawSkills().remove(skillName);
	}

	public void addSkill(Skill skill)
	{
		if(!getRawSkills().containsKey(skill.getType().toString())) getRawSkills().put(skill.getType().toString(), skill.getId().toString());
		save();
	}

	public Skill getSkill(Skill.Type type)
	{
		if(getRawSkills().containsKey(type.toString())) return Skill.get(UUID.fromString(getRawSkills().get(type.toString()).toString()));
		return null;
	}

	public Map<String, Object> getRawSkills()
	{
		if(skillData == null) skillData = Maps.newHashMap();
		return skillData;
	}

	public Collection<Skill> getSkills()
	{
		return Collections2.transform(getRawSkills().values(), new Function<Object, Skill>()
		{
			@Override
			public Skill apply(Object obj)
			{
				return Skill.get(UUID.fromString(obj.toString()));
			}
		});
	}

	public Collection<Skill> getLevelableSkills()
	{
		return Collections2.filter(getSkills(), new Predicate<Skill>()
		{
			@Override
			public boolean apply(Skill skill)
			{
				return skill.getType().isLevelable();
			}
		});
	}

	public boolean checkBound(String abilityName, MaterialData material)
	{
		return getBinds().containsKey(abilityName) && binds.get(abilityName).equals(material.getItemType().name());
	}

	public boolean isBound(Ability ability)
	{
		return getBinds().containsKey(ability.getName());
	}

	public boolean isBound(Material material)
	{
		return getBinds().containsValue(material.name());
	}

	public void setBind(Ability ability, Material material)
	{
		getBinds().put(ability.getName(), material.name());
	}

	public Map<String, Object> getBinds()
	{
		if(binds == null) binds = Maps.newHashMap();
		return this.binds;
	}

	public void removeBind(Ability ability)
	{
		getBinds().remove(ability.getName());
	}

	public void removeBind(Material material)
	{
		if(getBinds().containsValue(material.name()))
		{
			String toRemove = null;
			for(Map.Entry<String, Object> entry : getBinds().entrySet())
			{
				toRemove = entry.getValue().equals(material.name()) ? entry.getKey() : null;
			}
			getBinds().remove(toRemove);
		}
	}

	public int getIndividualSkillCap()
	{
		int total = 0;
		for(Skill skill : getSkills())
			total += skill.getLevel();
		return getOverallSkillCap() - total;
	}

	public int getOverallSkillCap()
	{
		// This is done this way so it can easily be manipulated later
		return Configs.getSettingInt("caps.skills");
	}

	public int getAscensions()
	{
		if(LEVEL_SEPERATE_SKILLS)
		{
			double total = 0.0;

			for(Skill skill : getSkills())
				total += skill.getLevel();

			return (int) Math.ceil(total / getSkills().size());
		}
		return (int) Math.ceil(getSkillPoints() / 500); // TODO Balance this.
	}

	public Integer getFavor()
	{
		return favor;
	}

	public void setFavor(int amount)
	{
		favor = amount;
		save();
	}

	public void addFavor(int amount)
	{
		if((favor + amount) > maxFavor) favor = maxFavor;
		else favor += amount;
		save();
	}

	public void subtractFavor(int amount)
	{
		if((favor - amount) < 0) favor = 0;
		else favor -= amount;
		save();
	}

	public Integer getMaxFavor()
	{
		return maxFavor;
	}

	public void addMaxFavor(int amount)
	{
		if((maxFavor + amount) > Configs.getSettingInt("caps.favor")) maxFavor = Configs.getSettingInt("caps.favor");
		else maxFavor += amount;
		save();
	}

	public void setMaxFavor(int amount)
	{
		if(amount < 0) maxFavor = 0;
		if(amount > Configs.getSettingInt("caps.favor")) maxFavor = Configs.getSettingInt("caps.favor");
		else maxFavor = amount;
		save();
	}

	public void subtractMaxFavor(int amount)
	{
		setMaxFavor(getMaxFavor() - amount);
	}

	// -- STATIC GETTERS/SETTERS -- //

	private static final DataAccess<UUID, DemigodsCharacterMeta> DATA_ACCESS = new DemigodsCharacterMeta();

	public static DemigodsCharacterMeta get(UUID id)
	{
		return DATA_ACCESS.getDirect(id);
	}

	public static Collection<DemigodsCharacterMeta> all()
	{
		return DATA_ACCESS.getAll();
	}

	public static DemigodsCharacterMeta create(DemigodsCharacter character)
	{
		DemigodsCharacterMeta charMeta = new DemigodsCharacterMeta();
		charMeta.initialize();
		charMeta.setCharacter(character);
		charMeta.generateId();
		charMeta.setFavor(Configs.getSettingInt("character.defaults.favor"));
		charMeta.setMaxFavor(Configs.getSettingInt("character.defaults.max_favor"));
		charMeta.resetSkills();
		charMeta.save();
		return charMeta;
	}
}
