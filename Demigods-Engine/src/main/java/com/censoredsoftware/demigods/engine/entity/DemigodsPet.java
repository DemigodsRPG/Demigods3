package com.censoredsoftware.demigods.engine.entity;

import com.censoredsoftware.demigods.engine.battle.Participant;
import com.censoredsoftware.demigods.engine.data.DataAccess;
import com.censoredsoftware.demigods.engine.entity.player.DemigodsCharacter;
import com.censoredsoftware.demigods.engine.mythos.Deity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class DemigodsPet extends DataAccess<UUID, DemigodsPet> implements Participant
{
	private UUID id;
	private String entityType;
	private String animalTamer;
	private boolean PvP;
	private UUID entityUUID;
	private UUID owner;

	public DemigodsPet()
	{}

	public DemigodsPet(UUID id, ConfigurationSection conf)
	{
		this.id = id;
		entityType = conf.getString("entityType");
		if(conf.getString("animalTamer") != null) animalTamer = conf.getString("animalTamer");
		PvP = conf.getBoolean("PvP");
		entityUUID = UUID.fromString(conf.getString("entityUUID"));
		if(conf.getString("owner") != null) owner = UUID.fromString(conf.getString("owner"));
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("entityType", entityType);
		if(animalTamer != null) map.put("animalTamer", animalTamer);
		map.put("PvP", PvP);
		map.put("entityUUID", entityUUID.toString());
		if(owner != null) map.put("owner", owner.toString());
		return map;
	}

	public void generateId()
	{
		id = UUID.randomUUID();
	}

	public void remove()
	{
		getEntity().remove();
		delete();
	}

	public abstract void delete();

	public void setTamable(LivingEntity tameable)
	{
		this.entityType = tameable.getType().name();
		this.entityUUID = tameable.getUniqueId();
	}

	public void setOwnerId(String playerName, UUID owner)
	{
		this.animalTamer = playerName;
		this.owner = owner;
		save();
	}

	public boolean canPvp()
	{
		return this.PvP;
	}

	public String getEntityType()
	{
		return entityType;
	}

	public String getAnimalTamer()
	{
		return animalTamer;
	}

	public UUID getEntityUUID()
	{
		return entityUUID;
	}

	public LivingEntity getEntity()
	{
		for(World world : Bukkit.getServer().getWorlds())
		{
			for(Entity pet : world.getLivingEntities())
			{
				if(!(pet instanceof Tameable)) continue;
				if(pet.getUniqueId().equals(this.entityUUID)) return (LivingEntity) pet;
			}
		}
		delete();
		return null;
	}

	public UUID getOwnerId()
	{
		return owner;
	}

	public UUID getId()
	{
		return this.id;
	}

	public Location getCurrentLocation()
	{
		try
		{
			return getEntity().getLocation();
		}
		catch(Exception ignored)
		{}
		return null;
	}

	public void disownPet()
	{
		if(this.getEntity() == null) return;
		((Tameable) this.getEntity()).setOwner(new AnimalTamer()
		{
			@Override
			public String getName()
			{
				return "Disowned";
			}
		});
	}

	public void setOwner(DemigodsCharacter owner)
	{
		setOwnerId(owner.getPlayerName(), owner.getId());
	}

	public DemigodsCharacter getOwner()
	{
		DemigodsCharacter owner = DemigodsCharacter.get(getOwnerId());
		if(owner == null)
		{
			disownPet();
			delete();
			return null;
		}
		else if(!owner.isUsable()) return null;
		return owner;
	}

	public Deity getDeity()
	{
		if(getOwner() == null)
		{
			disownPet();
			delete();
			return null;
		}
		else if(!getOwner().isUsable()) return null;
		return getOwner().getDeity();
	}

	@Override
	public DemigodsCharacter getRelatedCharacter()
	{
		return getOwner();
	}
}
