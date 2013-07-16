package com.censoredsoftware.Demigods.Engine.Object.Mob;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;
import com.google.common.collect.Sets;

// TODO Waiting on Horse API.

@Model
public class HorseWrapper
{
	@Id
	private Long Id;
	@Attribute
	@Indexed
	private String UUID;
	@Reference
	@Indexed
	private PlayerCharacter owner;

	public static HorseWrapper create(Horse horse, PlayerCharacter owner)
	{
		HorseWrapper wrapper = new HorseWrapper();
		wrapper.setHorse(horse);
		wrapper.setOwner(owner);
		wrapper.save();
		return wrapper;
	}

	public void save()
	{
		JOhm.save(this);
	}

	public static HorseWrapper load(Long id)
	{
		return JOhm.get(HorseWrapper.class, id);
	}

	public static Set<HorseWrapper> loadAll()
	{
		try
		{
			return JOhm.getAll(HorseWrapper.class);
		}
		catch(Exception e)
		{
			return Sets.newHashSet();
		}
	}

	public void remove()
	{
		getHorse().remove();
		delete();
	}

	public void delete()
	{
		JOhm.delete(HorseWrapper.class, this.Id);
	}

	public void setHorse(Horse horse)
	{
		this.UUID = horse.getUniqueId().toString();
	}

	public void setOwner(PlayerCharacter owner)
	{
		this.owner = owner;
	}

	public static HorseWrapper getHorse(Horse horse)
	{
		try
		{
			List<HorseWrapper> tracking = JOhm.find(HorseWrapper.class, "UUID", horse.getUniqueId().toString());
			return tracking.get(0);
		}
		catch(Exception ignored)
		{}
		return null;
	}

	public Horse getHorse()
	{
		for(World world : Bukkit.getServer().getWorlds())
		{
			for(LivingEntity living : world.getLivingEntities())
			{
				if(!(living instanceof Horse)) continue;
				if(living.getUniqueId().toString().equals(this.UUID)) return (Horse) living;
			}
		}
		return null;
	}

	public PlayerCharacter getOwner()
	{
		return this.owner;
	}
}
