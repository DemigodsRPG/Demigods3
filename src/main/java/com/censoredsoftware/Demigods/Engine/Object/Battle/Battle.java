package com.censoredsoftware.Demigods.Engine.Object.Battle;

import java.util.Set;

import org.bukkit.Location;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsLocation;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;

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
	@Reference
	@Indexed
	private DemigodsLocation endLoc;
	@Attribute
	private double range;
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
		battle.setStartTime(System.currentTimeMillis());

		int default_range = Demigods.config.getSettingInt("battle.min_range");
		double range = damager.getOfflinePlayer().getPlayer().getLocation().distance(damagee.getOfflinePlayer().getPlayer().getLocation());
		if(range < default_range)
		{
			battle.setRange(default_range);
		}
		else
		{
			battle.setRange(range);
		}

		BattleMeta meta = BattleMeta.create(damager);
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
		this.startLoc = DemigodsLocation.create(location);
	}

	void setStartTime(long time)
	{
		this.startTime = time;
	}

	public void setEndLocation(Location location)
	{
		this.endLoc = DemigodsLocation.create(location);
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
