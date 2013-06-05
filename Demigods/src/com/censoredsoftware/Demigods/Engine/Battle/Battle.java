package com.censoredsoftware.Demigods.Engine.Battle;

import java.util.Set;

import javax.persistence.Id;

import org.bukkit.Location;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Tracked.TrackedLocation;
import com.censoredsoftware.Demigods.Engine.Tracked.TrackedModelFactory;

@Model
public class Battle
{
	@Id
	private Long id;
	@Reference
	private BattleMeta meta;
	@Reference
	@Indexed
	private TrackedLocation startLoc;
	@Reference
	@Indexed
	private TrackedLocation endLoc;
	@Attribute
	@Indexed
	private long startTime;
	@Attribute
	@Indexed
	private long endTime;
	@Attribute
	@Indexed
	private Boolean active;

	void setMeta(BattleMeta meta)
	{
		this.meta = meta;
	}

	void setStartLocation(Location location)
	{
		this.startLoc = TrackedModelFactory.createTrackedLocation(location);
	}

	void setStartTime(long time)
	{
		this.startTime = time;
	}

	public void setEndLocation(Location location)
	{
		this.endLoc = TrackedModelFactory.createTrackedLocation(location);
	}

	public void setEndTime(long time)
	{
		this.endTime = time;
	}

	public void setActive(boolean option)
	{
		this.active = option;
	}

	public boolean isActive()
	{
		return this.active;
	}

	public long getId()
	{
		return this.id;
	}

	public BattleMeta getMeta()
	{
		return this.meta;
	}

	public Location getStartLocation()
	{
		return this.startLoc.toLocation();
	}

	public Location getEndLocation()
	{
		return this.endLoc.toLocation();
	}

	public long getStartTime()
	{
		return this.startTime;
	}

	public long getEndTime()
	{
		return this.endTime;
	}

	public static Battle load(Long id)
	{
		return JOhm.get(Battle.class, id);
	}

	public static Set<Battle> loadAll()
	{
		return JOhm.getAll(Battle.class);
	}

	public static void save(Battle battle)
	{
		JOhm.save(battle);
	}

	public void delete()
	{
		JOhm.delete(Battle.class, getId());
	}
}
