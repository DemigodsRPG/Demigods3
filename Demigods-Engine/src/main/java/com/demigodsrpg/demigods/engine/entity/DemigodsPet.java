package com.demigodsrpg.demigods.engine.entity;

import com.demigodsrpg.demigods.engine.battle.Participant;
import com.demigodsrpg.demigods.engine.data.DataAccess;
import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DemigodsPet extends DataAccess<UUID, DemigodsPet> implements Participant
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

	@Override
	public void remove()
	{
		getEntity().remove();
		super.remove();
	}

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
		remove();
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
			remove();
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
			remove();
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

	private static final DataAccess<UUID, DemigodsPet> DATA_ACCESS = new DemigodsPet();

	public static DemigodsPet get(UUID id)
	{
		return DATA_ACCESS.getDirect(id);
	}

	public static Collection<DemigodsPet> all()
	{
		return DATA_ACCESS.getAll();
	}

	public static DemigodsPet create(Tameable tameable, DemigodsCharacter owner)
	{
		if(owner == null) throw new IllegalArgumentException("Owner cannot be null.");
		if(!(tameable instanceof LivingEntity)) throw new IllegalArgumentException("Pet must be alive.");
		DemigodsPet wrapper = new DemigodsPet();
		wrapper.generateId();
		wrapper.setTamable((LivingEntity) tameable);
		wrapper.setOwner(owner);
		wrapper.save();
		return wrapper;
	}

	public static Collection<DemigodsPet> findByType(final EntityType type)
	{
		return Collections2.filter(all(), new Predicate<DemigodsPet>()
		{
			@Override
			public boolean apply(DemigodsPet pet)
			{
				return pet.getEntityType().equals(type.name());
			}
		});
	}

	public static Collection<DemigodsPet> findByTamer(final String animalTamer)
	{
		return Collections2.filter(all(), new Predicate<DemigodsPet>()
		{
			@Override
			public boolean apply(DemigodsPet pet)
			{
				return pet.getAnimalTamer().equals(animalTamer);
			}
		});
	}

	public static Collection<DemigodsPet> findByUUID(final UUID uniqueId)
	{
		return Collections2.filter(all(), new Predicate<DemigodsPet>()
		{
			@Override
			public boolean apply(DemigodsPet pet)
			{
				return pet.getEntityUUID().equals(uniqueId);
			}
		});
	}

	public static Collection<DemigodsPet> findByOwner(final UUID ownerId)
	{
		return Collections2.filter(all(), new Predicate<DemigodsPet>()
		{
			@Override
			public boolean apply(DemigodsPet pet)
			{
				return pet.getOwnerId().equals(ownerId);
			}
		});
	}

	public static DemigodsPet getPet(LivingEntity tameable)
	{
		if(!(tameable instanceof Tameable)) throw new IllegalArgumentException("LivingEntity not tamable.");
		return Iterables.getFirst(findByUUID(tameable.getUniqueId()), null);
	}

	public static void disownPets(String animalTamer)
	{
		for(DemigodsPet wrapper : findByTamer(animalTamer))
		{
			if(wrapper.getEntity() == null) continue;
			((Tameable) wrapper.getEntity()).setOwner(new AnimalTamer()
			{
				@Override
				public String getName()
				{
					return "Disowned";
				}
			});
		}
	}

	public static void reownPets(AnimalTamer tamer, DemigodsCharacter character)
	{
		for(DemigodsPet wrapper : findByTamer(character.getName()))
			if(wrapper.getEntity() != null) ((Tameable) wrapper.getEntity()).setOwner(tamer);
	}
}
