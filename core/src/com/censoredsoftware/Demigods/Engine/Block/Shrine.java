package com.censoredsoftware.Demigods.Engine.Block;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.API.CharacterAPI;
import com.censoredsoftware.Demigods.API.DeityAPI;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.DemigodsData;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedBlock;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedLocation;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedModelFactory;
import com.google.common.base.Objects;

@Model
public class Shrine
{
	@Id
	private Long id;
	@Attribute
	@Indexed
	private boolean active;
	@Attribute
	@Indexed
	private long owner;
	@Attribute
	@Indexed
	private String deity;
	@Reference
	@Indexed
	private TrackedLocation location;
	@Reference
	@Indexed
	private TrackedBlock block;

	public static void save(Shrine shrine)
	{
		DemigodsData.jOhm.save(shrine);
	}

	public void delete()
	{
		DemigodsData.jOhm.delete(Shrine.class, getId());
	}

	public static Shrine load(Long id)
	{
		return DemigodsData.jOhm.get(Shrine.class, id);
	}

	public static Set<Shrine> loadAll()
	{
		return DemigodsData.jOhm.getAll(Shrine.class);
	}

	void setLocation(Location location)
	{
		this.location = TrackedModelFactory.createTrackedLocation(location);
	}

	void setOwner(PlayerCharacter character)
	{
		this.owner = character.getId();
	}

	void setDeity(Deity deity)
	{
		this.deity = deity.getInfo().getName();
	}

	public void setActive(boolean option)
	{
		this.active = option;
	}

	public synchronized void remove()
	{
		Location location = this.location.toLocation();
		location.getBlock().setType(Material.AIR);

		Location locToMatch = location.add(0.5, 1.0, 0.5);
		for(Entity entity : location.getWorld().getEntities())
		{
			if(entity.getLocation().equals(locToMatch))
			{
				entity.remove();
			}
		}
	}

	public Long getId()
	{
		return this.id;
	}

	public PlayerCharacter getOwner()
	{
		return CharacterAPI.getChar(this.owner);
	}

	public Deity getDeity()
	{
		return DeityAPI.getDeity(this.deity);
	}

	public Location getLocation()
	{
		return this.location.toLocation();
	}

	public boolean isActive()
	{
		return this.active;
	}

	public synchronized void generate()
	{
		Location location = this.getLocation();

		// Remove entity to be safe
		Location locToMatch = this.getLocation().add(0.5, 1.0, 0.5);
		for(Entity entity : location.getWorld().getEntities())
		{
			if(entity.getLocation().equals(locToMatch))
			{
				entity.remove();
			}
		}

		// TODO: If we decide to change what Shrines look like this is where it will be.
		// Set bedrock
		this.block = TrackedModelFactory.createTrackedBlock(location, "shrine", Material.GOLD_BLOCK);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if(this == obj) return true;
		if(obj == null || getClass() != obj.getClass()) return false;
		final Shrine other = (Shrine) obj;
		return Objects.equal(this.id, other.id) && Objects.equal(this.location, other.location) && Objects.equal(this.active, other.active) && Objects.equal(this.block, other.block);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.id, this.location, this.active, this.block);
	}

	@Override
	public String toString()
	{
		return Objects.toStringHelper(this).add("id", this.id).add("location", this.location).add("active", this.active).add("blocks", this.block).toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
