package com.censoredsoftware.Demigods.Engine.Object.General;

import java.util.Set;

import org.bukkit.Location;

import redis.clients.johm.Attribute;
import redis.clients.johm.Id;
import redis.clients.johm.JOhm;
import redis.clients.johm.Model;

import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;

@Model
public class DemigodsWarp
{
	@Id
	private Long id;
	@Attribute
	private String name;
	@Attribute
	private long owner;
	@Attribute
	private long location;

	void setName(String name)
	{
		this.name = name;
	}

	void setOwner(long id)
	{
		this.owner = id;
	}

	void setLocation(Location location)
	{
		this.location = DemigodsLocation.create(location).getId();
	}

	public static DemigodsWarp create(String name, PlayerCharacter character, Location location)
	{
		DemigodsWarp warp = new DemigodsWarp();
		warp.setName(name);
		warp.setOwner(character.getId());
		warp.setLocation(location);
		DemigodsWarp.save(warp);
		return warp;
	}

	public String getName()
	{
		return this.name;
	}

	public PlayerCharacter getOwner()
	{
		return PlayerCharacter.load(this.owner);
	}

	public Location getLocation()
	{
		return DemigodsLocation.load(this.location).toLocation();
	}

	public Long getId()
	{
		return this.id;
	}

	public static void save(DemigodsWarp warp)
	{
		JOhm.save(warp);
	}

	public static DemigodsWarp load(long id)
	{
		return JOhm.get(DemigodsWarp.class, id);
	}

	public static Set<DemigodsWarp> loadAll()
	{
		return JOhm.getAll(DemigodsWarp.class);
	}
}
