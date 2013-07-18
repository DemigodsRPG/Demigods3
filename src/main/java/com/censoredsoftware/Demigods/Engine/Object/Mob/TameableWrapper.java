package com.censoredsoftware.Demigods.Engine.Object.Mob;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Object.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;
import com.google.common.collect.Sets;

@Model
public class TameableWrapper
{
	@Id
	private Long Id;
	@Attribute
	@Indexed
	private String entityType;
	@Attribute
	@Indexed
	private String animalTamer;
	@Attribute
	@Indexed
	private String UUID;
	@Reference
	@Indexed
	private PlayerCharacter owner;

	public static TameableWrapper create(LivingEntity tameable, PlayerCharacter owner)
	{
		if(!(tameable instanceof Tameable)) throw new IllegalArgumentException("LivingEntity not tamable.");
		TameableWrapper wrapper = new TameableWrapper();
		wrapper.setTamable(tameable);
		wrapper.setOwner(owner);
		wrapper.save();
		return wrapper;
	}

	public void save()
	{
		JOhm.save(this);
	}

	public static TameableWrapper load(Long id)
	{
		return JOhm.get(TameableWrapper.class, id);
	}

	public static List<TameableWrapper> findByType(EntityType type)
	{
		return JOhm.find(TameableWrapper.class, "entityType", type.getName());
	}

	public static List<TameableWrapper> findByTamer(String animalTamer)
	{
		return JOhm.find(TameableWrapper.class, "animalTamer", animalTamer);
	}

	public static Set<TameableWrapper> loadAll()
	{
		try
		{
			return JOhm.getAll(TameableWrapper.class);
		}
		catch(Exception e)
		{
			return Sets.newHashSet();
		}
	}

	public void remove()
	{
		getLivingEntity().remove();
		delete();
	}

	public void delete()
	{
		JOhm.delete(TameableWrapper.class, this.Id);
	}

	public void setTamable(LivingEntity tameable)
	{
		if(!(tameable instanceof Tameable)) throw new IllegalArgumentException("LivingEntity not tamable.");
		this.entityType = tameable.getType().getName();
		this.UUID = tameable.getUniqueId().toString();
	}

	public void setOwner(PlayerCharacter owner)
	{
		this.animalTamer = owner.getName();
		this.owner = owner;
	}

	public static TameableWrapper getTameable(LivingEntity tameable)
	{
		if(!(tameable instanceof Tameable)) throw new IllegalArgumentException("LivingEntity not tamable.");
		try
		{
			List<TameableWrapper> tracking = JOhm.find(TameableWrapper.class, "UUID", tameable.getUniqueId().toString());
			return tracking.get(0);
		}
		catch(Exception ignored)
		{}
		return null;
	}

	public LivingEntity getLivingEntity()
	{
		for(World world : Bukkit.getServer().getWorlds())
		{
			for(LivingEntity living : world.getLivingEntities())
			{
				if(!(living instanceof Tameable)) continue;
				if(living.getUniqueId().toString().equals(this.UUID)) return living;
			}
		}
		return null;
	}

	public PlayerCharacter getOwner()
	{
		return this.owner;
	}

	public Deity getDeity()
	{
		return this.owner.getDeity();
	}

	public static void disownPets(String animalTamer)
	{
		for(TameableWrapper wrapper : findByTamer(animalTamer))
		{
			((Tameable) wrapper.getLivingEntity()).setOwner(new AnimalTamer()
			{
				@Override
				public String getName()
				{
					return "Disowned";
				}
			});
		}
	}

	public static void reownPets(AnimalTamer tamer)
	{
		for(TameableWrapper wrapper : findByTamer(tamer.getName()))
		{
			((Tameable) wrapper.getLivingEntity()).setOwner(tamer);
		}
	}
}
