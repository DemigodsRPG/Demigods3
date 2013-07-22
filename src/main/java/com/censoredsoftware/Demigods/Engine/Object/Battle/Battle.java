package com.censoredsoftware.Demigods.Engine.Object.Battle;

import java.util.Set;

import org.bukkit.Location;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsLocation;

@Model
public class Battle
{
	@Id
	private Long id;
	@Reference
	private BattleMeta meta;
	@Reference
	@Indexed
	private DemigodsLocation startLoc;
	@Attribute
	private double range;
	@Attribute
	private long duration;
	@Attribute
	@Indexed
	private long startTime;

	public static Battle create(BattleParticipant damager, BattleParticipant damaged)
	{
		Battle battle = new Battle();
		battle.setStartLocation(damager.getCurrentLocation().toVector().getMidpoint(damaged.getCurrentLocation().toVector()).toLocation(damager.getCurrentLocation().getWorld()));
		battle.setStartTime(System.currentTimeMillis());

		int default_range = Demigods.config.getSettingInt("battles.min_range");
		double range = damager.getCurrentLocation().distance(damaged.getCurrentLocation());
		if(range < default_range) battle.setRange(default_range);
		else battle.setRange(range);

		battle.setDuration(Demigods.config.getSettingInt("battles.min_duration") * 1000);

		BattleMeta meta = BattleMeta.create(damager);
		meta.addParticipant(damager);
		meta.addParticipant(damaged);
		battle.setMeta(meta);
		Battle.save(battle);
		return battle;
	}

	void setMeta(BattleMeta meta)
	{
		this.meta = meta;
	}

	public void setRange(double range)
	{
		this.range = range;
	}

	public void setDuration(long duration)
	{
		this.duration = duration;
	}

	void setStartLocation(Location location)
	{
		this.startLoc = DemigodsLocation.create(location);
	}

	void setStartTime(long time)
	{
		this.startTime = time;
	}

	public long getId()
	{
		return this.id;
	}

	public double getRange()
	{
		return this.range;
	}

	public long getDuration()
	{
		return this.duration;
	}

	public BattleMeta getMeta()
	{
		return this.meta;
	}

	public Location getStartLocation()
	{
		return this.startLoc.toLocation();
	}

	public long getStartTime()
	{
		return this.startTime;
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
