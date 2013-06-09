package com.censoredsoftware.Demigods.Engine.Battle;

import java.util.Set;

import org.bukkit.Location;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;
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
	private Double range;
	@Attribute
	@Indexed
	private long startTime;
	@Attribute
	@Indexed
	private long endTime;
	@Attribute
	@Indexed
	private Boolean active;

	public static Battle create(PlayerCharacter damager, PlayerCharacter damagee)
	{
		Battle battle = new Battle();
		battle.setActive(true);
		battle.setStartLocation(damager.getOfflinePlayer().getPlayer().getLocation().toVector().getMidpoint(damagee.getOfflinePlayer().getPlayer().getLocation().toVector()).toLocation(damager.getOfflinePlayer().getPlayer().getWorld()));
		battle.setRange(damager.getOfflinePlayer().getPlayer().getLocation().distance(damagee.getOfflinePlayer().getPlayer().getLocation()));
		battle.setStartTime(System.currentTimeMillis());
		BattleMeta meta = BattleMeta.create(damager);
		meta.addLocation(damager.getOfflinePlayer().getPlayer().getLocation());
		meta.addLocation(damagee.getOfflinePlayer().getPlayer().getLocation());
		meta.addParticipant(damager);
		meta.addParticipant(damagee);
		battle.setMeta(meta);
		Battle.save(battle);
		return battle;
	}

	void setMeta(BattleMeta meta)
	{
		this.meta = meta;
	}

	void setRange(double range)
	{
		this.range = range;
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

	public double getRange()
	{
		return this.range;
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

	public static Battle get(Long id)
	{
		return JOhm.get(Battle.class, id);
	}

	public static Set<Battle> getAll()
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
